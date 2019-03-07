/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class KNNClassifier {

    public int K = 25;
    public TrainDigits train;

    public File test_file;
    int[][] label = new int[TrainDigits.WIDTH][TrainDigits.HEIGHT];
    
    public void setLabel(int[][] label){
        this.label = label;
    }
    //generate image pixel intensity from image file
    public void generateIntensityFromImage() throws Exception {
        BufferedImage train_image = new BufferedImage(TrainDigits.WIDTH, TrainDigits.HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        train_image = ImageIO.read(test_file);

        for (int r = 0; r < TrainDigits.HEIGHT; r++) {
            for (int c = 0; c < TrainDigits.WIDTH; c++) {
                int pixel_val = train_image.getRGB(r, c);
                int r_val = (pixel_val >> 16) & 0xff;
                int g_val = (pixel_val >> 8) & 0xff;
                int b_val = (pixel_val) & 0xff;
                
                //int intensity = (int)(0.299*r_val + 0.587*g_val + 0.114*b_val);
                int intensity = (int)(0.2125*r_val + 0.7154*g_val + 0.0721*b_val);
                //int intensity = (int)((r_val + g_val + b_val) / 3);
                label[c][r] = intensity;
            }
        }
    }
    
    /**
     * recognition digit
     * @return 
     */
    public String KNN() {
        Map<String, Double> distance_list = new LinkedHashMap<String, Double>();
        int counter = 0;
        for (int[][] train_item : train.train_images) {
            double distance = getEuclidean(label, train_item);
            String key = String.valueOf(train.train_labels[counter]) + ":" + String.valueOf(counter);
            distance_list.put(key, distance);
            counter++;
        }
        //sort distance
        distance_list = sortByValue(distance_list, true);
        counter = 0;
        List<Integer> closet_list = new ArrayList<Integer>();
        for (String key : distance_list.keySet()) {
            if(counter == K) break;
            String digit_Str = key.split(":")[0];
            int digit = Integer.valueOf(digit_Str);
            closet_list.add(digit);
            counter++;
        }
        //get max label
        String rst_str = getMaxLabel(closet_list);
        return rst_str;
    }
    /**
     * get Max label
     * @param closet_list
     * @return 
     */
    private String getMaxLabel(List<Integer> closet_list){
        Map<Integer, Integer> frequency = new LinkedHashMap<Integer, Integer>();
        for(int i = 0; i < 10; i++){
            frequency.put(i, 0);
        }
        for(int item : closet_list){
            if(frequency.containsKey(item)){
                int value = frequency.get(item);
                value++;
                frequency.put(item, value);
            }
        }
        //get result value that has maximum labeled counter
        int max_counter = 0;
        int max_value = 0;
        for(int key : frequency.keySet()){
            int value = frequency.get(key);
            if(max_counter < value){
                max_counter = value;
                max_value = key;
            }
        }
        return String.valueOf(max_value);
    }
    //get Euclidean distance
    private double getEuclidean(int[][] test_label, int[][] train_label) {
        double rst = 0.0;
        for (int r = 0; r < TrainDigits.HEIGHT; r++) {
            for (int c = 0; c < TrainDigits.WIDTH; c++) {
                rst += Math.pow((train_label[r][c] - test_label[r][c]), 2);
            }
        }
        rst = Math.sqrt(rst);
        return rst;
    }

    private Map<String, Double> sortByValue(Map<String, Double> unsortMap, final boolean order) {
        List<Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

}
