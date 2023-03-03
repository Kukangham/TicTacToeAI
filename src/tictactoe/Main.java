package tictactoe;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        startTheGame(game);
    }

    private static void startTheGame(Game game) {
        game.startCommand(game);
        boolean isGameOver = false;
        while (!isGameOver) {
            switch (game.getGameState()) {
                case START_THE_GAME:
                    game.setGameState(Game.GameState.PREPARE_THE_FIELD);
                    break;
                case PREPARE_THE_FIELD:
                    game.setGameState(Game.GameState.MAKE_A_MOVE);
                    break;
                case INITIAL_FILL:
//                    game.setGameState(GameState.MAKE_A_MOVE);
                    break;
                case MAKE_A_MOVE:
                    break;
                case FINISH_THE_GAME:
                    isGameOver = true;
                    break;
            }
            game.processInput(game);
        }
        game.endCommand();
    }
}