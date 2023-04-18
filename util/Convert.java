package util;

import java.util.List;

/**
 *
 * @author Amirul_Hafiz
 */
public class Convert {
    
    public static Double[] toArrayDouble(double[] array){
        Double[] tempArray = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            tempArray[i] = (Double) array[i];
        }
        return tempArray;
    }
    
    public static double[] toArraydouble(Double[] array){
        double[] tempArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            tempArray[i] = (double) array[i];
        }
        return tempArray;
    }
    
    public double[][] convertToDoubleArray(List<String[]> data) {
    double[][] result = new double[data.size()][data.get(0).length];
    for (int i = 0; i < data.size(); i++) {
        String[] row = data.get(i);
        for (int j = 0; j < row.length; j++) {
            result[i][j] = Double.parseDouble(row[j]);
        }
    }
    return result;
    }
    
    public static int findMaxIndex(double[] arr) {
        int maxIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }


}
