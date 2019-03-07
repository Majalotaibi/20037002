/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Recognition.KNNClassifier;
import Recognition.TrainDigits;
import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Star
 */
public class ImageTestView {

    public Stage testStage;
    public BorderPane main_pane = new BorderPane();
    public BorderPane photo_pane = new BorderPane();
    public GridPane contentPane = new GridPane();

    public Button open_file_btn;
    public Button test_btn;
    public Button exit_btn;

    public TextField result_view = new TextField();

    ImageView digit_image;

    public TrainDigits train;

    public File test_image_file;

    public void init() {

        digit_image = new ImageView();
        
        //Image loading_img = new Image(getClass().getResource("loading.gif").toExternalForm());
        //digit_image.setImage(loading_img);
        digit_image.autosize();
        digit_image.setFitWidth(200);
        digit_image.setFitHeight(180);
        
        photo_pane.setPrefSize(200, 180);
        photo_pane.setId("photo_view");
        photo_pane.setPadding(Insets.EMPTY);
        BorderPane.setMargin(digit_image, Insets.EMPTY);
        photo_pane.setCenter(digit_image);
        
        result_view.setId("p_label");
        result_view.setPromptText("Test Result");
        result_view.setEditable(false);
        result_view.setPrefWidth(200);
        

        open_file_btn = new Button("Open Image");
        open_file_btn.setId("p_button");
        open_file_btn.setPrefWidth(200);
        open_file_btn.requestFocus();

        test_btn = new Button("Test");
        test_btn.setId("p_button");
        test_btn.setPrefWidth(200);
        test_btn.setDisable(true);

        exit_btn = new Button("Exit");
        exit_btn.setId("p_button");
        exit_btn.setPrefWidth(200);

        VBox btn_group = new VBox();
        btn_group.setAlignment(Pos.CENTER);
        btn_group.setSpacing(15);
        btn_group.getChildren().addAll(result_view, open_file_btn, test_btn, exit_btn);

        contentPane.setId("grid");
        contentPane.setPadding(new Insets(10));
        contentPane.setHgap(30);
        contentPane.setVgap(15);
        contentPane.setAlignment(Pos.TOP_CENTER);
        
        contentPane.add(btn_group, 0, 0, 1, 1);
        contentPane.add(photo_pane, 1, 0, 1, 1);
        

        BorderPane.setMargin(contentPane, new Insets(10, 10, 15, 15));
        main_pane.setCenter(contentPane);

        Scene scene = new Scene(main_pane);
        scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());

        testStage = new Stage();
        testStage.setScene(scene);
        testStage.setResizable(false);
        testStage.setTitle("Test Digit Image");

        testStage.initOwner(RecognitionDigit.mainStage);
        testStage.initModality(Modality.WINDOW_MODAL);

        setAction();
    }

    public void show() {
        testStage.showAndWait();
    }

    //Button action
    private void setAction() {
        open_file_btn.setOnAction((evt) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Digit Images", "*.png", "*.jpg")
            );
            File img_file = fileChooser.showOpenDialog(new Stage());
            if (img_file != null) {
                try {
                    test_image_file = img_file;
                    Image test_img = new Image(test_image_file.toURI().toURL().toExternalForm());
                    digit_image.setImage(test_img);
                    test_btn.setDisable(false);
                } catch (Exception e) {
                    
                }
            }
        });
        test_btn.setOnAction((evt) -> {
            if(test_image_file == null) return;
            KNNClassifier knn = new KNNClassifier();
            knn.train = train;
            knn.test_file = test_image_file;
            try{
                knn.generateIntensityFromImage();
                String rst = knn.KNN();
                result_view.setText(rst);
            }catch(Exception e){
                
            }
            
        });
        exit_btn.setOnAction((evt) -> {
            testStage.close();
        });
    }

    public void disableBtns() {
        open_file_btn.setDisable(true);
        test_btn.setDisable(true);
        exit_btn.setDisable(true);
    }

    public void enableBtns() {
        open_file_btn.setDisable(false);
        test_btn.setDisable(false);
        exit_btn.setDisable(false);
    }
}
