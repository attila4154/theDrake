package com.example.thedrake.ui;

import com.example.thedrake.PlayingSide;
import com.example.thedrake.Troop;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


/**
 * view of captured tiles by each player
 */
public class CapturedView extends VBox {
    private List<TroopViewCaptured> captured;
    private final PlayingSide side;
    private final MainPane context;

    public CapturedView(MainPane mainPane, PlayingSide playingSide) {
        this.context = mainPane;
        this.side = playingSide;
        captured = new ArrayList<>();
        setPrefHeight(450);
        setMaxHeight(450);
        setPrefWidth(120);
        setMaxWidth(120);
        setSpacing(10);
        if (playingSide == PlayingSide.BLUE)
            setAlignment(Pos.BOTTOM_CENTER);
        if (playingSide == PlayingSide.ORANGE)
            setAlignment(Pos.TOP_CENTER);
        getChildren().add(new Label("Captured"));
    }

    public void addTroop(Troop troop, PlayingSide playingSide) {
        TroopViewCaptured toCapture = new TroopViewCaptured(troop, playingSide);
        captured.add(toCapture);
        getChildren().add(toCapture);
    }
}