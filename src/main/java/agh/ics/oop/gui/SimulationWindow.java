package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Vector2d;
import agh.ics.oop.constants.MapVariant;
import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.map.IWorldMap;
import agh.ics.oop.map.RectangularMap;
import agh.ics.oop.map.TubularMap;
import agh.ics.oop.map.mapelement.IMapElement;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.Formatter;
import java.util.Map;

public class SimulationWindow extends Application implements IRenderer {
    private final IWorldMap map;
    private final SimulationEngine engine;
    private final Stage primaryStage = new Stage();
    private final int WINDOW_SIZE = 600;
    private final int cellSize;
    private final GridPane grid;
    private Label animalNumberValue;
    private Label grassNumberValue;
    private Label freeFieldsValue;
    private Label averageEnergyValue;
    private Label averageLifespanValue;
    private final int mapWidth;
    private final int mapHeight;
    private Thread thread;


    public SimulationWindow(Map<ParameterType, Object> params, String csvPath) {
        this.grid = new GridPane();
        MapVariant mapVariant = (MapVariant) params.get(ParameterType.MAP_VARIANT);
        IWorldMap map;
//                                         consider - czy to powinno dziać się w tej klasie?
        switch (mapVariant) {
            case TUBULAR -> map = new TubularMap(params);
            case HELL_PORTAL -> map = new RectangularMap(params);
            default -> map = new TubularMap(params);
        }
        this.mapHeight = (int) params.get(ParameterType.MAP_HEIGHT);
        this.mapWidth = (int) params.get(ParameterType.MAP_WIDTH);
        this.cellSize = (int) WINDOW_SIZE / (int) params.get(ParameterType.MAP_WIDTH);
        this.map = map;
        this.engine = new SimulationEngine(params, this.map, this, csvPath);
        renderMap();
        primaryStage.setOnCloseRequest(e -> engine.stop());
        primaryStage.show();
    }


    private void renderMap() {
        grid.getChildren().clear();


        for (int i = 0; i < mapWidth; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            grid.getRowConstraints().add(new RowConstraints(cellSize));

            for (int j = 0; j < mapHeight; j++) {
                VBox vbox = new VBox();
                vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("rgb(45, 30, 15)"), CornerRadii.EMPTY, Insets.EMPTY)));
                if (map.isOccupied(new Vector2d(i, j))) {
                    vbox = new GuiElementBox((IMapElement) map.objectAt(new Vector2d(i, j))).getVbox();
                }
                grid.add(vbox, i, j);
                GridPane.setHalignment(vbox, HPos.CENTER);
                GridPane.setValignment(vbox, VPos.CENTER);
            }
        }

        Label animalNumber = new Label("Animal number: ");
        animalNumberValue = new Label(Integer.toString(engine.dataGatherer.getAliveAnimalNumber()));
        HBox animalNumberBox = new HBox(animalNumber, animalNumberValue);

        Label grassNumber = new Label("Grass number: ");
        grassNumberValue = new Label(Integer.toString(engine.dataGatherer.getGrassNumber()));
        HBox grassNumberBox = new HBox(grassNumber, grassNumberValue);

        Label freeFields = new Label("Free fields: ");
        freeFieldsValue = new Label(Integer.toString(engine.dataGatherer.getFreeFieldsNumber()));
        HBox freeFieldsBox = new HBox(freeFields, freeFieldsValue);

        Label averageEnergy = new Label("Average animal energy: ");
        averageEnergyValue = new Label(Double.toString(engine.dataGatherer.getAverageAnimalEnergy()));
        HBox averageEnergyBox = new HBox(averageEnergy, averageEnergyValue);

        Label averageLifespan = new Label("Average lifespan: ");
        averageLifespanValue = new Label(Double.toString(engine.dataGatherer.getAverageLifespan()));
        HBox averageLifespanBox = new HBox(averageLifespan, averageLifespanValue);


        Button button = new Button("go");
        HBox hbox = new HBox(button);
        VBox vbox = new VBox(grid, hbox, animalNumberBox, grassNumberBox, freeFieldsBox, averageEnergyBox, averageLifespanBox);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);

        button.setOnAction(actionEvent -> {
            System.out.println("Starting engine");
            thread = new Thread(engine);
            thread.start();
            button.setDisable(true);
        });
    }

    public void requestMapRendering() {
        Platform.runLater(() -> {
            updateMap();
            updateStats();
        });
    }

    public void updateMap() {
        grid.getChildren().clear();


        for (int i = 0; i < mapWidth; i++) {

            for (int j = 0; j < mapHeight; j++) {
                VBox vbox = new VBox();
                vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("rgb(45, 30, 15)"), CornerRadii.EMPTY, Insets.EMPTY)));
                if (map.isOccupied(new Vector2d(i, j))) {
                    vbox = new GuiElementBox((IMapElement) map.objectAt(new Vector2d(i, j))).getVbox();
                }
                grid.add(vbox, i, j);
                GridPane.setHalignment(vbox, HPos.CENTER);
                GridPane.setValignment(vbox, VPos.CENTER);
            }
        }
    }

    public void updateStats() {
        Formatter energy = new Formatter();
        double avgEnergy = engine.dataGatherer.getAverageAnimalEnergy();
        energy.format("%.2f", avgEnergy);

        Formatter lifespan = new Formatter();
        double avgLifespan = engine.dataGatherer.getAverageLifespan();
        lifespan.format("%.2f", avgLifespan);


        animalNumberValue.setText(Integer.toString(engine.dataGatherer.getAliveAnimalNumber()));
        grassNumberValue.setText(Integer.toString(engine.dataGatherer.getGrassNumber()));
        freeFieldsValue.setText(Integer.toString(engine.dataGatherer.getFreeFieldsNumber()));
        averageEnergyValue.setText(energy.toString());
        averageLifespanValue.setText(lifespan.toString());

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Simulation window started");
    }

    @Override
    public void stop(){
        System.out.println("stop called");
        engine.stop();
    }
}

