import org.apache.commons.math3.linear.RealVectorChangingVisitor;

/**
 * Created by Robin on 2016. 10. 08..
 */
public class ReluForRealVector implements RealVectorChangingVisitor {
    @Override
    public void start(int dimension, int start, int end) {

    }

    @Override
    public double visit(int index, double value) {
        if(value>0)
            return value;
        return 0;
    }

    @Override
    public double end() {
        return 0;
    }
}
