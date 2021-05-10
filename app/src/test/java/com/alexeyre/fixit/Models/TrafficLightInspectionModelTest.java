package com.alexeyre.fixit.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightInspectionModelTest {

    TrafficLightInspectionModel object = new TrafficLightInspectionModel("inspection_by", "timestamp");

    @Test
    void getinspection_by() {
        assertEquals("inspection_by", object.getinspection_by());
    }

    @Test
    void setinspection_by() {
        object.setinspection_by("Alex");
        assertEquals("Alex", object.getinspection_by());
    }

    @Test
    void gettimestamp() {
        assertEquals("timestamp", object.gettimestamp());
    }

    @Test
    void settimestamp() {
        object.settimestamp("10th June");
        assertEquals("10th June", object.gettimestamp());
    }
}