package com.example.thedrake.ui;

import com.example.thedrake.PlayingSide;
import com.example.thedrake.Troop;
import com.example.thedrake.TroopFace;
import javafx.scene.layout.Pane;

public class TroopViewCaptured extends Pane {
    private final Troop troop;
    private final static TileBackgrounds tileBackgrounds = new TileBackgrounds();

    public TroopViewCaptured(Troop troop, PlayingSide playingSide) {
        this.troop = troop;

        setPrefSize(100,100);
        setMaxSize(100, 100);
        setBackground(tileBackgrounds.getTroop(troop, playingSide, TroopFace.AVERS));
    }
}
