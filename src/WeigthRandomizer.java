import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

/**
 * Created by Robin on 2016. 10. 08..
 */
public class WeigthRandomizer implements RealMatrixChangingVisitor {
    private int endColumn;
    @Override
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
        this.endColumn = endColumn;
    }

    @Override
    public double visit(int row, int column, double value) {
        if(column == endColumn){
            return 0.0d;
        }
        return Generator.generateGaussianWeight();
    }

    @Override
    public double end() {
        return 0;
    }
}
