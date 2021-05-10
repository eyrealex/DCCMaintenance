package com.alexeyre.fixit.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightReportModelTest {
    TrafficLightReportModel object = new TrafficLightReportModel("physical", "electrical", "light", "button", "sound", "sequence", "repairs", "notes", "image", "signature", "key", "created_by");

    @Test
    void getPhysical_issues() {
        assertEquals("physical", object.getPhysical_issues());
    }

    @Test
    void setphysical_issues() {
        object.setphysical_issues("Yes");
        assertEquals("Yes", object.getPhysical_issues());
    }

    @Test
    void getelectrical_issues() {
        assertEquals("electrical", object.getelectrical_issues());
    }

    @Test
    void setelectrical_issues() {
        object.setelectrical_issues("Yes");
        assertEquals("Yes", object.getelectrical_issues());
    }

    @Test
    void getlight_issues() {
        assertEquals("light", object.getlight_issues());
    }

    @Test
    void setlight_issues() {
        object.setlight_issues("Yes");
        assertEquals("Yes", object.getlight_issues());
    }

    @Test
    void getbutton_issues() {
        assertEquals("button", object.getbutton_issues());
    }

    @Test
    void setbutton_issues() {
        object.setbutton_issues("Yes");
        assertEquals("Yes", object.getbutton_issues());
    }

    @Test
    void getsound_issues() {
        assertEquals("sound", object.getsound_issues());
    }

    @Test
    void setsound_issues() {
        object.setsound_issues("Yes");
        assertEquals("Yes", object.getsound_issues());
    }

    @Test
    void getsequence_issues() {
        assertEquals("sequence", object.getsequence_issues());
    }

    @Test
    void setsequence_issues() {
        object.setsequence_issues("Yes");
        assertEquals("Yes", object.getsequence_issues());
    }

    @Test
    void getrepairs_needed() {
        assertEquals("repairs", object.getrepairs_needed());
    }

    @Test
    void setrepairs_needed() {
        object.setrepairs_needed("Yes");
        assertEquals("Yes", object.getrepairs_needed());
    }

    @Test
    void getnotes() {
        assertEquals("notes", object.getnotes());
    }

    @Test
    void setnotes() {
        object.setnotes("Yes");
        assertEquals("Yes", object.getnotes());
    }

    @Test
    void getimage_url() {
        assertEquals("image", object.getimage_url());
    }

    @Test
    void setimage_url() {
        object.setimage_url("image url");
        assertEquals("image url", object.getimage_url());
    }

    @Test
    void getsignature_url() {
        assertEquals("signature", object.getsignature_url());
    }

    @Test
    void setsignature_url() {
        object.setsignature_url("signature url");
        assertEquals("signature url", object.getsignature_url());
    }

    @Test
    void getreport_key() {
        assertEquals("key", object.getreport_key());
    }

    @Test
    void setreport_key() {
        object.setreport_key("001");
        assertEquals("001", object.getreport_key());
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