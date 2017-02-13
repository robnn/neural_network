import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Robin on 2016. 10. 08..
 */
public class NNSolutionThree {
    public static void main(String[] args) {
        Reader reader = new Reader();

        int[] architecture = reader.readArchitetcture();
        NeuralNetwork nw = new NeuralNetwork(architecture);
        nw.setMatrixes(reader.readWeights(nw.getArchitecture()));
        nw.setDataInput(reader.readInput());
        nw.solve();
        nw.generatePartialDerivates();
        nw.printArchitecture();
        nw.printWeights();

    }
}
