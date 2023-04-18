//remember that must train the MLP first
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import MLP.MLP;
import Preprocess.Preprocess;
import util.Convert;
import util.filehandling;

public class main_System {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {

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
        
        Scanner sc = new Scanner(System.in);
        MLP mlp = null;
        String modelPath = "mlp.model"; // default model file path

        try {
            mlp = filehandling.loadModel(modelPath);
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load model from file: " + modelPath);

        }

        boolean conds = true;
        while (conds) {
            System.out.println("\nMenu:");
            System.out.println("1: Load MLP");
            System.out.println("2: Retrain MLP");
            System.out.println("3: Retest MLP");
            System.out.println("4: Exit Program");
            System.out.print("Please enter your choice (1-4): ");
            
            int choice = sc.nextInt();
            sc.nextLine(); // consume the newline character
            
            switch (choice) {

                case 1:

                // list all the MLP models available in the folder
                System.out.println("List of available MLP models:");
                File folder = new File(".");
                File[] files = folder.listFiles((dir, name) -> name.startsWith("mlp") && name.endsWith(".model"));
                
                if (files.length == 0) {
                    System.out.println("No MLP models found in the folder!");
                    break;
                }
                
                for (int i = 0; i < files.length; i++) {
                    System.out.println((i+1) + ": " + files[i].getName());
                }

                // prompt the user to select an MLP model to load
                System.out.print("Enter the number of the MLP model to load: ");
                int selection = sc.nextInt();

                sc.nextLine(); // consume the newline character
                
                if (selection < 1 || selection > files.length) {
                    System.out.println("Invalid selection!");
                    break;
                }

                String filePath = files[selection-1].getName();

                mlp = filehandling.loadModel(filePath);
                
                if (mlp != null) {
                    System.out.println("MLP loaded successfully from file: " + filePath);
                } else {
                    System.out.println("Failed to load MLP from file: " + filePath);
                }
                break;

                    
                case 2:
                    // retrain MLP
                    double[][] inputs = Preprocess.splitAttributes(trainData);
                    double[] target = Preprocess.splitLabels(trainData);

                    mlp.train(inputs, target, 0.01);
                    break;
                    
                case 3:
                    // retest MLP
                    MLP.testMLP(mlp, testData);
                    break;
                    
                case 4:
                    // exit program
                    System.out.println("Exiting program...");
                    conds = false;
                    break;
                    
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
            }
            
            if (conds) {
                System.out.print("Do you want to save the MLP? (y/n): ");
                String yn = sc.nextLine();

                while (!yn.equalsIgnoreCase("y") && !yn.equalsIgnoreCase("n")) { 
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    yn = sc.nextLine();
                }

                if (yn.equalsIgnoreCase("y")) {
                    filehandling.saveModel(mlp, "mlp_2.model", true);
                    System.out.println("MLP saved successfully!");
                } else {
                    System.out.println("The MLP was not saved!");
                }
            }
        }
        
        sc.close(); // close scanner object
    }
}
