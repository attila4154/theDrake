package com.example.thedrake;

import java.io.PrintWriter;

public class GameState implements JSONSerializable{
	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy) {
		this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy, 
			PlayingSide sideOnTurn, 
			GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
	}

	public Board board() {
		return board;
	}
	
	public PlayingSide sideOnTurn() {
		return sideOnTurn;
	}

	public PlayingSide sideNotOnTurn() {
		return sideOnTurn == PlayingSide.BLUE ? PlayingSide.ORANGE : PlayingSide.BLUE;
	}
	
	public GameResult result() {
		return result;
	}
	
	public Army army(PlayingSide side) {
		if(side == PlayingSide.BLUE) {
			return blueArmy;
		}
		
		return orangeArmy;
	}

	public Army armyOnTurn() {
		return army(sideOnTurn);
	}
	
	public Army armyNotOnTurn() {
		if(sideOnTurn == PlayingSide.BLUE)
			return orangeArmy;
		
		return blueArmy;
	}

	public boolean isPlacingFromStackStage() {
		return armyOnTurn().stack().size() != 0;
	}
	
	public Tile tileAt(TilePos pos) {
		if (blueArmy.boardTroops().at(pos).isPresent()) {
			return blueArmy.boardTroops().at(pos).get();
		}
		if (orangeArmy.boardTroops().at(pos).isPresent()) {
			return orangeArmy.boardTroops().at(pos).get();
		}
		return board.at(pos);
	}

	/**
	 *
	 * @param origin
	 * @return true if possible to step from origin tile,
	 * false if game state is not IN_PLAY or if there is no troop on origin
	 * or if there is troop on it but it's turn of another player
	 *
	 */
	private boolean canStepFrom(TilePos origin) {
		if (result != GameResult.IN_PLAY) return false;
		if (isBeginningState()) return false;
		return (armyOnTurn().hasTroopAt(origin));
	}

	private boolean isBeginningState() {
		if (!armyOnTurn().boardTroops().isLeaderPlaced() ||
			armyOnTurn().boardTroops().isPlacingGuards()) return true;
		return false;
	}

	///true if some troop can step at given tile
	public boolean canStepTo(TilePos target) {
		if (result != GameResult.IN_PLAY ||
			target == BoardPos.OFF_BOARD) return false;
		///no troop can be stepped on
		if (blueArmy.hasTroopAt(target) ||
				orangeArmy.hasTroopAt(target)) {
			return false;
		}
		return board.at(target).canStepOn();
	}

	/**
	 * @param target
	 * @return true if notOnTurn player's troop can be captured
	 */
	private boolean canCaptureOn(TilePos target) {
		if (result != GameResult.IN_PLAY) return false;
		return (armyNotOnTurn().hasTroopAt(target));
	}
	
	public boolean canStep(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canStepTo(target);
	}
	
	public boolean canCapture(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canCaptureOn(target);
	}
	
	public boolean canPlaceFromStack(TilePos target) {
		if (result != GameResult.IN_PLAY ||  target == BoardPos.OFF_BOARD ||
				armyOnTurn().stack().isEmpty()  || !board.at(target).canStepOn() ||
				armyOnTurn().hasTroopAt(target) ||
				armyNotOnTurn().hasTroopAt(target)){
			return false;
		}
		/**
		 * 3 cases:
		 * 1. placing leader (leader can be placed only in the 1st row for blue player
		 * 2. placing guards
		 * 3. stredni hra
		 */
		if (!armyOnTurn().boardTroops().isLeaderPlaced()){
			return armyOnTurn().canPlaceLeader(target, board().dimension());
		}
		else if (armyOnTurn().boardTroops().isPlacingGuards()) {
			return armyOnTurn().canPlaceGuard(target);
		}
		return armyOnTurn().hasTroopAdjacentTo(target);
	}
	
	public GameState stepOnly(BoardPos origin, BoardPos target) {		
		if(canStep(origin, target))		 
			return createNewGameState(
					armyNotOnTurn(),
					armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
		
		throw new IllegalArgumentException();
	}
	
	public GameState stepAndCapture(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target), 
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState captureOnly(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState placeFromStack(BoardPos target) {
		if(canPlaceFromStack(target)) {
			return createNewGameState(
					armyNotOnTurn(),
					armyOnTurn().placeFromStack(target), 
					GameResult.IN_PLAY);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState resign() {
		return createNewGameState(
				armyNotOnTurn(), 
				armyOnTurn(), 
				GameResult.VICTORY);
	}
	
	public GameState draw() {
		return createNewGameState(
				armyOnTurn(), 
				armyNotOnTurn(), 
				GameResult.DRAW);
	}
	
	private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
		if(armyOnTurn.side() == PlayingSide.BLUE) {
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		}
		
		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result); 
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"result\":");
			result.toJSON(writer);
		writer.print(",\"board\":");
			board.toJSON(writer);
		writer.print(",\"blueArmy\":");
			blueArmy.toJSON(writer);
//		writer.print("}");
		writer.print(",\"orangeArmy\":");
			orangeArmy.toJSON(writer);
		writer.print("}");

	}
}
