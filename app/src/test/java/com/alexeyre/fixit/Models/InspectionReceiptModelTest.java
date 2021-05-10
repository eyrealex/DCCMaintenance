package com.alexeyre.fixit.Models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InspectionReceiptModelTest {

    InspectionReceiptModel object = new InspectionReceiptModel("timestamp", "path", "created_by", "id", "location");

    @Test
    void gettimestamp() {
        assertEquals("timestamp", object.gettimestamp());
    }

    @Test
    void settimestamp() {
        object.settimestamp("new timestamp");
        assertEquals("new timestamp", object.gettimestamp());
    }

    @Test
    void getpath() {
        assertEquals("path", object.getpath());
    }

    @Test
    void setpath() {
        object.setpath("new path");
        assertEquals("new path", object.getpath());
    }

    @Test
    void getcreated_by() {
        assertEquals("created_by", object.getcreated_by());
    }

    @Test
    void setcreated_by() {
        object.setcreated_by("Alex");
        assertEquals("Alex", object.getcreated_by());
    }

    @Test
    void getid() {
        assertEquals("id", object.getid());
    }

    @Test
    void setid() {
        object.setid("123");
        assertEquals("123", object.getid());
    }

    @Test
    void getlocation() {
        assertEquals("location", object.getlocation());
    }

    @Test
    void setlocation() {
        object.setlocation("Dublin");
        assertEquals("Dublin", object.getlocation());
    }
}