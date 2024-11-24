package com.v;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    @Test
    void testSimulationRuns() {
        Simulation simulation = new Simulation();
        String result = simulation.runSimulation();
        assertNotNull(result);
        assertTrue(result.contains("Simulation"));
    }
}
