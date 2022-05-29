package com.example.thedrake.ui;

import com.example.thedrake.GameState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Controller {
    @FXML
    private Label endText;

    @FXML
    private Pane endPane;

    @FXML
    private void exitProgram(ActionEvent event) {
        event.consume();
        Platform.exit();
    }

    @FXML
    private void startDrake(ActionEvent event) {
        GameState gameState = TheDrakeApp.emptyGameState();
        MainPane pane = new MainPane(gameState);
        ((Node)event.getSource()).getScene().setRoot(pane);
    }

    @FXML
    public void startingScreen(ActionEvent event){
        try {
            ((Node)event.getSource()).getScene().setRoot(TheDrakeApp.getStartingScreen().load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///sets background if blue player wins
    public void setBackgroundBlue(){
        endPane.setStyle("-fx-background-color:" +
                "linear-gradient(#cacadc, #2d2da5)");
    }

    ///sets background if yellow player wins
    public void setBackgroundOrange(){
        endPane.setStyle("-fx-background-color:" +
                "linear-gradient(#f7b127, #dfd4c1)");
    }

    ///sets text for ending screen
    public void setText(String text){
        endText.setText(text);
    }
}
