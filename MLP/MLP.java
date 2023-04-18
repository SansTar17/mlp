package MLP;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class MLP implements Serializable{

    private double[][] hiddenWeights; // weights of hidden layer
    private double[][] outputWeights; // weights of output layer
    private double[] hiddenBiases; // biases of hidden layer
    private double[] outputBiases; // biases of output layer
    private int inputSize; // number of inputs
    private int hiddenSize; // number of neurons in hidden layer
    private int outputSize; // number of neurons in output layer
    private double learningRate; // learning rate for backpropagation

    public MLP(int inputSize, int hiddenSize, int outputSize, double learningRate) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.learningRate = learningRate;
        this.hiddenWeights = new double[inputSize][hiddenSize];
        this.outputWeights = new double[hiddenSize][outputSize];
        this.hiddenBiases = new double[hiddenSize];
        this.outputBiases = new double[outputSize];
        Random random = new Random();
        // initialize weights and biases randomly
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                hiddenWeights[i][j] = random.nextDouble() - 0.5;
            }
        }
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                outputWeights[i][j] = random.nextDouble() - 0.5;
            }
            hiddenBiases[i] = random.nextDouble() - 0.5;
        }
        for (int i = 0; i < outputSize; i++) {
            outputBiases[i] = random.nextDouble() - 0.5;
        }
    }

    public double[] feedForward(double[] input) {

        double[] hiddenOutput = new double[hiddenSize];
        double[] output = new double[outputSize];

        // compute hidden layer output
        for (int i = 0; i < hiddenSize; i++) {
            double net = 0;
            for (int j = 0; j < inputSize; j++) {
                net += input[j] * hiddenWeights[j][i];
            }
            hiddenOutput[i] = sigmoid(net + hiddenBiases[i]);
        }
        // compute output layer output
        for (int i = 0; i < outputSize; i++) {
            double net = 0;
            for (int j = 0; j < hiddenSize; j++) {
                net += hiddenOutput[j] * outputWeights[j][i];
            }
            output[i] = sigmoid(net + outputBiases[i]);
        }
        return output;
    }
      
    public double sigmoid(double x) {
            return 1.0 / (1.0 + Math.exp(-x));
    }
    
    public double sigmoidDerivative(double x) {
        double sigmoid = 1.0 / (1.0 + Math.exp(-x));
        double derivative = sigmoid * (1 - sigmoid);
        return derivative;
    }
          
    public void train(double[][] inputs, double[] targets, double stopError) {

        double[][] newTargets = new double[targets.length][outputSize];
        for (int i = 0; i < targets.length; i++) {
            newTargets[i][(int)targets[i]] = 1;
        }

        int epoch = 0;
        double error = Double.MAX_VALUE;
        while (error > stopError) {

            error = 0.0;

            for (int i = 0; i < inputs.length; i++) {

                double[] input = inputs[i];
                double[] target = newTargets[i];

                double[] hiddenOutput = new double[hiddenSize];
                double[] output = new double[outputSize];

                //feedfoward section
                // compute hidden layer output
                for (int j = 0; j < hiddenSize; j++) {
                    double net = 0;
                    for (int k = 0; k < inputSize; k++) {
                        net += input[k] * hiddenWeights[k][j];
                    }
                    hiddenOutput[j] = sigmoid(net + hiddenBiases[j]);
                }

                // compute output layer output
                for (int j = 0; j < outputSize; j++) {
                    double net = 0;
                    for (int k = 0; k < hiddenSize; k++) {
                        net += hiddenOutput[k] * outputWeights[k][j];
                    }
                    output[j] = sigmoid(net + outputBiases[j]);
                }

                // compute error
                double[] outputError = new double[outputSize];
                for (int j = 0; j < outputSize; j++) {
                    outputError[j] = (target[j] - output[j]) * output[j] * (1 - output[j]);
                    error += Math.abs(target[j] - output[j]);
                }
                
                double[] hiddenError = new double[hiddenSize];
                for (int j = 0; j < hiddenSize; j++) {
                    double net = 0;
                    for (int k = 0; k < outputSize; k++) {
                        net += outputError[k] * outputWeights[j][k];
                    }
                    hiddenError[j] = net * hiddenOutput[j] * (1 - hiddenOutput[j]);
                }

                //backpropagate section
                // update output weights and biases
                for (int j = 0; j < hiddenSize; j++) {
                    for (int k = 0; k < outputSize; k++) {
                        outputWeights[j][k] += learningRate * outputError[k] * hiddenOutput[j];
                    }
                }
                for (int j = 0; j < outputSize; j++) {
                    outputBiases[j] += learningRate * outputError[j];
                }

                // update hidden weights and biases
                for (int j = 0; j < inputSize; j++) {
                    for (int k = 0; k < hiddenSize; k++) {
                        hiddenWeights[j][k] += learningRate * hiddenError[k] * input[j];
                    }
                }
                for (int j = 0; j < hiddenSize; j++) {
                    hiddenBiases[j] += learningRate * hiddenError[j];
                }

            }
            error /= inputs.length;
            epoch++;
            System.out.printf("Epoch %d, error: %.5f\n", epoch, error);
        }

        // print the final weights
        System.out.println("Final weights:");
        System.out.println("Hidden weights:");
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                System.out.print(hiddenWeights[i][j] + " ");
            }
        System.out.println();
        }

        System.out.println("Output weights:");
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                System.out.print(outputWeights[i][j] + " ");
            }
            System.out.println();
        }

        // print the total epoch takes
        System.out.println("Total epoch: " + epoch);

        
    }
    
    public static double testMLP(MLP mlp, double[][] testData) {
        int numCorrect = 0;
        for (int i = 0; i < testData.length; i++) {
            double[] input = Arrays.copyOfRange(testData[i], 0, testData[i].length - 1);
            double[] output = mlp.feedForward(input);
            int predictedClass = findMaxIndex(output);
            int actualClass = (int) testData[i][testData[i].length - 1];
            if (predictedClass == actualClass) {
                numCorrect++;
            }
        }
        double accuracy = (double) numCorrect / testData.length;
        System.out.println("Accuracy: " + accuracy);
        return accuracy;
    }

    private static int findMaxIndex(double[] array) {
        int maxIndex = 0;
        double maxVal = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxVal) {
                maxIndex = i;
                maxVal = array[i];
            }
        }
        return maxIndex;
    }
    
}
