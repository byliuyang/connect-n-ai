import java.util.List;

/**
 * Date: Jan 24, 2016
 * Author: Yang Liu
 * WPIid:yliu17
 */

public interface ITestPlayer {
    void init(List<String> ls);
    void sendName();
    void readConfig(List<String> ls);
    void readMove(List<String> ls);
    Move getNextMove();
    void writeMove();
}