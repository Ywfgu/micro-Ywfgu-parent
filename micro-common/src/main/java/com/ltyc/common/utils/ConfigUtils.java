package com.ltyc.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ConfigUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigUtils.class);

    public static Map LoadConf(String arg) {
        if (arg.endsWith("yaml")) {
            return LoadYaml(arg);
        } else {
            return LoadProperty(arg);
        }
    }

    public static Map LoadYaml(String confPath) {
        return findAndReadYaml(confPath, true, true);
    }

    public static Map LoadProperty(String prop) {
        InputStream in = null;
        Properties properties = new Properties();

        try {
            in = getConfigFileInputStream(prop);
            properties.load(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such file " + prop);
        } catch (Exception e1) {
            throw new RuntimeException("Failed to read config file");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Map ret = new HashMap();
        ret.putAll(properties);
        return ret;
    }

    /**
     * @param name        conf file name
     * @param mustExist   if true, the file must exist, otherwise throw an exception
     * @param canMultiple if false and there is multiple conf, it will throw an exception
     * @return conf map
     */
    public static Map findAndReadYaml(String name, boolean mustExist, boolean canMultiple) {
        InputStream in = null;
        boolean confFileEmpty = false;
        try {
            in = getConfigFileInputStream(name, canMultiple);
            if (null != in) {
                Yaml yaml = new Yaml(new SafeConstructor());
                Map ret = (Map) yaml.load(new InputStreamReader(in));
                if (null != ret) {
                    return new HashMap(ret);
                } else {
                    confFileEmpty = true;
                }
            }

            if (mustExist) {
                if (confFileEmpty)
                    throw new RuntimeException("Config file " + name + " doesn't have any valid storm configs");
                else
                    throw new RuntimeException("Could not find config file on classpath " + name);
            } else {
                return new HashMap();
            }
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid configuration ").append(name).append(":").append(e.getMessage());
            throw new RuntimeException(sb.toString(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static InputStream getConfigFileInputStream(String configFilePath) throws IOException {
        return getConfigFileInputStream(configFilePath, true);
    }

    public static InputStream getConfigFileInputStream(String configFilePath, boolean canMultiple) throws IOException {
        if (null == configFilePath) {
            throw new IOException("Could not find config file, name not specified");
        }

        HashSet<URL> resources = new HashSet<>(findResources(configFilePath));
        if (resources.isEmpty()) {
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                return new FileInputStream(configFile);
            }
        } else if (resources.size() > 1 && !canMultiple) {
            throw new IOException("Found multiple " + configFilePath + " resources. " +
                    "You're probably bundling storm jars with your topology jar. " + resources);
        } else {
            LOG.debug("Using " + configFilePath + " from resources");
            URL resource = resources.iterator().next();
            return resource.openStream();
        }
        return null;
    }

    public static List<URL> findResources(String name) {
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(name);
            List<URL> ret = new ArrayList<>();
            while (resources.hasMoreElements()) {
                ret.add(resources.nextElement());
            }
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
