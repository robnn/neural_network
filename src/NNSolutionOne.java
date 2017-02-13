/**
 * Created by Robin on 2016. 10. 08..
 */
public class NNSolutionOne {
    public static void main(String[] args) {
        Reader reader = new Reader();
        int[] architecture = reader.readArchitetcture();
        NeuralNetwork nw = new NeuralNetwork(architecture);
        nw.printArchitecture();
        nw.printWeights();
    }


}
