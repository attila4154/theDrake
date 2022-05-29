package com.example.thedrake.ui;

import com.example.thedrake.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.Collection;


public class BoardView extends GridPane {

    private TileView selected = null;
    private ValidMoves validMoves;
    private final MainPane mainPane;

    public BoardView(ValidMoves validMoves, MainPane mainPane) {
        this.validMoves = validMoves;
        this.mainPane = mainPane;
        PositionFactory positionFactory = mainPane.getGameState().board().positionFactory();

        ///adding all tiles to the board
        for (int y = 0; y < positionFactory.dimension(); y++){
            for (int x = 0; x < positionFactory.dimension(); x++) {
                BoardPos boardPos = positionFactory.pos(x,positionFactory.dimension()-y-1);
                add(new TileView(mainPane.getGameState().tileAt(boardPos), this, boardPos), x, y);
            }
        }
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    ///update troops at all tiles
    public void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(mainPane.getGameState().tileAt(tileView.getPosition()));
        }
    }

    public void selected(TileView tileView) {
        ///3 cases:
        ///none was selected
        if (selected == null) {
            selected = tileView;
            tileView.drawBorder();
            showMoves(validMoves.boardMoves(tileView.getPosition()));
        }
        ///different tile was selected
        else if (selected != null && selected != tileView) {
            selected.undrawBorder();
            selected = tileView;
            selected.drawBorder();
            clearMoves();
            showMoves(validMoves.boardMoves(tileView.getPosition()));
        }
        ///this tile was selected again
        else {
            clearMoves();
            selected.undrawBorder();
            selected = null;
        }
    }

    public void executeMove(Move move) {
        selected.undrawBorder();
        selected = null;
        clearMoves();
        mainPane.executeMove(move);
        validMoves = new ValidMoves(mainPane.getGameState());
        updateTiles();
    }

    public void showMoves(Collection<Move> moves) {
        for (Move move : moves) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    public void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private TileView tileViewAt(BoardPos position) {
        int index = (mainPane.getGameState().board().dimension() - 1 - position.j())
                * 4 + position.i();
        return (TileView) getChildren().get(index);
    }

    public MainPane getContext() {
        return mainPane;
    }

    public void setValidMoves(ValidMoves validMoves) {
        this.validMoves = validMoves;
    }
}