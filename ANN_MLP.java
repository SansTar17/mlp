 
import java.io.IOException;
import java.util.List;

import MLP.MLP;
import util.Convert;
import util.filehandling;
import Preprocess.Preprocess;


/**
 *
 * @author Amirul_Hafiz
 */
public class ANN_MLP {

    
    public static void main(String[] args) throws IOException {
        

        //PREPROCESS STEP
        //reading the dataset and save it into data variable
        Preprocess Preprocess = new Preprocess();
        String csvPath = "/root/java_codes/ann/dataset.csv";
        List<String[]> data = Preprocess.readCSV(csvPath);
        
        Preprocess.printHead(data, 5);
        
        data = Preprocess.encodeData(data);
        Preprocess.printHead(data, 5);
        
        Convert convert = new Convert();
        double[][] inputData = convert.convertToDoubleArray(data);
        
        
        System.out.println(inputData[0].length);
        double[][][] splitData = Preprocess.splitData(inputData, 0.7);
        double[][] trainData = splitData[0];
        double[][] testData = splitData[1];
        
        //INITIALIZE STEP
        // creating MLP model
        int inputSize = 5; // number of input columns
        int hiddenSize = 5; // number of neurons in hidden layer
        int outputSize = 5; // number of output neurons (for drug classification)
        
        MLP mlp = new MLP(inputSize, hiddenSize, outputSize, 0.01);

        double[][] inputs = Preprocess.splitAttributes(trainData);
        double[] target = Preprocess.splitLabels(trainData);
        
        //TRAINING STEP
        mlp.train(inputs, target, 0.3);
        
        //TEST STEP
        System.out.println();
        MLP.testMLP(mlp, testData);
        

        System.out.println("Prediction by user input");
        // input values for the new patient
        double age = 30;
        String sex = "M";
        String bp = "HIGH";
        String cholesterol = "NORMAL";
        double na_To_K = 22.5;

        // convert categorical variables (sex, bp, cholesterol) to one-hot encoding
        double[] sexInput = new double[2];
        if (sex.equals("F")) {
            sexInput[0] = 1;
        } else {
            sexInput[1] = 1;
        }
        double[] bpInput = new double[3];
        if (bp.equals("LOW")) {
            bpInput[0] = 1;
        } else if (bp.equals("NORMAL")) {
            bpInput[1] = 1;
        } else {
            bpInput[2] = 1;
        }
        double[] cholesterolInput = new double[2];
        if (cholesterol.equals("NORMAL")) {
            cholesterolInput[0] = 1;
        } else {
            cholesterolInput[1] = 1;
        }

        // combine input values into a single input array
        double[] input = new double[inputSize];
        
        input[0] = age;
        input[1] = sexInput[0];
        input[1] = sexInput[1];
        input[2] = bpInput[0];
        input[2] = bpInput[1];
        input[2] = bpInput[2];
        input[3] = cholesterolInput[0];
        input[3] = cholesterolInput[1];
        input[4] = na_To_K;

        // feed the input forward through the MLP to get the predicted output
        double[] output = mlp.feedForward(input);

        // find the index of the maximum value in the output array
        int predictedClass = Convert.findMaxIndex(output);

        // map the predicted class index to the actual drug name
        String predictedDrug;
        switch (predictedClass) {
            case 0:
                predictedDrug = "DrugA";
                break;
            case 1:
                predictedDrug = "DrugB";
                break;
            case 2:
                predictedDrug = "DrugC";
                break;
            case 3:
                predictedDrug = "DrugX";
                break;
            case 4:
                predictedDrug = "DrugY";
                break;
            default:
                predictedDrug = "Unknown";
        }

        // print the predicted drug name
        System.out.println("Predicted Drug: " + predictedDrug);
        
        filehandling.saveModel(mlp, "mlp.model", true);

    }
    
}
