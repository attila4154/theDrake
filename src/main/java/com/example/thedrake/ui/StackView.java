package com.example.thedrake.ui;

import com.example.thedrake.PlayingSide;
import com.example.thedrake.Troop;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * stack view for each player
 * does everything through its context since it must 'communicate' with boardView
 */
public class StackView extends HBox {
    ///stack has max 7 troops (at the beginning of the game)
    private List<TroopView> stack = new ArrayList<>();
    private final MainPane context;
    private final PlayingSide playingSide;

    public StackView(List<Troop> stack, PlayingSide playingSide, MainPane context) {
        this.context = context;
        this.playingSide = playingSide;
        ///creating all troopViews from stack
        for (Troop troop : stack)
            this.stack.add(new TroopView(troop, playingSide, this));

        for (int i = 0; i < this.stack.size(); i++)
            getChildren().add(i, this.stack.get(i));

        updateBorder();
        setPrefHeight(120);
        setPrefWidth(770);
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    ///removes top troop from the stack
    public void removeFromTop() {
        getChildren().remove(0);
        stack.remove(0);
    }

    ///redraws border
    public void updateBorder(){
//        if (!context.getGameState().isPlacingFromStackStage()) {
//            setBorder(borderInactive);
//            return;
//        }
//        if (playingSide == PlayingSide.BLUE) {
//            if (context.getGameState().sideOnTurn() == PlayingSide.BLUE)
//                setBorder(borderBlueActive);
//            else setBorder(borderBlueInactive);
//        } else {
//            if (context.getGameState().sideOnTurn() == PlayingSide.ORANGE)
//                setBorder(borderOrangeActive);
//            else setBorder(borderOrangeInactive);
//        }
    }

    void selectTroop(TroopView toSelect) {
        context.selectFromStack(toSelect);
    }

    public MainPane getContext() {
        return context;
    }

    ///===========================================================================
    private static Border borderBlueActive = new Border(
            new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))
    );
    private static Border borderBlueInactive = new Border(
            new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, new BorderWidths(4))
    );
    private static Border borderOrangeActive = new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))
    );
    private static Border borderOrangeInactive = new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, new BorderWidths(4))
    );
    private static Border borderInactive = new Border(
            new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))
    );
}