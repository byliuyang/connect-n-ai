import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Date: Jan 24, 2016
 * Author: Yang Liu
 * WPIid:yliu17
 */
public class StateTest {
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

    @Test
    public void testDoMove() throws Exception {

        System.out.println("Test: testDoMove");

        Move move1 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        assertTrue(state.doMove(move1));

        Move move2 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        assertTrue(state.doMove(move2));

        Move move3 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        assertTrue(state.doMove(move3));

        Move move4 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        assertTrue(state.doMove(move4));

        Move move5 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        assertTrue(state.doMove(move5));

        Move move6 = new Move(3, Move.POP_OUT, TestPlayer.PLAYER_OPPONENT);
        assertFalse(state.doMove(move6));

        Move move7 = new Move(3, Move.POP_OUT, TestPlayer.PLAYER_ME);
        assertTrue(state.doMove(move7));
    }

    @org.junit.Test
    public void testGameOverRowWin() throws Exception {

        System.out.println("Test: testGameOverRowWin");

        Move move1 = new Move(2, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move1);

        Move move2 = new Move(1, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move2);

        Move move3 = new Move(4, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move3);

        Move move4 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move4);

        assertEquals(State.RES_WIN, state.gameOver(TestPlayer.PLAYER_ME));

    }

    @org.junit.Test
    public void testGameOverRowLose() throws Exception {

        System.out.println("Test: testGameOverRowLose");

        Move move1 = new Move(2, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move1);

        Move move2 = new Move(1, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move2);

        Move move3 = new Move(4, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move3);

        Move move4 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move4);

        assertEquals(State.RES_LOSE, state.gameOver(TestPlayer.PLAYER_ME));

    }

    @org.junit.Test
    public void testGameOverColWin() throws Exception {

        System.out.println("Test: testGameOverColWin");

        Move move1 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move1);

        Move move2 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move2);

        Move move3 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move3);

        Move move4 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move4);

        assertEquals(State.RES_WIN, state.gameOver(TestPlayer.PLAYER_ME));

    }

    @org.junit.Test
    public void testGameOverColLose() throws Exception {

        System.out.println("Test: testGameOverColLose");

        Move move1 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move1);

        Move move2 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move2);

        Move move3 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move3);

        Move move4 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move4);

        assertEquals(State.RES_LOSE, state.gameOver(TestPlayer.PLAYER_ME));

    }

    @Test
    public void testGameOverDiaWin() throws Exception {
        System.out.println("Test: testGameOverDiaWin");

        Move move1 = new Move(1, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move1);

        Move move2 = new Move(2, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move2);

        Move move3 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move3);

        Move move4 = new Move(4, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move4);

        state.doMove(move2);

        state.doMove(move3);
        state.doMove(move3);

        state.doMove(move4);
        state.doMove(move4);
        state.doMove(move4);

        assertEquals(State.RES_WIN, state.gameOver(TestPlayer.PLAYER_ME));
    }

    @Test
    public void testGameOverDiaLose() throws Exception {
        System.out.println("Test: testGameOverDiaLose");

        Move move1 = new Move(1, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move1);

        Move move2 = new Move(2, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move2);

        Move move3 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move3);

        Move move4 = new Move(4, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move4);

        state.doMove(move2);

        state.doMove(move3);
        state.doMove(move3);

        state.doMove(move4);
        state.doMove(move4);
        state.doMove(move4);

        assertEquals(State.RES_LOSE, state.gameOver(TestPlayer.PLAYER_ME));
    }

    @Test
    public void testGameOverAntiDiaWin() throws Exception {
        System.out.println("Test: testGameOverAntiDiaWin");

        Move move5 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);

        state.doMove(move5);

        Move move6 = new Move(2, Move.DROP, TestPlayer.PLAYER_OPPONENT);

        state.doMove(move6);
        state.doMove(move6);

        Move move7 = new Move(1, Move.DROP, TestPlayer.PLAYER_OPPONENT);

        state.doMove(move7);
        state.doMove(move7);
        state.doMove(move7);

        Move move1 = new Move(4, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move1);

        Move move2 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move2);

        Move move3 = new Move(2, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move3);

        Move move4 = new Move(1, Move.DROP, TestPlayer.PLAYER_ME);
        state.doMove(move4);

        assertEquals(State.RES_WIN, state.gameOver(TestPlayer.PLAYER_ME));
    }

    @Test
    public void testGameOverAntiDiaLose() throws Exception {
        System.out.println("Test: testGameOverAntiDiaLose");

        Move move5 = new Move(3, Move.DROP, TestPlayer.PLAYER_ME);

        state.doMove(move5);

        Move move6 = new Move(2, Move.DROP, TestPlayer.PLAYER_ME);

        state.doMove(move6);
        state.doMove(move6);

        Move move7 = new Move(1, Move.DROP, TestPlayer.PLAYER_ME);

        state.doMove(move7);
        state.doMove(move7);
        state.doMove(move7);

        Move move1 = new Move(4, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move1);

        Move move2 = new Move(3, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move2);

        Move move3 = new Move(2, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move3);

        Move move4 = new Move(1, Move.DROP, TestPlayer.PLAYER_OPPONENT);
        state.doMove(move4);

        assertEquals(State.RES_LOSE, state.gameOver(TestPlayer.PLAYER_ME));
    }
}