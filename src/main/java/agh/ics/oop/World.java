package agh.ics.oop;

import agh.ics.oop.gui.Menu;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {
        try {
            Application.launch(Menu.class, args);
        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal argument exception caught in World.main");
        }

    }
}
