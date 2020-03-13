package com.udemy.ticTacToe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {

	private List<Cell> emptyCells;
	private Scanner scanner;
	private CellState[][] board;
	private List<Cell> rootValues;

	public Board() {
		initializeBoard();
	}

	private void initializeBoard() {
		this.rootValues = new ArrayList<>();
		this.scanner = new Scanner(System.in);
		this.board = new CellState[Constants.BORDER_SIZE][Constants.BORDER_SIZE];
	}

	public boolean isRunning() {
		if (isWinning(CellState.COMPUTER) || isWinning(CellState.USER) || getEmptyCells().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private List<Cell> getEmptyCells() {
		emptyCells = new ArrayList<>();

		for (int i = 0; i < Constants.BORDER_SIZE; i++) {
			for (int j = 0; j < Constants.BORDER_SIZE; j++) {
				if (this.board[i][j] == CellState.EMPTY) {
					emptyCells.add(new Cell(i, j));
				}
			}
		}
		return emptyCells;
	}

	public void makeUserInput() {
		System.out.println("User's input: ");
		int x = scanner.nextInt();
		int y = scanner.nextInt();
		Cell cell = new Cell(x, y);
		move(cell, CellState.USER);
	}

	public boolean isWinning(CellState player) {

		if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
			return true;
		}

		if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
			return true;
		}

		for (int i = 0; i < Constants.BORDER_SIZE; i++) {
			// check the rows
			if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
				return true;
			}

			// check the columns
			if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
				return true;
			}
		}
		return false;
	}

	public void move(Cell cell, CellState player) {
		this.board[cell.getX()][cell.getY()] = player;
	}

	public void displayBoard() {
		System.out.println();

		for (int i = 0; i < Constants.BORDER_SIZE; i++) {
			for (int j = 0; j < Constants.BORDER_SIZE; j++) {
				System.out.print(this.board[i][j] + " ");
			}
			System.out.println();
		}
	}

	public Cell getBestMove() {

		int max = Integer.MIN_VALUE;
		int best = Integer.MIN_VALUE;

		for (int i=0; i< rootValues.size(); i++) {
			if(max < rootValues.get(i).getMinimaxValue()) {
				max = rootValues.get(i).getMinimaxValue();
				best = i;
			}
		}
		return rootValues.get(best);
	}

	public void setupBoard() {
		for (int i = 0; i < Constants.BORDER_SIZE; i++) {
			for (int j = 0; j < Constants.BORDER_SIZE; j++) {
				board[i][j] = CellState.EMPTY;
			}
		}
	}

	public int returnMin(List<Integer> list) {
		int min = Integer.MAX_VALUE;
		int index = Integer.MIN_VALUE;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) < min) {
				min = list.get(i);
				index = i;
			}
		}
		return list.get(index);
	}

	public int returnMax(List<Integer> list) {
		int max = Integer.MIN_VALUE;
		int index = Integer.MIN_VALUE;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) > max) {
				max = list.get(i);
				index = i;
			}
		}
		return list.get(index);
	}

	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public List<Cell> getRootValues() {
		return rootValues;
	}

	public void setRootValues(List<Cell> rootValues) {
		this.rootValues = rootValues;
	}

	public void setEmptyCells(List<Cell> emptyCells) {
		this.emptyCells = emptyCells;
	}

	public void callMinimax(int depth, CellState player) {
		rootValues.clear();
		minimax(depth, player);
	}

	private int minimax(int depth, CellState player) {
		if (isWinning(CellState.COMPUTER))
			return 1;
		if (isWinning(CellState.USER))
			return -1;

		List<Cell> availableCells = emptyCells;
		if (availableCells.isEmpty())
			return 0;

		List<Integer> scores = new ArrayList<>();

		for (int i = 0; i < availableCells.size(); i++) {
			Cell point = availableCells.get(i);

			if (player == CellState.COMPUTER) {
				move(point, CellState.COMPUTER);
				int currentScore = minimax(depth + 1, CellState.USER);
				scores.add(currentScore);

				if (depth == 0) {
					point.setMinimaxValue(currentScore);
					rootValues.add(point);
				}
			} else if (player == CellState.USER) {
				move(point, CellState.USER);
				scores.add(minimax(depth + 1, CellState.COMPUTER));
			}

			board[point.getX()][point.getY()] = CellState.EMPTY;
		}

		if (player == CellState.COMPUTER) {
			return returnMax(scores);
		}
		return returnMin(scores);
	}
}
