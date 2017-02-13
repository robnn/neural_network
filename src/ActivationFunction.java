import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Robin on 2016. 09. 30..
 */
public interface ActivationFunction {
    public RealVector Activate(RealVector x);
    public RealVector Derivative(RealVector x);
}
