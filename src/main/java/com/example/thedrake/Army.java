package com.example.thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Army implements JSONSerializable{
	private final BoardTroops boardTroops;
	private final List<Troop> stack;
	private final List<Troop> captured;
	
	public Army(PlayingSide playingSide, List<Troop> stack) {
		this(
				new BoardTroops(playingSide), 
				stack, 
				Collections.emptyList());
	}
	
	public Army( 
			BoardTroops boardTroops,
			List<Troop> stack, 
			List<Troop> captured) {
		this.boardTroops = boardTroops;
		this.stack = stack;
		this.captured = captured;
	}
	
	public PlayingSide side() {
		return boardTroops.playingSide();
	}
	
	public BoardTroops boardTroops() {
		return boardTroops;
	}
	
	public List<Troop> stack() {
		return stack;
	}
	
	public List<Troop> captured() {
		return captured;
	}

	public Collection<Move> moves(){
		List<Move> moves = new ArrayList<>();
//		for (var t : boardTroops.getTroopMap()){
//
//		}
		return Collections.emptyList();
	}

	///leader can be placed only in the 1st row for blue player and in the last row for orange
	public boolean canPlaceLeader(TilePos pos, int dimension) {
		if (side() == PlayingSide.BLUE && pos.row() == 1 ) return true;
		else if (side() == PlayingSide.ORANGE && pos.row() == dimension) return true;
		return false;
	}

	///guards can only be placed next to leader
	public boolean canPlaceGuard(TilePos target) {
		return target.isNextTo(boardTroops().leaderPosition());
	}

	public Army placeFromStack(BoardPos target) {
		if(target == TilePos.OFF_BOARD) 
			throw new IllegalArgumentException();
		
		if(stack.isEmpty())
			throw new IllegalStateException();
		
		if(boardTroops.at(target).isPresent())
			throw new IllegalStateException();

		List<Troop> newStack = new ArrayList<Troop>(
				stack.subList(1, stack.size()));
		
		return new Army(
				boardTroops.placeTroop(stack.get(0), target),
				newStack, 
				captured);
	}
	
	public Army troopStep(BoardPos origin, BoardPos target) {
		return new Army(boardTroops.troopStep(origin, target), stack, captured);
	}
	
	public Army troopFlip(BoardPos origin) {
		return new Army(boardTroops.troopFlip(origin), stack, captured);
	}
	
	public Army removeTroop(BoardPos target) {
		return new Army(boardTroops.removeTroop(target), stack, captured);
	}
	
	public Army capture(Troop troop) {
		List<Troop> newCaptured = new ArrayList<Troop>(captured);
		newCaptured.add(troop);
		
		return new Army(boardTroops, stack, newCaptured);
	}

	public boolean hasTroopAdjacentTo(TilePos pos) {
		return boardTroops.hasTroopAdjacentTo(pos);
	}

	public boolean hasTroopAt(TilePos pos) {
		return boardTroops.at(pos).isPresent();
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"boardTroops\":");
		boardTroops().toJSON(writer);
		writer.print(",\"stack\":[");
		boolean first = true;
		for (Troop t : stack) {
			if (!first) writer.print(",");
			else first = false;
			t.toJSON(writer);
		}
		writer.print("]");
		first = true;
		writer.print(",\"captured\":[");
		for (Troop t : captured) {
			if (!first) writer.print(",");
			else first = false;
			t.toJSON(writer);
		}
		writer.print("]}");
	}

}
