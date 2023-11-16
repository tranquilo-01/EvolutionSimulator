package agh.ics.oop.gui;

import agh.ics.oop.constants.ConfigLoader;
import agh.ics.oop.constants.ParameterType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.Map;


public class Menu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Stage
        Stage menuWindow = new Stage();
        menuWindow.setTitle("Menu");

        Label filePathLabel = new Label("Provide config file path, otherwise default config will be loaded");
        TextField filePathTxt = new TextField();

        Label csvFilePathLabel = new Label("Provide path to directory where csv file will be created, otherwise default path will be used");
        TextField csvPathTxt = new TextField();

        Button startButton = new Button("Start simulation");

        VBox vbox = new VBox(filePathLabel, filePathTxt, csvFilePathLabel, csvPathTxt, startButton);

        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15);

        menuWindow.setScene(new Scene(vbox));
        menuWindow.setHeight(300);
        menuWindow.setWidth(600);
        menuWindow.show();


        startButton.setOnAction(actionEvent -> {
            ConfigLoader configLoader = new ConfigLoader(filePathTxt.getText());
            try {
                Map<ParameterType, Object> params = ConfigLoader.getParams();
                SimulationWindow simulation = new SimulationWindow(params, csvPathTxt.getText());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


    }

}
