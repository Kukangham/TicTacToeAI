package tictactoe;

import java.util.Arrays;

public class Board {
    private BoardState boardState;

    Board() {
        this.boardState = BoardState.GAME_NOT_FINISHED;
    }

    BoardState getBoardState() {
        return this.boardState;
    }

    void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    char getValue(int x, int y, char[][] field) {
        return field[x][y];
    }

    void setValue(int x, int y, char value, char[][] field) {
        field[x][y] = value;
    }

    char[] getValues(char[][] field) {
        char[] boardValues = new char[9];
        int iterator = 0;
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                if (iterator < 9) {
                    boardValues[iterator] = field[r][c];
                    iterator++;
                }
            }
        }
        return boardValues;
    }

    void setValues(char[][] field, char[] values) {
        int iterator = 0;
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                if (iterator < 9) {
                    field[r][c] = values[iterator];
                    iterator++;
                }
            }
        }
    }

    char[] getRow(int rowNumber, char[][] field) {
        char[] rowValues = new char[field.length];
        for (int i = 0; i < field.length; i++) {
            rowValues[i] = getValue(i, rowNumber, field);
        }
        return rowValues;
    }

    char[] getCol(int colNumber, char[][] field) {
        char[] colValues = new char[field[0].length];
        for (int i = 0; i < field.length; i++) {
            colValues[i] = getValue(colNumber, i, field);
        }
        return colValues;
    }

    // In this particular method, I take into account that the diagonals from
    // the upper left corner to the lower right and from the lower right corner to the upper left corner are the same
    char[] getDiagonalTL(char[][] field) {
        char[] diagonalTL = new char[field.length];
        for (int i = 0; i < field.length; i++) {
            diagonalTL[i] = getValue(i, i, field);
        }
        return diagonalTL;
    }

    // In this particular method, I take into account that the diagonals from
    // the upper right corner to the lower left and from the lower left corner to the upper right corner are the same
    char[] getDiagonalTR(char[][] field) {
        char[] diagonalTR = new char[field.length];
        for (int i = 0; i < field.length; i++) {
            diagonalTR[i] = getValue(i, (field.length - 1) - i, field);
        }
        return diagonalTR;
    }

    void createField(char[][] field) {
        for (int r = 0; r < field.length; r++) {
            Arrays.fill(field[r], BoardValue.EMPTY.getValue());
        }
    }

    void drawLayOut(char[][] field) {
        System.out.println("-".repeat(9));
        for (int r = 0; r < field.length; r++) {
            System.out.print("| ");
            for (int c = 0; c < field[r].length; c++) {
                System.out.print(field[r][c] + " ");
            }
            System.out.println("|");
        }
        System.out.println("-".repeat(9));
    }
}

enum BoardState {
    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins");

    private final String message;

    BoardState(String message) {
        this.message = message;
    }

    String getMessage() {
        return this.message;
    }
}

enum BoardValue {
    X('X'),
    O('O'),
    EMPTY('_');

    private final char value;

    BoardValue(char value) {
        this.value = value;
    }

    char getValue() {
        return this.value;
    }
}