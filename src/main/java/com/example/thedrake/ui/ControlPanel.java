package com.example.thedrake.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * control panel during game itself (2 buttons at the top)
 */
public class ControlPanel extends HBox {
    private final Button exitButton = new Button("Exit");
    private final Button resignButton = new Button ("Resign");

    public ControlPanel() {
        setSpacing(5);
        exitButton.getStyleClass().add("inGame");
        resignButton.getStyleClass().add("inGame");
        getChildren().add(exitButton);
        getChildren().add(resignButton);
        exitButton.setOnAction(actionEvent -> {
            try {
                exitButton.getScene().setRoot(TheDrakeApp.getStartingScreen().load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
