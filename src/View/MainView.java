/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Recognition.TrainDigits;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Star
 */
public class MainView {
    public BorderPane main_pane = new BorderPane();
    public GridPane contentPane = new GridPane();
    
    public Label title;
    
    public Button train_btn;
    public Button test_image_btn;
    public Button test_draw_btn;
    public Button exit_btn;
    
    TrainDigits train = new TrainDigits();
    
    boolean train_state = false;
    
    public void init() {
        //initialize title label
        title = new Label("HandWritten Digit Recognition");
        title.setId("p_title");
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();
        VBox title_box = new VBox();
        title_box.setAlignment(Pos.CENTER);
        title_box.setSpacing(10);
        VBox.setMargin(title, new Insets(0, 0, 0, 0));
        title_box.getChildren().addAll(sep1, title, sep2);
        
        train_btn = new Button("Train");
        train_btn.setId("p_button");
        train_btn.setPrefWidth(350);
        
        test_image_btn = new Button("Test Image");
        test_image_btn.setId("p_button");
        test_image_btn.setPrefWidth(350);
        test_image_btn.setDisable(true);
        
        test_draw_btn = new Button("Test Draw");
        test_draw_btn.setId("p_button");
        test_draw_btn.setPrefWidth(350);
        test_draw_btn.setDisable(true);
        
        exit_btn = new Button("Exit");
        exit_btn.setId("p_button");
        exit_btn.setPrefWidth(350);
        
        VBox btn_group = new VBox();
        btn_group.setAlignment(Pos.CENTER);
        btn_group.setSpacing(15);
        btn_group.getChildren().addAll(train_btn, test_image_btn, test_draw_btn, exit_btn);

        contentPane.setId("grid");
        contentPane.setPadding(new Insets(10));
        contentPane.setHgap(30);
        contentPane.setVgap(15);
        contentPane.setAlignment(Pos.TOP_CENTER);

        contentPane.add(btn_group, 0, 0, 1, 1);
        
        BorderPane.setMargin(contentPane, new Insets(10, 10, 15, 15));
        main_pane.setTop(title_box);
        main_pane.setCenter(contentPane);
        
        setAction();
        
    }

    //Button action
    private void setAction() {
        train_btn.setOnAction((evt) -> {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    train_state = train.Train();
                    return null;
                }
            };
            task.setOnFailed(e -> {
                Throwable exception = e.getSource().getException();
                if (exception != null) {
                   train_btn.setText("Train");
                   enableBtns();
                }
            });
            task.setOnSucceeded(e -> {
                train_btn.setText("Train");
                enableBtns();
                if(train_state) train_btn.setDisable(true);
            });
            new Thread(task).start();
            train_btn.setText("Training...");
            disableBtns();
        });
        test_image_btn.setOnAction((evt) -> {
            ImageTestView test_view = new ImageTestView();
            test_view.train = train;
            test_view.init();
            test_view.show();
        });
        test_draw_btn.setOnAction((evt) -> {
            DrawTestView test_view = new DrawTestView();
            test_view.train = train;
            test_view.init();
            test_view.show();
        });
        exit_btn.setOnAction((evt) -> {
            System.exit(0);
        });
    }
    public void disableBtns(){
        train_btn.setDisable(true);
        test_image_btn.setDisable(true);
        test_draw_btn.setDisable(true);
        exit_btn.setDisable(true);
    }
    public void enableBtns(){
        train_btn.setDisable(false);
        test_image_btn.setDisable(false);
        test_draw_btn.setDisable(false);
        exit_btn.setDisable(false);
    }
}
