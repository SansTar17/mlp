package Preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocess {
    
    
    public List<String[]> readCSV(String csvFilePath) {
        String line = "";
        String delimiter = ",";
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine();

            while ((line = br.readLine()) != null) {
                // Split the line into an array of values
                String[] row = line.split(delimiter);
                // Add the row to the data list
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public void printHead(List<String[]> data, int numRows) {
    System.out.println("Head of the data:");
        for (int i = 0; i < Math.min(numRows, data.size()); i++) {
            String[] row = data.get(i);
            for (String value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }
    
    public void printHead(double[][] data, int numRows) {
        System.out.println("Head of the data:");
        for (int i = 0; i < Math.min(numRows, data.length); i++) {
            double[] row = data[i];
            for (double value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }
    
    public void printHead(double[] data, int numRows) {
        System.out.println("Head of the data:");
        int numCols = Math.min(numRows, data.length);
        for (int i = 0; i < numCols; i++) {
            System.out.print(data[i] + "\t");
        }
        System.out.println();
    }

    public List<String[]> encodeData(List<String[]> data) {
        Map<String, Integer> sexMap = new HashMap<>();
        sexMap.put("M", 0);
        sexMap.put("F", 1);

        Map<String, Integer> bpMap = new HashMap<>();
        bpMap.put("LOW", 0);
        bpMap.put("NORMAL", 1);
        bpMap.put("HIGH", 2);

        Map<String, Integer> cholMap = new HashMap<>();
        cholMap.put("NORMAL", 0);
        cholMap.put("HIGH", 1);

        Map<String, Integer> drugMap = new HashMap<>();
        drugMap.put("drugA", 0);
        drugMap.put("drugB", 1);
        drugMap.put("drugC", 2);
        drugMap.put("drugX", 3);
        drugMap.put("DrugY", 4);

        for (String[] row : data) {
            row[1] = sexMap.get(row[1]).toString(); // map sex attribute to integers
            row[2] = bpMap.get(row[2]).toString(); // map bp attribute to integers
            row[3] = cholMap.get(row[3]).toString(); // map cholesterol attribute to integers
            row[5] = drugMap.get(row[5]).toString(); // map drug attribute to integers
        }

        return data;
    }
    
    public double[][][] splitData(double[][] inputData, double splitPercentage) {
        int numInstances = inputData.length;
        int numTrain = (int) Math.round(numInstances * splitPercentage);
        int numTest = numInstances - numTrain;
        int numAttributes = inputData[0].length;

        double[][] trainData = new double[numTrain][numAttributes];
        double[][] testData = new double[numTest][numAttributes];

        // Shuffle the input data
        List<double[]> shuffledInputData = Arrays.asList(inputData);
        Collections.shuffle(shuffledInputData);

        // Assign the shuffled data to train and test sets
        for (int i = 0; i < numTrain; i++) {
            trainData[i] = shuffledInputData.get(i);
        }
        for (int i = 0; i < numTest; i++) {
            testData[i] = shuffledInputData.get(i + numTrain);
        }

        return new double[][][] { trainData, testData };
    }
    
    public double[][] splitAttributes(double[][] inputData) {
        int numRows = inputData.length;
        int numCols = inputData[0].length;
        double[][] result = new double[numRows][numCols-1];
        for(int i=0; i<numRows; i++) {
            for(int j=0; j<numCols-1; j++) {
                result[i][j] = inputData[i][j];
            }
        }
        return result;
    }

    public double[] splitLabels(double[][] inputData) {
        int numRows = inputData.length;
        int numCols = inputData[0].length;
        double[] result = new double[numRows];
        for(int i=0; i<numRows; i++) {
            result[i] = inputData[i][numCols-1];
        }
        return result;
    }

}
