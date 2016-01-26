import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Date: Jan 24, 2016
 * Author: Yang Liu
 * WPIid:yliu17
 */
public class Move{
    private int colNum;
    private int operation;
    private int score;
    private int player;

    public static int DROP = 1;
    public static int POP_OUT = 0;

    public Move(int colNum, int operation, int player) {
        this.colNum = colNum;
        this.operation = operation;
        this.player = player;
    }

    public Move(int score) {
        this.colNum = -1;
        this.operation = -1;
        this.score = score;
    }

    public int getColNum() {
        return colNum;
    }


    public int getOperation() {
        return operation;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlayer() {
        return player;
    }

}