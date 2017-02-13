import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Robin on 2016. 09. 30..
 */
public class LinearActivationFunction implements ActivationFunction{

    @Override
    public RealVector Activate(RealVector x) {
        return x;
    }

    @Override
    public RealVector Derivative(RealVector x) {
        int size = x.getDimension();
        double[] b = new double[size];
        for(int i=0; i< size; i++){
            b[i] = 1.d;
        }
        RealVector a = new ArrayRealVector(b);
        return a;
    }
}
