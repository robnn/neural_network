import java.util.Random;
import java.util.Vector;

/**
 * Created by Robin on 2016. 09. 30..
 */
public class Generator {
    public static double generateGaussianWeight(){
        Random r = new Random();
        return (r.nextGaussian()*0.1);
    }
}
