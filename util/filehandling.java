package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import MLP.MLP;

public class filehandling {
    
    public static void saveModel(MLP mlp, String filePath, boolean overwrite) throws IOException {
        File file = new File(filePath);
        if (file.exists() && !overwrite) {
            throw new IOException("File already exists and overwrite is not allowed.");
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(mlp);
        out.close();
        System.out.println("Model saved to file: " + filePath);
    }
 
    public static MLP loadModel(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
        MLP mlp = (MLP) in.readObject();
        in.close();
        System.out.println("Model loaded from file: " + filePath);
        return mlp;
    }


}
