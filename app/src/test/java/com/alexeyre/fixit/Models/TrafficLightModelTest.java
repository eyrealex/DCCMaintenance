package com.alexeyre.fixit.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightModelTest {

    TrafficLightModel object = new TrafficLightModel("name", "latitude", "longitude", "key");

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
    void getname() {
        assertEquals("name", object.getname());
    }

    @Test
    void setname() {
        object.setname("Alex Eyre");
        assertEquals("Alex Eyre", object.getname());
    }

    @Test
    void getlatitude() {
        assertEquals("latitude", object.getlatitude());
    }

    @Test
    void setlatitude() {
        object.setlatitude("33456.67");
        assertEquals("33456.67", object.getlatitude());
    }

    @Test
    void getlongitude() {
        assertEquals("longitude", object.getlongitude());
    }

    @Test
    void setlongitude() {
        object.setlongitude("-6.7764");
        assertEquals("-6.7764", object.getlongitude());
    }
}