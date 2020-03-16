import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/26
 */
public class DataTest {

    @Test
    public void test(){
        int i=0;
        while(i<100){
            System.out.println((System.nanoTime() & 0xfffffff)+"8" );
            i++;
        }
    }

    @Test
    public void test1(){
        long nanotime = System.nanoTime();
        System.out.println(nanotime);
        System.out.println(nanotime>>9);//17883084497
        System.out.println(nanotime & 0x7fffffff);
        System.out.println(nanotime & 0xfffffff);
        System.out.println(nanotime>>13);
        System.out.println(Integer.MAX_VALUE);//2147483647
        System.out.println((int)(nanotime>>13));
    }

    /**
     * date
     */
    @Test
    public void test2(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd:HH:mm:ss.SSSSSS ZZ");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd:HH:mm:ss.SSS ZZ");
        Date date = new Date();
        System.out.println(sdf.format(date));
        System.out.println(sdf1.format(date));
    }
}
