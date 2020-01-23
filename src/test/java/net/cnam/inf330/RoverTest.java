package net.cnam.inf330;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class for testing the Rover Mission Command Center application.
 */
public class RoverTest {
    /**
     * Initialize the MCC before the tests are run.
     */
    @BeforeClass // This method is run only once, before the test methods are run
    public static void initMissionCommandCenter() {
        // TODO 1) Initialize MCC singleton instance before the test methods are run
        MissionCommandCenter.getInstance();
    }

    /**
     * Application must catch an InvalidRoverPositionException if a rover has moved out of the grid.
     * Rover must pull back after moving out of the grid.
     */
    // TODO 5) Change this test to check that the rover pulls back after moving out of the grid
    @Test
    public void testRoverOutOfGridException() {
        MissionCommandCenter mcc = MissionCommandCenter.getInstance();
        mcc.setGridHeight(1);
        mcc.setGridWidth(1);

        Rover rover = new Rover(1, 0, 0, Orientation.N);
        mcc.addRover(rover);
        rover.moveForward();
        rover.moveForward();

        ThrowingRunnable tr = () -> mcc.checkRoverPosition(rover);
        assertThrows(InvalidRoverPositionException.class, tr);

        mcc.clearRovers();
    }

    /* TODO 3) 5) Write a new test for a scenario where 2 rovers collide at the same position on the grid
     *   and the second rover must pull back as a result
     */
    @Test
    public void testRoverOnRoverException() {
        MissionCommandCenter mcc = MissionCommandCenter.getInstance();
        mcc.setGridHeight(5);
        mcc.setGridWidth(5);

        Rover rover = new Rover(1, 0, 1, Orientation.N);
        mcc.addRover(rover);

        Rover rover2 = new Rover(2, 0, 0, Orientation.N);
        mcc.addRover(rover2);
        rover2.moveForward();

        // TEST COLLIDE
        ThrowingRunnable tr = () -> mcc.checkRoverPosition(rover2);
        assertThrows(InvalidRoverPositionException.class, tr);
        // TEST PULL BACK
        assertNotEquals(Integer.toString(rover.getX())+Integer.toString(rover.getY()), Integer.toString(rover2.getX())+Integer.toString(rover2.getY()));

        mcc.clearRovers();
    }

    /* TODO 5) Write a new test for a scenario where a rover is created at an invalid position
     *   and is not deployed as a result
     */
    @Test
    public void testRoverDeployedException() {
        MissionCommandCenter mcc = MissionCommandCenter.getInstance();
        mcc.setGridHeight(5);
        mcc.setGridWidth(5);

        assertNull(mcc.deployAndMoveRover(1, "6 6 N", "M"));
        assertTrue(mcc.getRovers().size()==0);

        mcc.clearRovers();
    }

    /**
     * Application must produce output data that matches the expected output after processing the input rover data.
     */
    @Test
    public void outputDataTest() throws IOException, URISyntaxException {
        List<String> inputLines = Main.readResourceFile("rover_test_input.txt");
        List<String> expectedOutputLines = Main.readResourceFile("rover_test_output.txt");

        // TODO 7) Test that processing the input lines produces an output that matches the expected output lines
        MissionCommandCenter mcc = MissionCommandCenter.getInstance();
        mcc.setGridHeight(5);
        mcc.setGridWidth(5);

        List<String> outputLines = mcc.processRoverData(inputLines);

        assertTrue(outputLines.equals(expectedOutputLines));
    }
}
