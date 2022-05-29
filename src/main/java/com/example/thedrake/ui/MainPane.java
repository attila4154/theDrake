package com.example.thedrake.ui;

import com.example.thedrake.*;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class MainPane extends BorderPane {
    private GameState gameState;
    private final BorderPane borderPane;
    private final BoardView boardView;
    private final StackView stackViewTop;
    private final StackView stackViewBottom;
    private final CapturedView capturedViewBlue;
    private final CapturedView capturedViewOrange;
    private final ControlPanel controlPanel;
    private final String blueTurn = "-fx-background-color: linear-gradient( rgba(255,179,15,1) 0%, rgba(107,126,184,1) 40%, rgba(19,24,190,1) 100%)";
    private final String orangeTurn = "-fx-background-color: linear-gradient( rgba(255,179,15,1) 0%, rgba(245,213,102,1) 60%, rgba(19,24,190,1) 100%)";

    private TroopView selectedFromStack = null;

    public MainPane(GameState gameState) {
        this.gameState = gameState;
        this.borderPane = new BorderPane();
        this.boardView = new BoardView(new ValidMoves(gameState), this);
        this.stackViewBottom = new StackView(
                gameState.army(PlayingSide.BLUE).stack(), PlayingSide.BLUE, this);
        this.stackViewTop = new StackView(
                gameState.army(PlayingSide.ORANGE).stack(), PlayingSide.ORANGE, this);
        this.capturedViewBlue = new CapturedView(
                this, PlayingSide.BLUE
        );
        this.capturedViewOrange = new CapturedView(
                this, PlayingSide.ORANGE
        );
        this.controlPanel = new ControlPanel();
        /**
         * game field:
         * control panel
         *
         *         ORANGE stack
         *
         * c    board -------------- board    c
         * a    |                        |    a
         * p    |                        |    p
         * t    |                        |    t
         * u    |                        |    u
         * r    |                        |    r
         * e    |                        |    e
         * d    |                        |    d
         *      board -------------- board
         *
         *                  BLUE stack
         */
        setTop(controlPanel);
        borderPane.setCenter(boardView);
        borderPane.setBottom(stackViewBottom);
        borderPane.setLeft(capturedViewBlue);
        borderPane.setRight(capturedViewOrange);
        borderPane.setTop(stackViewTop);
        setBottom(borderPane);
        setPrefHeight(750);
        setMaxHeight(750);
        setMinHeight(750);
        setPrefWidth(770);
        updateBackground();
    }

    /**
     *  called when 1 of the players lost
     *  of if player has no turns left
     */
    public void drawEndingMenu(PlayingSide side) {
//        if (gameState.result() == GameResult.DRAW) {
//            EndMenu menu = new EndMenu();
//            try {
//                getScene().setRoot(menu.getFxml().load());
//                Controller controller = menu.getFxml().getController();
//                controller.setText("draw!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        if (gameState.result() == GameResult.VICTORY) {
            try {
                EndMenu menu = new EndMenu();
                getScene().setRoot(menu.getFxml().load());
                Controller controller = menu.getFxml().getController();
                controller.setText(side.name().toLowerCase() + " player won!");
                if (side == PlayingSide.BLUE) controller.setBackgroundBlue();
                if (side == PlayingSide.ORANGE) controller.setBackgroundOrange();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectFromStack(TroopView toSelect){
        ///no troop selected
        if (selectedFromStack == null) {
            selectedFromStack = toSelect;
            selectedFromStack.setBorder();
            if (gameState.armyOnTurn().stack().get(0) == toSelect.getTroop())
                boardView.showMoves(
                        new ValidMoves(gameState).movesFromStack()
                );
        }
        ///unselect current troop
        else if (selectedFromStack == toSelect) {
            boardView.clearMoves();
            selectedFromStack.unsetBorder();
            selectedFromStack = null;
        }
        ///select new troop, unselect old
        else {
            boardView.clearMoves();
            selectedFromStack.unsetBorder();
            selectedFromStack = toSelect;
            selectedFromStack.setBorder();
            if (gameState.armyOnTurn().stack().get(0) == toSelect.getTroop())
                boardView.showMoves(
                        new ValidMoves(gameState).movesFromStack()
                );
        }
    }

    ///moves selected troop from stack to given position
    public void moveFromStack(BoardPos position) {
        if (gameState.armyOnTurn().stack().get(0) == selectedFromStack.getTroop()) {
            gameState = gameState.placeFromStack(position);
            boardView.updateTiles();
            if (selectedFromStack.playingSide == PlayingSide.BLUE) {
                stackViewBottom.removeFromTop();
                setStyle(orangeTurn);
            }
            else {
                stackViewTop.removeFromTop();
                setStyle(blueTurn);
            }
//            stackViewTop.updateBorder();
//            stackViewBottom.updateBorder();
            boardView.clearMoves();
            if (noMovesAvail()) {
                System.out.println("yep");
                gameState = gameState.resign();
                drawEndingMenu(gameState.sideOnTurn());
            }
        }
        boardView.setValidMoves(new ValidMoves(gameState));
        selectedFromStack = null;
    }

    ///adds last captured troop to captureView
    public void addToCaptured(){
        int capturedSize = gameState.armyNotOnTurn().captured().size();
        Troop lastCaptured = gameState.armyNotOnTurn().captured().get(capturedSize-1);
        if (gameState.sideOnTurn() == PlayingSide.BLUE)
            capturedViewOrange.addTroop(lastCaptured, PlayingSide.BLUE);
        else
            capturedViewBlue.addTroop(lastCaptured, PlayingSide.ORANGE);
    }

    ///executes move, checks if current player has any moves available
    public void executeMove(Move move){
        setGameState(move.execute(getGameState()));
        if (gameState.result() != GameResult.IN_PLAY)
            drawEndingMenu(gameState.sideNotOnTurn());
        if (move instanceof CaptureOnly || move instanceof StepAndCapture)
            addToCaptured();
        updateBackground();
    }

    private void updateBackground() {
        if (gameState.sideOnTurn() == PlayingSide.BLUE)
            setStyle(blueTurn);
        else
            setStyle(orangeTurn);
    }

    boolean noMovesAvail(){
        if (gameState.armyOnTurn().stack().isEmpty()) return false;
        for (int i = 0; i < gameState.board().positionFactory().dimension(); i++)
            for (int j = 0; j < gameState.board().positionFactory().dimension(); j++)
                if (gameState.canPlaceFromStack(gameState.board().positionFactory().pos(i,j))){
                    System.out.println("avail at " + i + "," + j);
                    return false;
                }

        return true;
    }

    void setSelectedFromStack(TroopView selected) {
        this.selectedFromStack = selected;
    }

    TroopView getSelectedFromStack() {
        return selectedFromStack;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public CapturedView getCapturedViewBlue() {
        return capturedViewBlue;
    }
    public CapturedView getCapturedViewOrange() {
        return capturedViewOrange;
    }
}
