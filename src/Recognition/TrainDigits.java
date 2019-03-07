/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recognition;

import java.io.File;
import java.util.List;


public class TrainDigits {

    public String TRAIN_LABEL = "Train/train-labels.idx1-ubyte";
    public String TRAIN_IMAGE = "Train/train-images.idx3-ubyte";

    public static int WIDTH = 28;
    public static int HEIGHT = 28;

    public int[] train_labels;
    public List<int[][]> train_images;
        
    /**
     * read train dataset and label of MNIST dataset
     * @return 
     */
    public boolean Train() {
        File train_set_file = new File(TRAIN_IMAGE);
        File train_set_label = new File(TRAIN_LABEL);
        if(!train_set_file.exists() || !train_set_label.exists()){
            return false;
        }
        train_labels = MnistReader.getLabels("Train/train-labels.idx1-ubyte");
        train_images = MnistReader.getImages("Train/train-images.idx3-ubyte");
        return true;
    }
}
