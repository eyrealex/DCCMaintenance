package com.alexeyre.fixit.Models;

import java.io.Serializable;

public class TrafficLightReportModel implements Serializable {
    String physical_issues, electrical_issues, light_issues, button_issues, sound_issues, sequence_issues, repairs_needed, notes, image_url, signature_url, report_key;

    public TrafficLightReportModel() {
    }


    public String getPhysical_issues() {
        return physical_issues;
    }

    public void setphysical_issues(String physical_issues) {
        this.physical_issues = physical_issues;
    }

    public String getelectrical_issues() {
        return electrical_issues;
    }

    public void setelectrical_issues(String electrical_issues) {
        this.electrical_issues = electrical_issues;
    }

    public String getlight_issues() {
        return light_issues;
    }

    public void setlight_issues(String light_issues) {
        this.light_issues = light_issues;
    }

    public String getbutton_issues() {
        return button_issues;
    }

    public void setbutton_issues(String button_issues) {
        this.button_issues = button_issues;
    }

    public String getsound_issues() {
        return sound_issues;
    }

    public void setsound_issues(String sound_issues) {
        this.sound_issues = sound_issues;
    }

    public String getsequence_issues() {
        return sequence_issues;
    }

    public void setsequence_issues(String sequence_issues) {
        this.sequence_issues = sequence_issues;
    }

    public String getrepairs_needed() {
        return repairs_needed;
    }

    public void setrepairs_needed(String repairs_needed) {
        this.repairs_needed = repairs_needed;
    }

    public String getnotes() {
        return notes;
    }

    public void setnotes(String notes) {
        this.notes = notes;
    }

    public String getimage_url() {
        return image_url;
    }

    public void setimage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getsignature_url() {
        return signature_url;
    }

    public void setsignature_url(String signature_url) {
        this.signature_url = signature_url;
    }

    public String getreport_key() {
        return report_key;
    }

    public void setreport_key(String report_key) {
        this.report_key = report_key;
    }
}
