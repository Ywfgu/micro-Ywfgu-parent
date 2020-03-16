import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/26
 */
public class IntTest {
    @Test
    public void test1(){
        int cnt = 0;
        while (true){
            System.out.println(RandomUtils.nextInt() & 0x4ff);
            if (cnt>100) {
                break;
            }
            cnt++;
        }
    }
    @Test
    public void test2(){
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
    }
}
