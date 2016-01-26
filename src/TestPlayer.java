import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: Jan 24, 2016
 * Author: Yang Liu
 * WPIid:yliu17
 */


public class TestPlayer implements ITestPlayer {

    private Config config;

    private int playerNum;
    private State state;

    private final String PLAYER_NAME = "yliu17";

    public static final int PLAYER_ME = 0;
    public static final int PLAYER_OPPONENT = 1;

    private final int POSITIVE_INFINITY = 99999;
    private final int NEGATIVE_INFINITY = -99999;

    private long startTime;
    private final boolean DEBUG = false;

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void processInput() throws IOException {

        String s = input.readLine();
        List<String> ls = Arrays.asList(s.split(" "));
        if (ls.size() == 2) {
            readMove(ls);
            writeMove();
        } else if (ls.size() == 1) {
            // Game is over when receive -playerNum
            if (Integer.parseInt(ls.get(0)) == -playerNum)
                gameOver();
        } else if (ls.size() == 5) {
            // Initialize player
            init(ls);
        } else if (ls.size() == 4) {
            // Decide who is the first player
            if (ls.get(1).equals(PLAYER_NAME)) setPlayerNum(1);
            else setPlayerNum(2);

        } else
            System.out.println("not what I want");
    }

    @Override
    public void init(List<String> ls) {
        readConfig(ls);
        this.state = new State(config.getBoardWidth(), config.getBoardHeight(), config.getNumPieceWin());
        if (config.getTurnOfPlayer() == playerNum) writeMove();
    }

    @Override
    public void sendName() {
        System.out.print(PLAYER_NAME);
    }

    @Override
    public void readConfig(List<String> ls) {
        setConfig(new Config(
                Integer.parseInt(ls.get(0)),
                Integer.parseInt(ls.get(1)),
                Integer.parseInt(ls.get(2)),
                Integer.parseInt(ls.get(3)),
                Integer.parseInt(ls.get(4))
        ));
    }

    @Override
    public void readMove(List<String> ls) {
        Move move = new Move(
                Integer.parseInt(ls.get(0)),
                Integer.parseInt(ls.get(1)),
                PLAYER_OPPONENT
        );

        state.doMove(move);
    }

    @Override
    public void writeMove() {
        // Find the next best move
        Move move = getNextMove();

        // Perform move on state
        state.doMove(move);

        if (DEBUG) printState(state);

        System.out.printf("%d %d\n", move.getColNum(), move.getOperation());
    }

    public void gameOver() {
        System.out.println("game over!!!");
        System.exit(0);
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    @Override
    public Move getNextMove() {
        return alphaBetaSearch(this.state);
    }

    private Move alphaBetaSearch(State state) {
        startTime = System.currentTimeMillis();
        return maxMove(state, NEGATIVE_INFINITY, POSITIVE_INFINITY);

    }

    private Move maxMove(State state, int alpha, int beta) {
        if (cutOff(state, TestPlayer.PLAYER_ME)) return eval(state, TestPlayer.PLAYER_ME);

        ArrayList<Move> moves = getMovesByState(state, TestPlayer.PLAYER_ME);
        if (DEBUG) System.out.println("Max in:" + moves.size());

        Move maxMove = new Move(NEGATIVE_INFINITY);

        for (Move move : moves) {
            if (DEBUG) printMove("Max", move);
            move.setScore(minMove(getResult(state, move), alpha, beta).getScore());
            maxMove = max(maxMove, move);
            if (DEBUG) System.out.println("Max out score:" + maxMove.getScore());

            if (maxMove.getScore() >= beta) return maxMove;
            alpha = Math.max(alpha, maxMove.getScore());
        }
        return maxMove;
    }

    private Move minMove(State state, int alpha, int beta) {
        if (cutOff(state, TestPlayer.PLAYER_OPPONENT)) return eval(state, TestPlayer.PLAYER_OPPONENT);

        ArrayList<Move> moves = getMovesByState(state, TestPlayer.PLAYER_OPPONENT);
        if (DEBUG) System.out.println("Min:" + moves.size());

        Move minMove = new Move(POSITIVE_INFINITY);

        for (Move move : moves) {
            move.setScore(maxMove(getResult(state, move), alpha, beta).getScore());
            minMove = min(minMove, move);
            if (DEBUG) System.out.println("Min score:" + minMove.getScore());

            if (minMove.getScore() <= alpha) return minMove;
            beta = Math.min(beta, minMove.getScore());
        }
        return minMove;
    }

    private Move min(Move move1, Move move2) {
        return move1.getScore() <= move2.getScore() ? move1 : move2;
    }

    private Move max(Move move1, Move move2) {
        return move1.getScore() >= move2.getScore() ? move1 : move2;
    }

    private boolean cutOff(State state, int player) {
        // 1) when depth <= 0, cutoff
        // 2) when game over, terminate
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - this.startTime;
        long timeLimit = ((config.getTimeLimit() / 2) - 1) * 1000;

        if ((timeElapsed >= timeLimit) || (state.gameOver(player) != State.RES_NOT_OVER)) {
            return true;
        }
        return false;
    }

    private ArrayList<Move> getMovesByState(State state, int player) {
        return state.getAvailableMoves(player);
    }

    private State getResult(State state, Move move) {
        if (DEBUG) printMove("Res", move);

        State newState = new State(state);
        newState.doMove(move);

        if (DEBUG) printState(newState);
        return newState;
    }

    private Move eval(State state, int player) {
        // 1) When the game win, score is POSITIVE INFINITY
        // 2) When the game lose, score is NEGATIVE INFINITY\
        // 3) When the game is draw, score is 0
        // 3) When there is n - 1 my color on the same line with q space between
        //    them and the position m row below the space there is a color, (n-1)^2 - e^3 - m^4
        // 4)

        switch (state.gameOver(player)) {
            case State.RES_WIN:
                if (DEBUG) System.out.println("Score:" + POSITIVE_INFINITY);
                return new Move(POSITIVE_INFINITY);
            case State.RES_LOSE:
                if (DEBUG) System.out.println("Score:" + NEGATIVE_INFINITY);
                return new Move(NEGATIVE_INFINITY);
            default:
                int boardHeight = state.getBoardHeight();
                int boardWidth = state.getBoardWidth();
                int numToConnect = state.getNumToConnect();
                int[][] board = state.getBoard();

                int score = 0;

                Move move = new Move(score);

                // N in row

                for (int row = boardHeight - 1; row >= 0; row--) {
                    int numMe = 0;
                    int numOpp = 0;

                    for (int col = 0; col < boardWidth; col++) {
                        int cell = board[row][col];

                        if ((cell == TestPlayer.PLAYER_ME || cell == TestPlayer.PLAYER_OPPONENT)) {
                            if (cell == TestPlayer.PLAYER_ME) numMe++;
                            else numOpp++;
                        }
                    }

                    score += numMe * 100;
                    score -= numOpp * 150;
                }

                // N in col

                for (int col = 0; col < boardWidth; col++) {
                    int numMe = 0;
                    int numOpp = 0;

                    for (int row = boardHeight - 1; row >= 0; row--) {
                        int cell = board[row][col];
                        if ((cell == TestPlayer.PLAYER_ME || cell == TestPlayer.PLAYER_OPPONENT)) {
                            if (cell == TestPlayer.PLAYER_ME) numMe++;
                            else numOpp++;
                        }
                    }

                    score += numMe * 100;
                    score -= numOpp * 150;
                }

                // N in diagonal from left bottom to right top

                int diaColBegin = boardWidth - numToConnect;
                int diaRowEnd = numToConnect - 1;

                for (int col = diaColBegin; col >= 0; col--) {
                    int numMe = 0;
                    int numOpp = 0;
                    for (int row = 0; col + row <= boardWidth - 1 && boardHeight - row - 1 >= 0; row++) {
                        int cell = board[boardHeight - row - 1][col + row];
                        if ((cell == TestPlayer.PLAYER_ME || cell == TestPlayer.PLAYER_OPPONENT)) {
                            if (cell == TestPlayer.PLAYER_ME) numMe++;
                            else numOpp++;
                        }
                    }
                    score += numMe * 60;
                    score -= numOpp * 80;
                }


                for (int row = boardHeight - 2; row >= diaRowEnd; row--) {
                    int numMe = 0;
                    int numOpp = 0;
                    for (int col = 0; row - col >= 0; col++) {
                        int cell = board[row - col][col];

                        if ((cell == TestPlayer.PLAYER_ME || cell == TestPlayer.PLAYER_OPPONENT)) {
                            if (cell == TestPlayer.PLAYER_ME) numMe++;
                            else numOpp++;
                        }
                    }
                    score += numMe * 60;
                    score -= numOpp * 80;
                }

                // N in diagonal from right bottom to left top

                diaColBegin = boardWidth - numToConnect;
                diaRowEnd = numToConnect - 1;

                for (int col = diaColBegin; col < boardWidth; col++) {
                    int numMe = 0;
                    int numOpp = 0;
                    for (int row = 0; col - row >= 0 && boardHeight - row - 1 >= 0; row++) {
                        int cell = board[boardHeight - row - 1][col - row];
                        if ((cell == TestPlayer.PLAYER_ME || cell == TestPlayer.PLAYER_OPPONENT)) {
                            if (cell == TestPlayer.PLAYER_ME) numMe++;
                            else numOpp++;
                        }
                    }
                    score += numMe * 60;
                    score -= numOpp * 80;
                }

                for (int row = boardHeight - 2; row >= diaRowEnd; row--) {
                    int numMe = 0;
                    int numOpp = 0;
                    for (int col = 0; row - col >= 0; col++) {
                        int cell = board[row - col][boardWidth - col - 1];

                        if ((cell == TestPlayer.PLAYER_ME || cell == TestPlayer.PLAYER_OPPONENT)) {
                            if (cell == TestPlayer.PLAYER_ME) numMe++;
                            else numOpp++;
                        }
                    }
                    score += numMe * 60;
                    score -= numOpp * 80;
                }

                move.setScore(score);
                return move;
        }

    }

    public static void main(String[] args) throws IOException {
        TestPlayer rp = new TestPlayer();
        System.out.println(rp.PLAYER_NAME);
        while (true) {
            rp.processInput();
        }

    }

    public void printState(State state) {
        for (int row = 0; row < state.getBoardHeight(); row++) {
            for (int col = 0; col < state.getBoardWidth(); col++) {
                System.out.print(state.getBoard()[row][col] + " ");
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }

    public void printMove(String prefix, Move move) {
        System.out.printf("%s-Move(%d, %d)\n", prefix, move.getColNum(), move.getOperation());
    }
}
