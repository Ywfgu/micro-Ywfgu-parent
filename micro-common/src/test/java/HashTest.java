import com.google.common.primitives.Bytes;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/29
 */
public class HashTest {

    //c06c1150c34267c67cce75d5960ee974
    @Test
    public void test1(){
        byte[] userBytes = "901783".getBytes();
        byte[] passwdBytes = "ICP001".getBytes();
        String timestamp = "229112534";
        System.out.println(DigestUtils.md5Hex(Bytes.concat(userBytes, new byte[9], passwdBytes, timestamp.getBytes())));
    }

    @Test
    public void test2(){
        byte[] userBytes = "901783".getBytes();
        byte[] passwdBytes = "ICP001".getBytes();
        Long timestamp = 229112534L;

        byte[] timestampBytes = (timestamp+"").getBytes();
        System.out.println(DigestUtils.md5Hex(Bytes.concat(userBytes, new byte[9], passwdBytes, timestampBytes)));
    }
}
