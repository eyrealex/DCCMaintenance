package com.alexeyre.fixit.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceReportModelTest {
    MaintenanceReportModel object = new MaintenanceReportModel("physical_issues", "electrical_issues", "light_issues", "button_issues", "sound_issues", "sequence_issues", "repairs_needed", "notes", "image_url", "signature_url", "report_key", "created_by");

    @Test
    void getPhysical_issues() {
        assertEquals("physical_issues", object.getPhysical_issues());
    }

    @Test
    void setphysical_issues() {
        object.setphysical_issues("Yes");
        assertEquals("Yes", object.getPhysical_issues());
    }

    @Test
    void getelectrical_issues() {
        assertEquals("electrical_issues", object.getelectrical_issues());
    }

    @Test
    void setelectrical_issues() {
        object.setelectrical_issues("Yes");
        assertEquals("Yes", object.getelectrical_issues());
    }

    @Test
    void getlight_issues() {
        assertEquals("light_issues", object.getlight_issues());
    }

    @Test
    void setlight_issues() {
        object.setlight_issues("Yes");
        assertEquals("Yes", object.getlight_issues());
    }

    @Test
    void getbutton_issues() {
        assertEquals("button_issues", object.getbutton_issues());
    }

    @Test
    void setbutton_issues() {
        object.setbutton_issues("Yes");
        assertEquals("Yes", object.getbutton_issues());
    }

    @Test
    void getsound_issues() {
        assertEquals("sound_issues", object.getsound_issues());
    }

    @Test
    void setsound_issues() {
        object.setsound_issues("Yes");
        assertEquals("Yes", object.getsound_issues());
    }

    @Test
    void getsequence_issues() {
        assertEquals("sequence_issues", object.getsequence_issues());
    }

    @Test
    void setsequence_issues() {
        object.setsequence_issues("Yes");
        assertEquals("Yes", object.getsequence_issues());
    }

    @Test
    void getrepairs_needed() {
        assertEquals("repairs_needed", object.getrepairs_needed());
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
        assertEquals("image_url", object.getimage_url());
    }

    @Test
    void setimage_url() {
        object.setimage_url("image url");
        assertEquals("image url", object.getimage_url());
    }

    @Test
    void getsignature_url() {
        assertEquals("signature_url", object.getsignature_url());
    }

    @Test
    void setsignature_url() {
        object.setsignature_url("signature url");
        assertEquals("signature url", object.getsignature_url());
    }

    @Test
    void getreport_key() {
        assertEquals("report_key", object.getreport_key());
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