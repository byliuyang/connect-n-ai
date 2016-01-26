import java.util.ArrayList;

/**
 * Date: Jan 24, 2016
 * Author: Yang Liu
 * WPIid:yliu17
 */
public class State {
    private int boardWidth;
    private int boardHeight;
    private int numToConnect;
    private int[][] board;
    private int[] colCount;
    private boolean[] playerPopOut;

    private final int EMPTY_CELL = 9;

    public static final int RES_WIN = 1;
    public static final int RES_LOSE = -1;
    public static final int RES_DRAW = 0;
    public static final int RES_NOT_OVER = 0;

    /**
     * Create new state from a old state
     *
     * @param state Target state
     */
    public State(State state) {

        if(state == null) {
            return;
        }

        this.boardWidth = state.getBoardWidth();
        this.boardHeight = state.getBoardHeight();
        this.numToConnect = state.numToConnect;

        this.playerPopOut = new boolean[2];
        this.playerPopOut[0] = state.getPlayerPopOut()[0];
        this.playerPopOut[1] = state.getPlayerPopOut()[1];

        board = new int[boardHeight][boardWidth];
        colCount = new int[boardWidth];

        System.arraycopy(state.getColCount(), 0, colCount, 0, state.getColCount().length);

        for (int row = 0; row < boardHeight; row++) {
            System.arraycopy(state.getBoard()[row], 0, board[row], 0, state.getBoard()[row].length);
        }
    }

    public State(int boardWidth, int boardHeight, int numToConnect) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.numToConnect = numToConnect;
        init();
    }

    private void init() {
        this.playerPopOut = new boolean[2];
        this.playerPopOut[0] = false;
        this.playerPopOut[1] = false;

        board = new int[boardHeight][boardWidth];

        colCount = new int[boardWidth];
        for (int col = 0; col < boardWidth; col++)
            colCount[col] = 0;

        for (int row = 0; row < boardHeight; row++) {
            for (int col = 0; col < boardWidth; col++) {
                board[row][col] = EMPTY_CELL;
            }
        }
    }

    public boolean doMove(Move move) {
        if (isValid(move)) {
            int operation = move.getOperation();
            int col = move.getColNum();
            if (operation == Move.DROP) {
                board[boardHeight - colCount[col] - 1][col] = move.getPlayer();
                colCount[col]++;

                return true;
            } else if (operation == Move.POP_OUT) {
                this.playerPopOut[move.getPlayer()] = true;

                for (int row = boardHeight - 1; row >= boardHeight - colCount[col]; row--)
                    board[row][col] = board[row - 1][col];
                colCount[col]--;
                return true;
            }
        }

        return false;

    }

    private boolean isValid(Move move) {
        int col = move.getColNum();

        // 1) 0 <= col <= boardWidth
        // 2) col is not full
        if ((col >= 0) && (col <= boardWidth - 1) && (colCount[col] < boardHeight)) {
            int operation = move.getOperation();

            // Drop valid
            // 1) Not popped out yet
            // 2) The bottom color is the player's
            return (operation == Move.DROP)
                    || ((operation == Move.POP_OUT) && (!this.playerPopOut[move.getPlayer()]) && (board[boardHeight - 1][col] == move.getPlayer()));
        }

        return false;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean[] getPlayerPopOut() {
        return playerPopOut;
    }

    public int[] getColCount() {
        return colCount;
    }

    public int getNumToConnect() {
        return numToConnect;
    }

    public ArrayList<Move> getAvailableMoves(int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int col = 0; col < boardWidth; col++) {
            for (int opt = 0; opt < 2; opt++) {
                Move move = new Move(col, opt, player);
                if (isValid(move)) moves.add(move);
            }
        }

        return moves;
    }

    public int gameOver(int player) {
        // When no available moves, game is draw
        if (getAvailableMoves(player).size() == 0) return RES_DRAW;

        // N in row
        int numInLine = 0;
        int lineColor = TestPlayer.PLAYER_ME;

        for (int row = boardHeight - 1; row >= 0; row--) {
            for (int col = 0; col < boardWidth; col++) {
                int cell = board[row][col];
                if (cell != lineColor) {
                    numInLine = 0;
                    lineColor = cell;
                }
                numInLine++;

                if ((lineColor == TestPlayer.PLAYER_ME || lineColor == TestPlayer.PLAYER_OPPONENT) && numInLine >= numToConnect) {
                    if (lineColor == TestPlayer.PLAYER_ME) return RES_WIN;
                    else return RES_LOSE;
                }
            }
        }

        // N in col
        numInLine = 0;
        lineColor = TestPlayer.PLAYER_ME;

        for (int col = 0; col < boardWidth; col++) {
            for (int row = boardHeight - 1; row >= 0; row--) {
                int cell = board[row][col];
                if (cell != lineColor) {
                    numInLine = 0;
                    lineColor = cell;
                }
                numInLine++;

                if ((lineColor == TestPlayer.PLAYER_ME || lineColor == TestPlayer.PLAYER_OPPONENT) && numInLine >= numToConnect) {
                    if (lineColor == TestPlayer.PLAYER_ME) return RES_WIN;
                    else return RES_LOSE;
                }
            }
        }

        // N in diagonal from left bottom to right top
        numInLine = 0;
        lineColor = TestPlayer.PLAYER_ME;

        int diaColBegin = boardWidth - numToConnect;
        int diaRowEnd = numToConnect - 1;

        for (int col = diaColBegin; col >= 0; col--) {
            for (int row = 0; col + row <= boardWidth - 1 && boardHeight - row - 1 >= 0; row++) {
                int cell = board[boardHeight - row - 1][col + row];
                if (cell != lineColor) {
                    numInLine = 0;
                    lineColor = cell;
                }
                numInLine++;

                if ((lineColor == TestPlayer.PLAYER_ME || lineColor == TestPlayer.PLAYER_OPPONENT) && numInLine >= numToConnect) {
                    if (lineColor == TestPlayer.PLAYER_ME) return RES_WIN;
                    else return RES_LOSE;
                }
            }
        }

        numInLine = 0;
        lineColor = TestPlayer.PLAYER_ME;

        for (int row = boardHeight - 2; row >= diaRowEnd; row--) {
            for (int col = 0; row - col >= 0; col++) {
                int cell = board[row - col][col];

                if (cell != lineColor) {
                    numInLine = 0;
                    lineColor = cell;
                }
                numInLine++;

                if ((lineColor == TestPlayer.PLAYER_ME || lineColor == TestPlayer.PLAYER_OPPONENT) && numInLine >= numToConnect) {
                    if (lineColor == TestPlayer.PLAYER_ME) return RES_WIN;
                    else return RES_LOSE;
                }
            }
        }

        // N in diagonal from right bottom to left top

        numInLine = 0;
        lineColor = TestPlayer.PLAYER_ME;

        diaColBegin = boardWidth - numToConnect;
        diaRowEnd = numToConnect - 1;

        for (int col = diaColBegin; col < boardWidth; col++) {
            for (int row = 0; col - row >= 0 && boardHeight - row - 1 >= 0; row++) {
                int cell = board[boardHeight - row - 1][col - row];
                if (cell != lineColor) {
                    numInLine = 0;
                    lineColor = cell;
                }
                numInLine++;

                if ((lineColor == TestPlayer.PLAYER_ME || lineColor == TestPlayer.PLAYER_OPPONENT) && numInLine >= numToConnect) {
                    if (lineColor == TestPlayer.PLAYER_ME) return RES_WIN;
                    else return RES_LOSE;
                }
            }
        }

        numInLine = 0;
        lineColor = TestPlayer.PLAYER_ME;

        for (int row = boardHeight - 2; row >= diaRowEnd; row--) {
            for (int col = 0; row - col >= 0; col++) {
                int cell = board[row - col][boardWidth - col - 1];

                if (cell != lineColor) {
                    numInLine = 0;
                    lineColor = cell;
                }
                numInLine++;

                if ((lineColor == TestPlayer.PLAYER_ME || lineColor == TestPlayer.PLAYER_OPPONENT) && numInLine >= numToConnect) {
                    if (lineColor == TestPlayer.PLAYER_ME) return RES_WIN;
                    else return RES_LOSE;
                }
            }
        }

        return RES_NOT_OVER;
    }

}
