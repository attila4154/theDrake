package com.example.thedrake.ui;

import com.example.thedrake.BoardPos;
import com.example.thedrake.Move;
import com.example.thedrake.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TileView extends Pane {

    ///TileView remembers which tile it has
    private Tile tile;
    private Move move = null;
    private final BoardView context;
    private final static TileBackgrounds tileBackgrounds = new TileBackgrounds();
    private final BoardPos boardPos;
    private final ImageView moveImage;
    private final static Border border = new Border(
            new BorderStroke(
                    Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)
            ));

    public TileView(Tile tile, BoardView context, BoardPos boardPos) {
        this.tile = tile;
        this.context = context;
        this.boardPos = boardPos;
        setPrefSize(100,100);
        update();

        setOnMouseClicked(e -> onClick());

        moveImage = new ImageView(new Image(getClass().getResourceAsStream("/assets/move.png")));
        moveImage.setVisible(false);
        getChildren().add(moveImage);
    }

    ///boardView updates all tiles using this method
    public void setTile(Tile tile) {
        this.tile = tile;
        update();
    }

    public BoardPos getPosition() {
        return boardPos;
    }

    private void update() {
        setBackground(tileBackgrounds.get(tile));
    }

    private void onClick() {
        ///if there is selected troop from stack

        if (context.getContext().getSelectedFromStack() != null) {
            if (context.getContext().getGameState().canPlaceFromStack(boardPos))
                context.getContext().moveFromStack(boardPos);
            return;
        }

        ///selected tile can make move at THIS tile
        if (move != null) {
            context.executeMove(move);
        }
        ///select tile if has troop on it
        else if (tile.hasTroop())
            select();
    }

    private void select() {
        setBorder(border);
        context.selected(this);
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);
    }

    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }

    public void drawBorder(){
        setBorder(border);
    }

    public void undrawBorder(){
        setBorder(null);
    }
}
