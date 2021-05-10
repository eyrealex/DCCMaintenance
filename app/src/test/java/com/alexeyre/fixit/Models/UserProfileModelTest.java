package com.alexeyre.fixit.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileModelTest {

    UserProfileModel object = new UserProfileModel("name", "email", "phone", "uid");


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
    void getemail() {
        assertEquals("email", object.getemail());
    }

    @Test
    void setemail() {
        object.setemail("alex@gmail.com");
        assertEquals("alex@gmail.com", object.getemail());
    }

    @Test
    void getphone() {
        assertEquals("phone", object.getphone());
    }

    @Test
    void setphone() {
        object.setphone("0867774821");
        assertEquals("0867774821", object.getphone());
    }

    @Test
    void getuid() {
        assertEquals("uid", object.getuid());
    }

    @Test
    void setuid() {
        object.setuid("001");
        assertEquals("001", object.getuid());
    }
}