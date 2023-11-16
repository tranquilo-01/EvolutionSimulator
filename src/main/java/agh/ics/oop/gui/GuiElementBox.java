package agh.ics.oop.gui;

import agh.ics.oop.map.mapelement.IMapElement;
import agh.ics.oop.map.mapelement.animal.Animal;
import agh.ics.oop.map.mapelement.plant.Grass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;


public class GuiElementBox {
    private final VBox vbox;

    public GuiElementBox(IMapElement element) {

        vbox = new VBox();
        if (element instanceof Animal) {
            vbox.setBackground(
                    new Background(
                            new BackgroundFill(Paint.valueOf("blue"), CornerRadii.EMPTY, Insets.EMPTY)
                    ));
        } else if (element instanceof Grass) {
            vbox.setBackground(
                    new Background(
                            new BackgroundFill(Paint.valueOf("green"), CornerRadii.EMPTY, Insets.EMPTY)
                    ));
        } else {
            vbox.setBackground(
                    new Background(
                            new BackgroundFill(Paint.valueOf("black"), CornerRadii.EMPTY, Insets.EMPTY)
                    ));
        }
        vbox.setAlignment(Pos.CENTER);
    }

    public VBox getVbox() {
        return vbox;
    }
}
