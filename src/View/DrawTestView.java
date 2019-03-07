/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Recognition.KNNClassifier;
import Recognition.TrainDigits;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Star
 */
public class DrawTestView {

    public Stage testStage;
    public BorderPane main_pane = new BorderPane();
    public BorderPane photo_pane = new BorderPane();
    public GridPane contentPane = new GridPane();

    public Button test_btn;
    public Button clear_btn;
    public Button exit_btn;

    public TextField result_view = new TextField();
    public TextField pixel_view = new TextField();
    
    public Label pixel_label = new Label("Pixel Width: ");

    Canvas digit_drawing;
    GraphicsContext gc;

    public TrainDigits train;

    public int[][] label;
    
    private int init_pixel_width = 10;
    
    public void init() {
        pixel_label.setId("p_label");
        
        digit_drawing = new Canvas(196, 196);
        gc = digit_drawing.getGraphicsContext2D();
        initDraw(gc);
        
        digit_drawing.autosize();
        
        StackPane canvasContainer = new StackPane(digit_drawing);
        canvasContainer.setId("photo_view");
        canvasContainer.setMaxSize(196, 196);

        result_view.setId("p_label");
        result_view.setPromptText("Test Result");
        result_view.setEditable(false);
        result_view.setPrefWidth(200);
        
        pixel_view.setId("p_label");
        pixel_view.setText("10");
        pixel_view.setPromptText("Pixel Width");
        pixel_view.setPrefWidth(200);
        pixel_view.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (newValue == null) {
                    return;
                }
                if (newValue.matches("\\d*")) {
                    if(newValue.isEmpty()) return;
                    int pixel_width = Integer.valueOf(newValue);
                    if(pixel_width == 0) return;
                    gc.setLineWidth(pixel_width);
                } else {
                    pixel_view.setText(oldValue);
                }
            }
        });
        
        test_btn = new Button("Test");
        test_btn.setId("p_button");
        test_btn.setPrefWidth(200);

        clear_btn = new Button("Clear");
        clear_btn.setId("p_button");
        clear_btn.setPrefWidth(200);

        exit_btn = new Button("Exit");
        exit_btn.setId("p_button");
        exit_btn.setPrefWidth(200);

        VBox btn_group = new VBox();
        btn_group.setAlignment(Pos.CENTER);
        btn_group.setSpacing(15);
        btn_group.getChildren().addAll(result_view, test_btn, clear_btn, exit_btn);
        
        VBox pixel_box = new VBox();
        pixel_box.setSpacing(5);
        pixel_box.getChildren().addAll(pixel_label, pixel_view, canvasContainer);
        
        contentPane.setId("grid");
        contentPane.setPadding(new Insets(10));
        contentPane.setHgap(30);
        contentPane.setVgap(15);
        contentPane.setAlignment(Pos.TOP_CENTER);

        contentPane.add(btn_group, 0, 0, 1, 1);
        contentPane.add(pixel_box, 1, 0, 1, 1);
        //contentPane.add(canvasContainer, 1, 1, 1, 1);

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

    private void initDraw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.fill();
        gc.strokeRect(
                0, //x of the upper left corner
                0, //y of the upper left corner
                canvasWidth, //width of the rectangle
                canvasHeight);  //height of the rectangle
        
        //gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(init_pixel_width);
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    public void show() {
        testStage.showAndWait();
    }

    //Button action
    private void setAction() {

        test_btn.setOnAction((evt) -> {
            getLabelFromDraw();
            if (label == null) {
                return;
            }
            KNNClassifier knn = new KNNClassifier();
            knn.train = train;
            knn.setLabel(label);
            try {
                String rst = knn.KNN();
                result_view.setText(rst);
            } catch (Exception e) {

            }

        });
        clear_btn.setOnAction((evt) -> {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            result_view.clear();
        });
        exit_btn.setOnAction((evt) -> {
            testStage.close();
        });
        digit_drawing.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gc.beginPath();
                gc.moveTo(event.getX(), event.getY());
                gc.stroke();
            }
        });

        digit_drawing.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gc.lineTo(event.getX(), event.getY());
                gc.stroke();
            }
        });

        digit_drawing.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

            }
        });
    }

    public void getLabelFromDraw() {
        BufferedImage image = getScaledImage(digit_drawing);
        label = new int[TrainDigits.WIDTH][TrainDigits.HEIGHT];
        for (int r = 0; r < TrainDigits.HEIGHT; r++) {
            for (int c = 0; c < TrainDigits.WIDTH; c++) {
                int pixel_val = image.getRGB(r, c);
                int r_val = (pixel_val >> 16) & 0xff;
                int g_val = (pixel_val >> 8) & 0xff;
                int b_val = (pixel_val) & 0xff;
                
                r_val = 255 - r_val;
                g_val = 255 - g_val;
                b_val = 255 - b_val;
                //int intensity = (int)(0.299*r_val + 0.587*g_val + 0.114*b_val);
                //int intensity = (int)((r_val + g_val + b_val) / 3);
                int intensity = (int)(0.2125*r_val + 0.7154*g_val + 0.0721*b_val);
                label[c][r] = intensity;
            }
        }
        try {
            String label_text_file = "test.txt";
            PrintWriter writer = new PrintWriter(label_text_file, "UTF-8");
            for (int r = 0; r < TrainDigits.WIDTH; r++) {
                for (int c = 0; c < TrainDigits.HEIGHT; c++) {
                    writer.print(label[c][r]);
                }
                writer.println();
            }
            writer.close();
        } catch (Exception e) {

        }

    }

    public void disableBtns() {
        test_btn.setDisable(true);
        exit_btn.setDisable(true);
    }

    public void enableBtns() {
        test_btn.setDisable(false);
        exit_btn.setDisable(false);
    }

    private BufferedImage getScaledImage(Canvas canvas) {
        // for a better recognition we should improve this part of how we retrieve the image from the canvas
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);
        Image tmp = SwingFXUtils.fromFXImage(writableImage, null).getScaledInstance(TrainDigits.WIDTH, TrainDigits.HEIGHT, Image.SCALE_SMOOTH);
        BufferedImage scaledImg = new BufferedImage(TrainDigits.WIDTH, TrainDigits.HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics = scaledImg.getGraphics();
        graphics.drawImage(tmp, 0, 0, null);
        graphics.dispose();
        return scaledImg;
    }
}
