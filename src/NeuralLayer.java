import org.apache.commons.math3.linear.*;

/**
 * Created by Robin on 2016. 10. 08..
 */
public class NeuralLayer {
    private RealMatrix weightAndBiasMatrix; //contains the biases too
    private RealMatrix weightMatrix;
    private RealMatrix newWeight;
    private RealMatrix partialDerivateWeigthMatrix;
    private RealVector partialDerivateBiasVector;
    private RealVector biasVector;
    private RealVector newBias;
    private RealVector s; //output without the activation function
    private RealVector input;
    private RealVector output;
    private ActivationFunction activationFunction;
    private boolean isInputLayer;
    private boolean isOutputLayer;
    private NeuralLayer nextLayer;
    private RealVector neededOutput;
    private double courageValue;

    public NeuralLayer(int[] architecture, int i) {
        if(i == 0) {
            weightAndBiasMatrix = null;
        }
        else {
            WeigthRandomizer weigthRandomizer = new WeigthRandomizer();
            weightAndBiasMatrix = new Array2DRowRealMatrix(architecture[i], architecture[i-1]+1);
            weightAndBiasMatrix.walkInColumnOrder(weigthRandomizer);
        }

    }
    public void setInputLayer(boolean inputLayer) {
        isInputLayer = inputLayer;
    }

    public void setOutputLayer(boolean outputLayer) {
        isOutputLayer = outputLayer;
    }

    public void printWeights(){
        if(isInputLayer) {

        }
        else {
            RealMatrixFormat format = new RealMatrixFormat("", "", "", "", "\n", ",");
            System.out.println(format.format(weightAndBiasMatrix));
        }
    }
    private void setBiasVector(){
        this.biasVector = weightAndBiasMatrix.getColumnVector(weightAndBiasMatrix.getColumnDimension()-1);
    }
    private void setWeightMatrix(){
        double[][] weighs = new double[weightAndBiasMatrix.getRowDimension()][weightAndBiasMatrix.getColumnDimension()-1];
        this.weightAndBiasMatrix.copySubMatrix(0,weightAndBiasMatrix.getRowDimension()-1,0,weightAndBiasMatrix.getColumnDimension()-2, weighs);
        weightMatrix = new Array2DRowRealMatrix(weighs);
    }

    public void setWeightAndBiasMatrix(double[][] weightAndBiasMatrix) {
        if(weightAndBiasMatrix == null){
            throw new RuntimeException("U'RE WRONG! wrong call for the first layer, fixit");
        }
        this.weightAndBiasMatrix = new Array2DRowRealMatrix(weightAndBiasMatrix);
        setBiasVector();
        setWeightMatrix();
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }
    public void setInput(RealVector input) {
        this.input = input;
    }

    public RealVector solve(){
        if(weightAndBiasMatrix == null)
            return input;
        setWeightAndBiasMatrix(this.weightAndBiasMatrix.getData());
        s = weightMatrix.transpose().preMultiply(input).add(biasVector);
        RealVector activated = activationFunction.Activate(s);
        output = activated;
        return  activated;
    }


    public void setNextLayer(NeuralLayer nextLayer) {
        this.nextLayer = nextLayer;
    }

    private RealVector onlyPartialDeltaFunction(NeuralLayer neuralLayer){
        if(neuralLayer.isOutputLayer){
            return neuralLayer.activationFunction.Derivative(neuralLayer.output); //outputnak megfelelő méretü 1esekkel teli vektor
        }
        else{
            neuralLayer = nextLayer;
            RealVector rv = ((neuralLayer.onlyPartialDeltaFunction(neuralLayer)));
            RealMatrix rm = neuralLayer.getWeightMatrix();
            RealVector rv2 = activationFunction.Derivative(s);
            RealVector rv3 = rm.preMultiply(rv);
           return ( rv2.ebeMultiply(rv3));
        }
    }
    private RealVector deltaFunctionWithError(NeuralLayer neuralLayer){
        if(neuralLayer.isOutputLayer) {
            return neededOutput.subtract(output);
        }
        else{
            neuralLayer = nextLayer;
            RealVector rv = ((neuralLayer.deltaFunctionWithError(neuralLayer)));
            RealMatrix rm = neuralLayer.getWeightMatrix();
            RealVector rv2 = activationFunction.Derivative(s);
            RealVector rv3 = rm.preMultiply(rv);
            return ( rv2.ebeMultiply(rv3));
        }
    }
    public void recalcWeights(){
        RealVector delta = deltaFunctionWithError(this);
        newBias = biasVector.add(delta.mapMultiply(2*courageValue));
        newWeight = weightMatrix.add((delta.mapMultiply(2*courageValue)).outerProduct(input));

    }

    public void setCourageValue(double courageValue) {
        this.courageValue = courageValue;
    }

    public void setNeededOutput(double[] neededOutput) {
        if(isOutputLayer){
            this.neededOutput = new ArrayRealVector(neededOutput);
        }
        else {
            throw new RuntimeException("Do not set the needed output for layers except the outputlayer!!");
        }
    }

    public void setNewWeightAndBiasAsPresentValues(){
        weightMatrix = newWeight;
        biasVector = newBias;
        weightAndBiasMatrix.setSubMatrix(weightMatrix.getData(), 0,0);
        weightAndBiasMatrix.setColumnVector(weightAndBiasMatrix.getColumnDimension()-1,biasVector);
    }

    public void calcPartialDerivates(){
        partialDerivateBiasVector = onlyPartialDeltaFunction(this);
        RealMatrix rm = partialDerivateBiasVector.outerProduct(input);
        partialDerivateWeigthMatrix = new Array2DRowRealMatrix(rm.getRowDimension(), rm.getColumnDimension() +1);
        partialDerivateWeigthMatrix.setSubMatrix(rm.getData(),0,0);
       partialDerivateWeigthMatrix.setColumnVector(partialDerivateWeigthMatrix.getColumnDimension()-1, partialDerivateBiasVector);

    }
    public RealMatrix getWeightMatrix() {
        return weightMatrix;
    }

    public RealVector getBiasVector() {
        return biasVector;
    }

    public void setWeightAndBiasMatrixFromPartialDerivates(){
        weightAndBiasMatrix = partialDerivateWeigthMatrix;
    }
    public RealVector calcQuadraticError(){
        RealVector rv = (neededOutput.subtract(output)).ebeMultiply(neededOutput.subtract(output));
        return (rv);
    }

}
