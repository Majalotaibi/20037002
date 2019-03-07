/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author Star
 */
public class RecognitionDigit extends Application {
    
    public static Stage mainStage;
    
    MainView view = new MainView();
    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        
        view.init();
        Scene scene = new Scene(view.main_pane);
        scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        mainStage.setTitle("Digit Recognition");
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setResizable(false);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
