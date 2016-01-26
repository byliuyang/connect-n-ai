/**
 * Date: Jan 24, 2016
 * Author: Yang Liu
 * WPIid:yliu17
 */
public class Config {
    private int boardHeight;
    private int boardWidth;
    private int numPieceWin;
    private int turnOfPlayer;
    private int timeLimit;

    public Config(int boardHeight, int boardWidth, int numPieceWin, int turnOfPlayer, int timeLimit) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.numPieceWin = numPieceWin;
        this.turnOfPlayer = turnOfPlayer;
        this.timeLimit = timeLimit;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getNumPieceWin() {
        return numPieceWin;
    }

    public void setNumPieceWin(int numPieceWin) {
        this.numPieceWin = numPieceWin;
    }

    public int getTurnOfPlayer() {
        return turnOfPlayer;
    }

    public void setTurnOfPlayer(int turnOfPlayer) {
        this.turnOfPlayer = turnOfPlayer;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}
