package com.example.thedrake.ui;

import com.example.thedrake.PlayingSide;
import com.example.thedrake.Troop;
import com.example.thedrake.TroopFace;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

///represents troop tile in the stack
public class TroopView extends Pane {
    private final Troop troop;
    public PlayingSide playingSide;
    private static final TileBackgrounds tileBackgrounds = new TileBackgrounds();
    private final StackView context;
    private static Border border = new Border(
            new BorderStroke(
                    Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)
            ));

    public TroopView(Troop troop, PlayingSide playingSide, StackView context) {
        this.troop = troop;
        this.playingSide = playingSide;
        this.context = context;

        setPrefSize(100,100);
        setMaxSize(100, 100);
        setBackground(tileBackgrounds.getTroop(troop, this.playingSide, TroopFace.AVERS));

        setOnMouseClicked(e -> onClick());
    }

    ///only troop from top of the stack of the current player can be selected
    private void onClick() {
        if (context.getContext().getGameState().sideOnTurn() == playingSide &&
            context.getContext().getGameState().armyOnTurn().stack().get(0) == troop)
                context.selectTroop(this);
    }

    public void setBorder() {
        setBorder(border);
    }

    public void unsetBorder() {
        setBorder(null);
    }

    public Troop getTroop() {
        return troop;
    }
}
