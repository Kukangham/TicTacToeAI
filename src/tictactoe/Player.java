package tictactoe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Player {
    private final Game game;
    private final char value;
    static ArrayList<Point> blockCoordinates = new ArrayList<>();
    static ArrayList<Point> winCoordinates = new ArrayList<>();

    Player(Game game, char value) {
        this.game = game;
        this.value = value;
    }

    Game getGame() {
        return this.game;
    }

    char getValue() {
        return this.value;
    }

    int createRandomNumber() {
        Random random = new Random();
        return random.nextInt(3);
    }

    // This function returns true if there are moves
    // remaining on the board. It returns false if
    // there are no moves left to play.
    boolean isMovesLeft(char[][] field) {
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                if (Objects.equals(field[r][c], BoardValue.EMPTY.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Returns a value based on who is winning
    // field[3][3] is the Tic-Tac-Toe board
    int evaluate() {
        // Checking for rows for X or O victory
        if (game.isHorizontalWin(game.getPlayerX())) {
            return 10;
        } else if (game.isHorizontalWin(game.getPlayerY())) {
            return -10;
        }

        // Checking for columns for X or O victory
        if (game.isVerticalWin(game.getPlayerX())) {
            return 10;
        } else if (game.isVerticalWin(game.getPlayerY())) {
            return -10;
        }

        // Checking for top left diagonal for X or O victory
        if (game.isTopLeftDiagonalWin(game.getPlayerX())) {
            return 10;
        } else if (game.isTopLeftDiagonalWin(game.getPlayerY())) {
            return -10;
        }

        // Checking for top left diagonal for X or O victory
        if (game.isTopRightDiagonalWin(game.getPlayerX())) {
            return 10;
        } else if (game.isTopRightDiagonalWin(game.getPlayerY())) {
            return -10;
        }

        // Else if none of them have won then return 0
        return 0;
    }

    // This is the minimax function. It considers all
    // the possible ways the game can go and returns
    // the value of the board
    int minimax(int depth, boolean isMax) {
        int score = evaluate();

        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == 10) {
            return score;
        }

        // If Minimizer has won the game
        // return his/her evaluated score
        if (score == -10) {
            return score;
        }

        // If there are no more moves and
        // no winner then it is a draw(tie)
        if (!isMovesLeft(game.getField())) {
            return 0;
        }

        // If maximizer's move else minimizer's move
        int best;
        if (isMax) {
            best = -1000;

            // Traverse all cells
            for (int r = 0; r < game.getField().length; r++) {
                for (int c = 0; c < game.getField()[r].length; c++) {
                    if (Objects.equals(game.getField()[r][c], BoardValue.EMPTY.getValue())) {
                        // Make the move
                        game.getField()[r][c] = game.getPlayerX().getValue();

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(depth + 1, false));

                        // Undo the move
                        game.getField()[r][c] = BoardValue.EMPTY.getValue();
                    }
                }
            }
        } else {
            best = 1000;

            // Traverse all cells
            for (int r = 0; r < game.getField().length; r++) {
                for (int c = 0; c < game.getField()[r].length; c++) {
                    if (Objects.equals(game.getField()[r][c], BoardValue.EMPTY.getValue())) {
                        // Make the move
                        game.getField()[r][c] = game.getPlayerY().getValue();

                        best = Math.min(best, minimax(depth + 1, true));

                        // Undo the move
                        game.getField()[r][c] = BoardValue.EMPTY.getValue();
                    }
                }
            }
        }
        return best;
    }

    void setTrigger(char enemyValue, char ownValue) {
        int enemyWinCondition = 0;
        int ownWinCondition = 0;
        // Loop over each existing row and if it detects a potential win for the opponent
        // it marks and thus blocks it, or if it detects a potential win for itself
        // it saves the first detected coordinates and returns
        for (int r = 0; r < game.getField().length; r++) {
            for (int c = 0; c < game.getField()[r].length; c++) {
                if (game.getField()[r][c] == enemyValue) {
                    enemyWinCondition++;
                }
                if (game.getField()[r][c] == ownValue) {
                    ownWinCondition++;
                }

                if (enemyWinCondition == 2 || ownWinCondition == 2) {
                    for (int i = 0; i < game.getTriggerField().length; i++) {
                        if (ownWinCondition == 2
                                && Objects.equals(game.getField()[r][i], BoardValue.EMPTY.getValue())) {
                            winCoordinates.add(new Point(r, i));
                            return;
                        }
                        if (Objects.equals(game.getField()[r][i], BoardValue.EMPTY.getValue())
                                && game.getTriggerField()[r][i] != 'T') {
                            game.getTriggerField()[r][i] = 'T';
                            blockCoordinates.add(new Point(r, i));
                            break;
                        }
                    }
                }
            }
            enemyWinCondition = 0;
            ownWinCondition = 0;
        }

        // Loop over each existing column and if it detects a potential win for the opponent
        // it marks and thus blocks it, or if it detects a potential win for itself
        // it saves the first detected coordinates and returns
        for (int c = 0; c < game.getField().length; c++) {
            for (int r = 0; r < game.getField()[c].length; r++) {
                if (game.getField()[r][c] == enemyValue) {
                    enemyWinCondition++;
                }
                if (game.getField()[r][c] == ownValue) {
                    ownWinCondition++;
                }

                if (enemyWinCondition == 2 || ownWinCondition == 2) {
                    for (int i = 0; i < game.getTriggerField().length; i++) {
                        if (ownWinCondition == 2
                                && Objects.equals(game.getField()[i][c], BoardValue.EMPTY.getValue())) {
                            winCoordinates.add(new Point(i, c));
                            return;
                        }
                        if (Objects.equals(game.getField()[i][c], BoardValue.EMPTY.getValue())
                                && game.getTriggerField()[i][c] != 'T') {
                            game.getTriggerField()[i][c] = 'T';
                            blockCoordinates.add(new Point(i, c));
                            break;
                        }
                    }
                }
            }
            enemyWinCondition = 0;
            ownWinCondition = 0;
        }

        // Loop over each existing cell of the upper-left diagonal and if it detects a potential win for the opponent
        // it marks and thus blocks it, or if it detects a potential win for itself
        // it saves the first detected coordinates and returns
        for (int d = 0; d < game.getField().length; d++) {
            if (game.getField()[d][d] == enemyValue) {
                enemyWinCondition++;
            }
            if (game.getField()[d][d] == ownValue) {
                ownWinCondition++;
            }

            if (enemyWinCondition == 2 || ownWinCondition == 2) {
                for (int i = 0; i < game.getTriggerField().length; i++) {
                    if (ownWinCondition == 2
                            && Objects.equals(game.getField()[i][i], BoardValue.EMPTY.getValue())) {
                        winCoordinates.add(new Point(i, i));
                        return;
                    }
                    if (Objects.equals(game.getField()[i][i], BoardValue.EMPTY.getValue())
                            && game.getTriggerField()[i][i] != 'T') {
                        game.getTriggerField()[i][i] = 'T';
                        blockCoordinates.add(new Point(i, i));
                        break;
                    }
                }
                break;
            }
        }

        enemyWinCondition = 0;
        ownWinCondition = 0;
        // Loop over each existing cell of the upper-right diagonal and if it detects a potential win for the opponent
        // it marks and thus blocks it, or if it detects a potential win for itself
        // it saves the first detected coordinates and returns
        for (int d = 0; d < game.getField().length; d++) {
            if (game.getField()[d][game.getField().length - 1 - d] == enemyValue) {
                enemyWinCondition++;
            }
            if (game.getField()[d][game.getField().length - 1 - d] == ownValue) {
                ownWinCondition++;
            }

            if (enemyWinCondition == 2) {
                for (int i = 0; i < game.getTriggerField().length; i++) {
                    if (ownWinCondition == 2
                            && Objects.equals(game.getField()[i][game.getField().length - 1 - i], BoardValue.EMPTY.getValue())) {
                        winCoordinates.add(new Point(i, i));
                    }
                    if (Objects.equals(game.getField()[i][game.getField().length - 1 - i], BoardValue.EMPTY.getValue())
                            && game.getTriggerField()[i][game.getField().length - 1 - i] != 'T') {
                        game.getTriggerField()[i][game.getTriggerField().length - 1 - i] = 'T';
                        blockCoordinates.add(new Point(i, game.getTriggerField().length - 1 - i));
                        break;
                    }
                }
                break;
            }
        }
    }

    Point getRandomCoordinate(ArrayList<Point> point) {
        return point.get(new Random().nextInt(point.size()));
    }

    void move() {

    }
}

class Human extends Player {
    Human(Game game, char value) {
        super(game, value);
    }

    @Override
    void move() {
        while (true) {
            System.out.println("Enter the coordinates:");
            String[] scanInput = getGame().getScanner().nextLine().split("\\s+");

            try {
                int x = Integer.parseInt(scanInput[0]) - 1, y = Integer.parseInt(scanInput[1]) - 1;
                if (getGame().getBoard().getValue(x, y, getGame().getField()) == '_') {
                    getGame().getBoard().setValue(x, y, getValue(), getGame().getField());
                    getGame().getBoard().drawLayOut(getGame().getField());
                    break;
                } else {
                    System.out.println("This cell is occupied! Choose another one!");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("You should enter numbers!");
                move();
            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                System.out.println("Coordinates should be from 1 to 3!");
                move();
            }
        }
    }
}

class AI extends Player {
    enum Level {EASY, MEDIUM, HARD}

    private final Level level;

    AI(Game game, char value, Level level) {
        super(game, value);
        this.level = level;
    }

    Level getLevel() {
        return this.level;
    }

    void printMoveWarning() {
        System.out.println("Making move level \"" + getLevel().name().toLowerCase()+ "\"");
    }
}

class EasyAI extends AI {
    EasyAI(Game game, char value) {
        super(game, value, Level.EASY);
    }

    @Override
    void move() {
        printMoveWarning();
        while (true) {
            int x = createRandomNumber(), y = createRandomNumber();
            if (getGame().getBoard().getValue(x, y, getGame().getField()) == '_') {
                getGame().getBoard().setValue(x, y, getValue(), getGame().getField());
                getGame().getBoard().drawLayOut(getGame().getField());
                break;
            }
        }
    }
}

class mediumAI extends AI {
    mediumAI(Game game, char value) {
        super(game, value, Level.MEDIUM);
    }

    @Override
    void move() {
        if (getGame().getCountMove() % 2 == 0) {
            setTrigger(getGame().getPlayerY().getValue(), getGame().getPlayerX().getValue());
        } else if (getGame().getCountMove() % 2 == 1) {
            setTrigger(getGame().getPlayerX().getValue(), getGame().getPlayerY().getValue());
        }

        if (!winCoordinates.isEmpty()) {
            Point win = getRandomCoordinate(winCoordinates);
            int x = (int) win.getX(); int y = (int) win.getY();
            getGame().getField()[x][y] = getValue();
            getGame().getBoard().drawLayOut(getGame().getField());
        } else if (blockCoordinates.isEmpty()) {
            while (true) {
                int x = createRandomNumber(), y = createRandomNumber();
                if (getGame().getBoard().getValue(x, y, getGame().getField()) == '_') {
                    getGame().getBoard().setValue(x, y, getValue(), getGame().getField());
                    getGame().getBoard().drawLayOut(getGame().getField());
                    break;
                }
            }
        } else {
            Point randomCoordinate = getRandomCoordinate(blockCoordinates);
            int x = (int) randomCoordinate.getX(); int y = (int) randomCoordinate.getY();
            getGame().getField()[x][y] = getValue();
            getGame().getBoard().drawLayOut(getGame().getField());
            getGame().resetTriggerField(getGame().getTriggerField());
            blockCoordinates.clear();
        }
    }
}

class hardAI extends AI {
    hardAI(Game game, char value) {
        super(game, value, Level.HARD);
    }

    // This will place the best possible
    // move for the first playerX and for the second playerY
    @Override
    void move() {
        printMoveWarning();
        int bestMoveRow = -1, bestMoveCol = -1;
        boolean foundABetterOption = true;

        if (getGame().getCountMove() % 2 == 0) {
            int bestVal = -1000;
            // Traverse all cells, evaluate minimax function for all empty cells
            for (int r = 0; r < getGame().getField().length; r++) {
                for (int c = 0; c < getGame().getField()[r].length; c++) {
                    if (Objects.equals(getGame().getField()[r][c], BoardValue.EMPTY.getValue())) {
                        getGame().getField()[r][c] = getGame().getPlayerX().getValue();

                        // compute evaluation function for this move
                        int moveVal = minimax(0, false);

                        // Undo the move
                        getGame().getField()[r][c] = BoardValue.EMPTY.getValue();

                        // If the value of the current move is
                        // more than the best value, then update
                        // bestVal
                        if (moveVal == -10 || moveVal == 10 || moveVal == -1000) {
                            foundABetterOption = false;
                        }
                        if (moveVal > bestVal) {
                            bestMoveRow = r;
                            bestMoveCol = c;
                            bestVal = moveVal;
                        }
                    }
                }
            }
            if (foundABetterOption) {
                while (true) {
                    int x = createRandomNumber(), y = createRandomNumber();
                    if (getGame().getBoard().getValue(x, y, getGame().getField()) == '_') {
                        getGame().getBoard().setValue(x, y, getValue(), getGame().getField());
                        getGame().getBoard().drawLayOut(getGame().getField());
                        break;
                    }
                }
            } else {
                getGame().getField()[bestMoveRow][bestMoveCol] = getGame().getPlayerX().getValue();
                getGame().getBoard().drawLayOut(getGame().getField());
            }
        } else if (getGame().getCountMove() % 2 == 1) {
            int bestVal = 1000;
            // Traverse all cells, evaluate minimax function for all empty cells
            for (int r = 0; r < getGame().getField().length; r++) {
                for (int c = 0; c < getGame().getField()[r].length; c++) {
                    if (Objects.equals(getGame().getField()[r][c], BoardValue.EMPTY.getValue())) {
                        getGame().getField()[r][c] = getGame().getPlayerY().getValue();

                        // compute evaluation function for this move
                        int moveVal = minimax(0, true);

                        // Undo the move
                        getGame().getField()[r][c] = BoardValue.EMPTY.getValue();

                        // If the value of the current move is
                        // more than the best value, then update
                        // bestVal
                        if (moveVal == 10 || moveVal == -10 || moveVal == 1000) {
                            foundABetterOption = false;
                        }
                        if (moveVal < bestVal) {
                            bestMoveRow = r;
                            bestMoveCol = c;
                            bestVal = moveVal;
                        }
                    }
                }
            }
            if (foundABetterOption) {
                while (true) {
                    int x = createRandomNumber(), y = createRandomNumber();
                    if (getGame().getBoard().getValue(x, y, getGame().getField()) == '_') {
                        getGame().getBoard().setValue(x, y, getValue(), getGame().getField());
                        getGame().getBoard().drawLayOut(getGame().getField());
                        break;
                    }
                }
            } else {
                getGame().getField()[bestMoveRow][bestMoveCol] = getGame().getPlayerY().getValue();
                getGame().getBoard().drawLayOut(getGame().getField());
            }
        }
    }
}

//    boolean isNumeric(String scanInput) {
//        ParsePosition pos = new ParsePosition(0);
//        NumberFormat.getInstance().parse(scanInput, pos);
//        return scanInput.length() == pos.getIndex();
//    }

//    void easyAIMove(Board board, Player player) {
//        int x = createRandomNumber(), y = createRandomNumber();
//        if (board.getValue(x, y, board.getBoard()) == '_') {
//            System.out.println("Making move level \"easy\"");
//            this.countMove++;
//            board.setValue(x, y, player, board.getBoard());
//            board.drawLayOut(board.getBoard());
//        } else {
//            easyAIMove(board, player);
//        }
//    }

//    void HumanMove(Board board, Input input, Scanner scanner, Player player) {
//        System.out.println("Enter the coordinates:");
//        String scanInput = scanner.nextLine().replaceAll("\\s+","");
//        if (input.isCoordinateValid(scanInput)) {
//            char[] charArray = scanInput.toCharArray();
//            int x = Character.getNumericValue(charArray[0]) - 1, y = Character.getNumericValue(charArray[1]) - 1;
//            if (board.getValue(x, y, board.getBoard()) == '_') {
//                this.countMove++;
//                board.setValue(x, y, player, board.getBoard());
//                board.drawLayOut(board.getBoard());
//            } else {
//                System.out.println("This cell is occupied! Choose another one!");
//                HumanMove(board,input, scanner, player);
//            }
//        } else {
//            HumanMove(board,input, scanner, player);
//        }
//    }