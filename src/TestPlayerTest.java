import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Date: Jan 24, 2016
 * Author: Yang Liu
 * WPIid:yliu17
 */
public class TestPlayerTest {

    State state;

    @org.junit.Before
    public void setUp() throws Exception {
        state = new State(7, 6, 4);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        for (int row = 0; row < state.getBoardHeight(); row++) {
            for (int col = 0; col < state.getBoardWidth(); col++) {
                System.out.print(state.getBoard()[row][col] + " ");
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }
}