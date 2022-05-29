package com.example.thedrake.ui;

import com.example.thedrake.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(getStartingScreen().load());
        scene.getStylesheets().add(getClass().getResource(
                "application.css"
        ).toExternalForm());
        Image icon = new Image("https://cdn-icons-png.flaticon.com/512/1037/1037970.png");
        stage.getIcons().add(icon);
        stage.setTitle("The Drake");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static FXMLLoader getStartingScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(TheDrakeApp.class.getResource("main-window.fxml"));
        return fxmlLoader;
    }

    public static GameState emptyGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1,1), BoardTile.MOUNTAIN),
                                new Board.TileAt(positionFactory.pos(0,1), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }

    public static GameState sampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        return new StandardDrakeSetup().startState(board).
                placeFromStack(positionFactory.pos(0,0)).
                placeFromStack(positionFactory.pos(3,3)).
                placeFromStack(positionFactory.pos(1,0)).
                placeFromStack(positionFactory.pos(3,2)).
                placeFromStack(positionFactory.pos(0,1)).
                placeFromStack(positionFactory.pos(2,3)).
                placeFromStack(positionFactory.pos(2,0)).
                placeFromStack(positionFactory.pos(1,3)).
                placeFromStack(positionFactory.pos(3,0)).
                placeFromStack(positionFactory.pos(0,3)).
                placeFromStack(positionFactory.pos(2,1)).
                placeFromStack(positionFactory.pos(0,2));

    }
}