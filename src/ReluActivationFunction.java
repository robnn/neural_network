import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Robin on 2016. 09. 30..
 */
public class ReluActivationFunction implements ActivationFunction{
        @Override
    public RealVector Activate(RealVector x) {
            ReluForRealVector reluForRealVector = new ReluForRealVector();
            x.walkInDefaultOrder(reluForRealVector);
        return x;
    }

    @Override
    public RealVector Derivative(RealVector x) {
        int size = x.getDimension();
        double[] b = new double[size];
        for(int i=0; i< size; i++){
          if(x.getEntry(i) >0)
              b[i] = 1.d;
          else
              b[i] = 0.d;
        }
        RealVector a = new ArrayRealVector(b);
        return a;
    }
}
