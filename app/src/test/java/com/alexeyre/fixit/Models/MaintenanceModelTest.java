package com.alexeyre.fixit.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceModelTest {

    MaintenanceModel object = new MaintenanceModel("key", "timestamp", "name", "created_by");

    @Test
    void getkey() {
        assertEquals("key", object.getkey());
    }

    @Test
    void setkey() {
        object.setkey("001");
        assertEquals("001", object.getkey());
    }

    @Test
    void gettimestamp() {
        assertEquals("timestamp", object.gettimestamp());
    }

    @Test
    void settimestamp() {
        object.settimestamp("April");
        assertEquals("April", object.gettimestamp());
    }

    @Test
    void getname() {
        assertEquals("name", object.getname());
    }

    @Test
    void setname() {
        object.setname("Dublin");
        assertEquals("Dublin", object.getname());
    }

    @Test
    void getcreated_by() {
        assertEquals("created_by", object.getcreated_by());
    }

    @Test
    void setcreated_by() {
        object.setcreated_by("Alex Eyre");
        assertEquals("Alex Eyre", object.getcreated_by());
    }
}