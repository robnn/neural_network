import java.util.DoubleSummaryStatistics;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Robin on 2016. 09. 30..
 */
public class Reader {
    private Scanner scanner;
    private Vector<double[]> neededOutputs;
    Reader(){
        scanner = new Scanner(System.in);
    }
    public int[] readArchitetcture(){
        String s;
        String[] separated;
        s = scanner.nextLine();
        separated = s.split(",");
        int[] architecture = new int[separated.length];

        for(int i=0; i<separated.length; i++){
           architecture[i] = Integer.parseInt(separated[i]);
        }
        return architecture;
    }


    private double[][] readWeightsForOneLayer(int neuronCount){ //Reads the weights for one layer;
        String[] s = new String[neuronCount];
        for(int i = 0; i <neuronCount;i++){ //reading the lines, just for one layer
            s[i] = scanner.nextLine();
        }
        double[][] weights = new double[s.length][];
        for(int i= 0;i<s.length; i++){
            String[] separated = s[i].split(",");
            weights[i] = new double[separated.length];
            for(int j=0; j<separated.length; j++){
                weights[i][j] = Double.parseDouble(separated[j]);
            }
        }

        return weights;
    }

    public  Vector<double[][]> readWeights(int[] architecture) {

        Vector<double[][]> readed = new Vector<>();
        readed.add(null); //first layer is input layer, havent got weights
        for (int i = 1; i < architecture.length; i++) {

            double[][] weight = readWeightsForOneLayer(architecture[i]);
            readed.add(weight);
        }
        return readed;
    }
    public Vector<double[]> readInput(){
        int howMuchInput=Integer.parseInt(scanner.nextLine());
        String[] s = new String[howMuchInput];
        Vector<double[]> inputs = new Vector<>();
        for(int i=0; i<howMuchInput; i++){
            s[i] = scanner.nextLine();
        }
        for(int i=0; i<howMuchInput; i++){
            String[] separated = s[i].split(",");
            double[] doubles = new double[separated.length];

            for(int j=0; j<separated.length; j++){
                doubles[j] = Double.parseDouble(separated[j]);
            }
            inputs.add(doubles);
        }

        return inputs;
    }
    public Vector<double[]> readLearningPattern(int[] architecture){ //TODO
        int howMuchInput=Integer.parseInt(scanner.nextLine());
        String[] s = new String[howMuchInput];
        String[] expectedValues =  new String[howMuchInput];;
        int howMuchExpectedValues = architecture[architecture.length-1];
        Vector<double[]> inputs = new Vector<>();
        neededOutputs = new Vector<>();
        for(int i=0; i<howMuchInput; i++){
            s[i] = scanner.nextLine();
        }

        for(int i=0; i<howMuchInput; i++){
            String[] separated = s[i].split(",");
            double[] doubles = new double[separated.length-howMuchExpectedValues];

            for(int j=0; j<separated.length-howMuchExpectedValues; j++){
                doubles[j] = Double.parseDouble(separated[j]);
            }
            double[] neededOutput = new double[howMuchExpectedValues];
            int k=0;
            for(int j=separated.length-howMuchExpectedValues; j<separated.length; j++){
                neededOutput[k] = Double.parseDouble(separated[j]);
                k++;
            }
            neededOutputs.add(neededOutput);
            inputs.add(doubles);
        }

        return inputs;
    }
    public double[] readLearningParams(){
        String s = scanner.nextLine();
        String[] separated = s.split(",");
        double[] doubles = new double[separated.length];
        for(int j=0; j<separated.length; j++){
            doubles[j] = Double.parseDouble(separated[j]);
        }
        return doubles;
    }

    public Vector<double[]> getNeededOutputs() {
        return neededOutputs;
    }
}
