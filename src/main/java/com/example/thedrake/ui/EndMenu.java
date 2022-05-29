package com.example.thedrake.ui;

import com.example.thedrake.PlayingSide;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;


public class EndMenu {
    private FXMLLoader fxmlLoader;

    public EndMenu() {
        this.fxmlLoader = new FXMLLoader(TheDrakeApp.class.getResource("end-menu.fxml"));
    }

    public FXMLLoader getFxml(){
        return fxmlLoader;
    }
}
