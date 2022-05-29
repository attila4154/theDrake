package com.example.thedrake;

import java.io.PrintWriter;
import java.util.Arrays;

public class Board implements JSONSerializable{
	private final int dimension;
	private final BoardTile[][] board;
	private final PositionFactory positionFactory;

	public Board(int dimension) {
		this.dimension = dimension;
		positionFactory = new PositionFactory(dimension);
		board = new BoardTile[dimension][dimension];
		for (int i = 0; i < board.length; i++) {
			Arrays.fill(board[i], BoardTile.EMPTY);
		}
	}

	public int dimension() {
		return dimension;
	}

	public BoardTile at(TilePos pos) {
		return board[pos.i()][pos.j()];
	}

	private Board copy() {
		Board newBoard = new Board(dimension);
		for (int i = 0; i < board.length; i++) {
			newBoard.board[i] = board[i].clone();
		}
		return newBoard;
	}

	public Board withTiles(TileAt ...ats) {
		Board newBoard = this.copy();
		for (int i = 0; i < ats.length; i++) {
			int posI = ats[i].pos.i();
			int posJ = ats[i].pos.j();
			newBoard.board[posI][posJ] = ats[i].tile;
		}
		return newBoard;
	}

	public PositionFactory positionFactory() {
		return positionFactory;
	}

	public static class TileAt {

		public final BoardPos pos;
		public final BoardTile tile;
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}

	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"dimension\":" + dimension);
		writer.print(",\"tiles\":[");
		boolean first = true;
		for (int col = 0; col < board[0].length; col++) {
			for (int row = 0; row < board.length; row++) {
				if (first) first = false;
				else writer.print(",");
				board[row][col].toJSON(writer);
			}
		}
		writer.print("]}");
	}
}

