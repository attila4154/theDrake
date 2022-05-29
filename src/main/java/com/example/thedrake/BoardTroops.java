package com.example.thedrake;

import java.io.PrintWriter;
import java.util.*;


/**
 * class that holds all the troops of one side (blue or orange)
 */
public class BoardTroops implements JSONSerializable{
	private final PlayingSide playingSide;
	///every troop(TroopTile) has position(BoardPos):
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	///number of guards:
	private final int guards;
	
	public BoardTroops(PlayingSide playingSide) {
		this.playingSide = playingSide;
		this.troopMap = Collections.EMPTY_MAP;
		this.leaderPosition = TilePos.OFF_BOARD;
		this.guards = 0;
	}
	
	public BoardTroops(
			PlayingSide playingSide,
			Map<BoardPos, TroopTile> troopMap,
			TilePos leaderPosition, 
			int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		if (troopMap.containsKey(pos)) {
			return Optional.of(troopMap.get(pos));
		}
		return Optional.empty();
	}

	public boolean hasTroopAdjacentTo(TilePos pos) {
		for (TilePos tilePos : troopMap.keySet()) {
			if (tilePos.isNextTo(pos)) return true;
		}
		return false;
	}
	
	public PlayingSide playingSide() {
		return playingSide;
	}
	
	public TilePos leaderPosition() {
		return leaderPosition;
	}

	public int guards() {
		return guards;
	}
	
	public boolean isLeaderPlaced() {
		return leaderPosition != TilePos.OFF_BOARD;
	}
	
	public boolean isPlacingGuards() {
		///2 guards are being placed right after placing leader,
		// leader being the first one to be placed
		if (!isLeaderPlaced()) return false;
		return guards < 2;
	}
	
	public Set<BoardPos> troopPositions() {
		return troopMap.keySet();
	}

	public Collection<TroopTile> troopSet() {
		return troopMap.values();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		if (at(target).isPresent()) throw new IllegalArgumentException();
		Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
		newMap.put(target, new TroopTile(troop, playingSide(), TroopFace.AVERS));
		if (!isLeaderPlaced()) {
			return new BoardTroops(playingSide(), newMap, target, 0);
		}
		else if (isPlacingGuards()){
			return new BoardTroops(playingSide(), newMap, leaderPosition(), guards+1);
		}
		return new BoardTroops(playingSide(), newMap, leaderPosition(), guards);
	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if (!isLeaderPlaced() || isPlacingGuards())
			throw new IllegalStateException("Cannot move troops before the leader or guards are placed");
		if (at(origin).isEmpty() || at(target).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
		newMap.put(target, newMap.get(origin).flipped());
		newMap.remove(origin);
		if (origin.equalsTo(leaderPosition().i(), leaderPosition().j())) {
			return new BoardTroops(playingSide(), newMap, target, guards());
		}
		return new BoardTroops(playingSide(), newMap, leaderPosition(), guards());
	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot flip troops before the leader is placed.");
		}

		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot flip troops before guards are placed.");
		}

		if(!at(origin).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot remove troop before the leader is placed.");
		}

		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot remove troop before guards are placed.");
		}

		if(!at(target).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		newTroops.remove(target);
		if (leaderPosition.equalsTo(target.i(), target.j())) {
			return new BoardTroops(playingSide(), newTroops, TilePos.OFF_BOARD, guards);
		}
		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{");
		writer.print("\"side\":");
		playingSide.toJSON(writer);
		writer.print(",\"leaderPosition\":");
		leaderPosition().toJSON(writer);
		writer.print(",\"guards\":");
		writer.print(guards);
		writer.print(",\"troopMap\":{");
		/**
		 * printing troop map:
		 * all troops must be sorted by their board position
		 */
		var ref = new Object() {
			boolean first = true;
		};
		troopMap.keySet().stream().sorted( (bp1, bp2) -> {
			if (bp1.column() == bp2.column()) {
				return Integer.compare(bp1.row(), bp2.row());
			}
			return Character.compare(bp1.column(), bp2.column());
		}).forEach(key -> {
				if (!ref.first)
					writer.print(",");
				else ref.first = false;
				key.toJSON(writer);
				writer.print(":");
				troopMap.get(key).toJSON(writer);
			}
		);
		writer.print("}}");
	}

	public Map<BoardPos, TroopTile> getTroopMap() {
		return troopMap;
	}
}
