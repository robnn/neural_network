import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorFormat;

import java.util.Vector;

/**
 * Created by Robin on 2016. 10. 08..
 */
public class NeuralNetwork {
    private Vector<NeuralLayer> neuralLayers = new Vector<>();
    private NeuralLayer inputLayer;
    private NeuralLayer outputLayer;
    private Vector<double[]> dataInput;
    private Vector<double[]> dataOutput;
    private Vector<double[]> neededOutput;
    private int[] architecture;

    private int epochCount;
    private double learningInputRate;
    private RealVector averageQuadraticError;
    public NeuralNetwork(int[] architecture) {
        this.architecture = architecture;
        generateLayers();
        setActivations();
        dataOutput = new Vector<>();
    }

    private void generateLayers() {
        neuralLayers = new Vector<>();
        for (int i = 0; i < architecture.length; i++) {
            neuralLayers.add(new NeuralLayer(architecture, i));
        }
        neuralLayers.get(0).setInputLayer(true);
        inputLayer = neuralLayers.get(0);
        outputLayer = neuralLayers.get(neuralLayers.size()-1);
        outputLayer.setOutputLayer(true);
        setAllNextLayers();
    }
    public void printArchitecture(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<architecture.length; i++){
            sb.append(architecture[i]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
    }
    public void printWeights(){
        neuralLayers.forEach(NeuralLayer::printWeights);
    }
    public void setMatrixes(Vector<double[][]> weights) {
        for (int i = 1; i < weights.size(); i++) {
            neuralLayers.get(i).setWeightAndBiasMatrix(weights.get(i));
        }
    }

    public int[] getArchitecture() {
        return architecture;
    }

    public void setDataInput(Vector<double[]> inputs) {
        this.dataInput = inputs;
    }
    private void setActivations(){
        ActivationFunction relu = new ReluActivationFunction();
        ActivationFunction linear = new LinearActivationFunction();
        for (NeuralLayer nl: neuralLayers){ //at first set relu for all
            nl.setActivationFunction(relu);
        }
        outputLayer.setActivationFunction(linear);//at the end set linear for the output layer
    }
    public void solve(){
        for(double[] d: dataInput) {//for every input
            NeuralLayer neuralLayer = inputLayer;
            RealVector input = new ArrayRealVector(d);
            neuralLayer.setInput(input);
            RealVector out;
            do { //for every layer, starting with the input
                out = neuralLayer.solve(); //solve the layer
                neuralLayer= neuralLayers.get(neuralLayers.indexOf(neuralLayer)+1); //next layer
                neuralLayer.setInput(out); //set the previous output as input
            }while (neuralLayer != outputLayer); //back testing cycle needed, until the last layer
            outputLayer.setInput(out); //do it for the last layer
            out = outputLayer.solve();
            dataOutput.add(out.toArray()); //store the outputs in a vector
        }
    }
    public void printOutput(){
        int size = dataOutput.size();
        System.out.println(size);
        for(double[] d: dataOutput){
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i<d.length; i++){
                sb.append(d[i]);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb.toString());
        }
    }

    public void generatePartialDerivates() {
        for(int i = 1; i<neuralLayers.size();i++){ //except the first one
            neuralLayers.get(i).calcPartialDerivates();
        }
        for(int i = 1; i<neuralLayers.size();i++){ //except the first one
            neuralLayers.get(i).setWeightAndBiasMatrixFromPartialDerivates();
        }
    }
    private void setAllNextLayers(){
        //nextLayer set for every layer
        for(int i = 0; i<neuralLayers.size()-1;i++){ //except the last one
            neuralLayers.get(i).setNextLayer(neuralLayers.get(i + 1));
        }
    }
    public void learn(){

        int learningCount = (int)Math.floor(dataInput.size()*learningInputRate);
        for(int i=0; i<epochCount; i++){//for epoch count
            for(int j=0; j<learningCount; j++) {
                double[] d = dataInput.get(j);
                NeuralLayer neuralLayer = inputLayer;
                RealVector input = new ArrayRealVector(d);
                neuralLayer.setInput(input);
                RealVector out;
                do { //for every layer, starting with the input
                    out = neuralLayer.solve(); //solve the layer
                    neuralLayer= neuralLayers.get(neuralLayers.indexOf(neuralLayer)+1); //next layer
                    neuralLayer.setInput(out); //set the previous output as input
                }while (neuralLayer != outputLayer); //back testing cycle needed, until the last layer
                outputLayer.setInput(out); //do it for the last layer
                out = outputLayer.solve();

                outputLayer.setNeededOutput(neededOutput.get(dataInput.indexOf(d)));
                for(int k = 1; k<neuralLayers.size();k++){ //except the first one
                    neuralLayers.get(k).recalcWeights();
                }
                for(int k = 1; k<neuralLayers.size();k++){ //except the first one
                    neuralLayers.get(k).setNewWeightAndBiasAsPresentValues();
                }
            }
            //calculating quadratic errors
            int errorCount =0;
            averageQuadraticError = new ArrayRealVector(outputLayer.getBiasVector().getDimension()); //how much neurons in output layer
            for(int j=learningCount; j<dataInput.size(); j++) {
                double[] d = dataInput.get(j);
                NeuralLayer neuralLayer = inputLayer;
                RealVector input = new ArrayRealVector(d);
                neuralLayer.setInput(input);
                RealVector out;
                do { //for every layer, starting with the input
                    out = neuralLayer.solve(); //solve the layer
                    neuralLayer= neuralLayers.get(neuralLayers.indexOf(neuralLayer)+1); //next layer
                    neuralLayer.setInput(out); //set the previous output as input
                }while (neuralLayer != outputLayer); //back testing cycle needed, until the last layer
                outputLayer.setInput(out); //do it for the last layer
                out = outputLayer.solve();

                outputLayer.setNeededOutput(neededOutput.get(dataInput.indexOf(d)));
                RealVector oneError = outputLayer.calcQuadraticError();
                averageQuadraticError = averageQuadraticError.add(oneError);
                errorCount++;
            }
            averageQuadraticError = averageQuadraticError.mapDivide(errorCount).mapDivide(averageQuadraticError.getDimension());
            printQuadraticError();
        }

    }
    public void setLearningParameters(double[] parameters){
        epochCount=(int)parameters[0];
        learningInputRate = parameters[2];
        for(NeuralLayer n: neuralLayers){
            n.setCourageValue(parameters[1]);
        }

    }
    public void setNeededOutput(Vector<double[]> neededOutput) {
        this.neededOutput = neededOutput;
    }

    public void printQuadraticError() {
        //RealVectorFormat format = new RealVectorFormat("","",",");
        //System.out.println(format.format(averageQuadraticError));
        double d = 0;
        for(int i = 0;i<averageQuadraticError.getDimension(); i++){
            d +=averageQuadraticError.getEntry(i);
        }
        System.out.println(d);
    }
}
