package tictactoe;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    enum GameState {
        START_THE_GAME, PREPARE_THE_FIELD, INITIAL_FILL, MAKE_A_MOVE, FINISH_THE_GAME

    }
    private final Scanner scanner = new Scanner(System.in);
    private final Board board;
    private GameState gameState;
    private int counter = 0;
    private int countMove = 0;
    private final char[][] field = new char[3][3];
    private final char[][] triggerField = new char[3][3];

    private Player playerX;
    private Player playerY;

    Scanner getScanner() {
        return this.scanner;
    }

    Game() {
        this.board = new Board();
        this.gameState = GameState.START_THE_GAME;
    }

    Board getBoard() {
        return this.board;
    }

    Player getPlayerX() {
        return this.playerX;
    }

    Player getPlayerY() {
        return this.playerY;
    }

    void setPlayerX(Player playerX) {
        this.playerX = playerX;
    }

    void setPlayerY(Player playerY) {
        this.playerY = playerY;
    }

    GameState getGameState() {
        return this.gameState;
    }

    char[][] getField() {
        return this.field;
    }

    char[][] getTriggerField() {
        return this.triggerField;
    }

    int getCounter() {
        return this.counter;
    }

    void setCounter(int counter) {
        this.counter = counter;
    }

    int getCountMove() {
        return this.countMove;
    }

    void setCountMove(int countMove) {
        this.countMove = countMove;
    }

    void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    void setPlayerType(Game game, Player player, String strType) {
        if (player == getPlayerX()) {
            if ("EASY".equals(strType)) {
                player = new EasyAI(game, BoardValue.X.getValue());
                this.setPlayerX(player);
            } else if ("MEDIUM".equals(strType)) {
                player = new mediumAI(game, BoardValue.X.getValue());
                this.setPlayerX(player);
            } else if ("HARD".equals(strType)) {
                player = new hardAI(game, BoardValue.X.getValue());
                this.setPlayerX(player);
            } else if ("USER".equals(strType)) {
                player = new Human(game, BoardValue.X.getValue());
                this.setPlayerX(player);
            } else {
                throw new NoSuchElementException("Bad parameters!");
            }
        } else if (player == getPlayerY()) {
            if ("EASY".equals(strType)) {
                player = new EasyAI(game, BoardValue.O.getValue());
                this.setPlayerY(player);
            } else if ("MEDIUM".equals(strType)) {
                player = new mediumAI(game, BoardValue.O.getValue());
                this.setPlayerY(player);
            } else if ("HARD".equals(strType)) {
                player = new hardAI(game, BoardValue.O.getValue());
                this.setPlayerY(player);
            } else if ("USER".equals(strType)) {
                player = new Human(game, BoardValue.O.getValue());
                this.setPlayerY(player);
            } else {
                throw new NoSuchElementException("Bad parameters!");
            }
        }

    }

    void startCommand(Game game) {
        while(true) {
            System.out.println("Input command:");
            String[] command = this.scanner.nextLine().toUpperCase().split("\\s+");

            if ("EXIT".equals(command[0])) {
                System.exit(0);
            } else if ("START".equals(command[0])) {
                try {
                    setPlayerType(game, getPlayerX(), command[1]);
                    setPlayerType(game, getPlayerY(), command[2]);
                    break;
                } catch (NoSuchElementException | ArrayIndexOutOfBoundsException exceptions) {
                    System.out.println("Bad parameters!");
                }
            } else {
                System.out.println("Bad parameters!");
            }
        }
    }

    void setWhoseTurn() {
        int countX = 0, countY = 0;
        char[] boardValues = this.board.getValues(this.field);
        for (int i = 0; i < boardValues.length; i++) {
            if (boardValues[i] == 'X') {
                countX++;
            } else if (boardValues[i] == 'O') {
                countY++;
            }
        }
        this.countMove = countX + countY;
    }

    boolean isInBoardValueEnum(char[] charInputs) {
        BoardValue[] boardValues = BoardValue.values();
        boolean isInEnum = false;
        for (char inputChars : charInputs) {
            for (BoardValue boardValue: boardValues) {
                if (boardValue.getValue() == inputChars) {
                    isInEnum = true;
                    break;
                } else {
                    isInEnum = false;
                }
            }
            if (!isInEnum) {
                return false;
            }
        }
        return isInEnum;
    }

    void initialFill(Game game) {
        String scanInput = this.scanner.nextLine();
        char[] initialInput = new char[scanInput.length()];
        for (int i = 0; i < scanInput.length(); i++) {
            initialInput[i] = scanInput.charAt(i);
        }

        if (!isInBoardValueEnum(initialInput) || initialInput.length > 9) {
            this.gameState = GameState.INITIAL_FILL;
            processInput(game);
        } else {
            try {
                this.board.setValues(this.field, initialInput);
                setWhoseTurn();
                this.board.drawLayOut(this.field);
            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                this.gameState = GameState.INITIAL_FILL;
                processInput(game);
            }
        }
    }

    // Whose move depends on who made the move first, and if the method returns 0, then
    // it means that the player who made the first move must make his move if otherwise it returns 1.
    int whoseMove(int countMove) {
        int turn = countMove % 2;
        if (turn == 0) {
            return 0;
        }
        return 1;
    }

    boolean isTheGameFinished(Board board) {
        if (this.countMove >= 9) {
            board.setBoardState(BoardState.DRAW);
            return true;
        }
        System.out.println(BoardState.GAME_NOT_FINISHED.getMessage());
        return false;
    }

    void processInput(Game game) {
        switch (gameState) {
            case PREPARE_THE_FIELD -> {
                this.board.createField(this.field);
                this.board.drawLayOut(this.field);
            }
            case INITIAL_FILL -> {
                System.out.println("Enter the cells:");
                initialFill(game);
            }
            case MAKE_A_MOVE -> {
                boolean isGameFinished = false;
                while(!isGameFinished) {
                    switch(whoseMove(countMove)) {
                        case 0 -> {
                            playerX.move();
                            this.countMove++;
                            if (hasPlayerWon(playerX)) {
                                board.setBoardState(BoardState.X_WINS);
                                isGameFinished = true;
                                break;
                            }

                            if (isTheGameFinished(board)) isGameFinished = true;
                        }
                        case 1 -> {
                            playerY.move();
                            this.countMove++;
                            if (hasPlayerWon(playerY)) {
                                board.setBoardState(BoardState.O_WINS);
                                isGameFinished = true;
                                break;
                            }

                            if (isTheGameFinished(board)) isGameFinished = true;
                        }
                    }
                }
                this.gameState = GameState.FINISH_THE_GAME;
            }
            case FINISH_THE_GAME -> {
                System.out.println(board.getBoardState().getMessage());
            }
        }
    }

    boolean checkCounter(int counter) {
        return counter == 3;
    }

    void resetCounts() {
        this.counter = 0;
    }

    boolean hasPlayerWon(Player player) {
        return isTopLeftDiagonalWin(player) || isTopRightDiagonalWin(player) ||
                isHorizontalWin(player) || isVerticalWin(player);
    }

    boolean isHorizontalWin(Player player) {
        // All existing horizontal rows
        for (int y = 0; y < this.field.length; y++) {
            resetCounts();
            char[] rowArray = this.board.getRow(y, this.field);
            // Loop over all x-coordinates
            for (int r = 0; r < this.field[y].length; r++) {
                if (Objects.equals(rowArray[r], player.getValue())) {
                    this.counter++;
                }
            }
            if (checkCounter(this.counter)) {
                return true;
            }
        }
        return false;
    }

    boolean isVerticalWin(Player player) {
        // All existing vertical columns
        for (int x = 0; x < this.field.length; x++) {
            resetCounts();
            char[] columnArray = this.board.getCol(x, this.field);
            // Loop over all y-coordinates
            for (int y = 0; y < this.field[x].length; y++) {
                if (Objects.equals(columnArray[y], player.getValue())) {
                    this.counter++;
                }
            }
            if (checkCounter(this.counter)) {
                return true;
            }
        }
        return false;
    }

    boolean isTopLeftDiagonalWin(Player player) {
        resetCounts();
        // Loop over all diagonal cells
        char[] diagonalArray = this.board.getDiagonalTL(this.field);
        for (int d = 0; d < this.field.length; d++) {
            if (Objects.equals(diagonalArray[d], player.getValue())) {
                this.counter++;
            }
        }
        return checkCounter(this.counter);
    }

    boolean isTopRightDiagonalWin(Player player) {
        resetCounts();
        // Loop over all diagonal cells
        char[] diagonalArray = this.board.getDiagonalTR(this.field);
        for (int d = 0; d < this.field.length; d++) {
            if (Objects.equals(diagonalArray[d], player.getValue())) {
                this.counter++;
            }
        }
        return checkCounter(this.counter);
    }

    void resetTriggerField(char[][] field) {
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                if (field[r][c] == 'T') {
                    field[r][c] = BoardValue.EMPTY.getValue();
                }
            }
        }
    }

    void endCommand() {
        while(true) {
            System.out.println("Input command:");
            String command = this.scanner.nextLine().toLowerCase();

            if ("exit".equals(command)) {
                break;
            } else {
                System.out.println("Bad parameters!");
            }
        }
    }
}
