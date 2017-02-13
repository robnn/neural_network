/**
 * Created by Robin on 2016. 10. 09..
 */
public class NNSolutionFive {
    public static void main(String[] args) {
        Reader reader = new Reader();

        double[] learningParameters = reader.readLearningParams();

        int[] architecture = reader.readArchitetcture();

        NeuralNetwork nw = new NeuralNetwork(architecture);
        nw.setLearningParameters(learningParameters);

        //need the reading of the learning patterns here and set the input and neededOutput
        nw.setDataInput(reader.readLearningPattern(nw.getArchitecture()));
        nw.setNeededOutput(reader.getNeededOutputs());

        nw.learn();
        nw.printArchitecture();
        nw.printWeights();

    }
}
