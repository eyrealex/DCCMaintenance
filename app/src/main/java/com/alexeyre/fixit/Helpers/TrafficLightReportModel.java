package com.alexeyre.fixit.Helpers;

import java.io.Serializable;

public class TrafficLightReportModel implements Serializable {
    String physical_damage, electrical_damage, sounds_ok, lights_ok, pedestrian_button_ok, pedestrian_lights_ok, sequence_ok, repairs_needed, notes, image_url, signature_url, report_key;

    public TrafficLightReportModel() {
    }

    public String getphysical_damage() {
        return physical_damage;
    }

    public void setphysical_damage(String physical_damage) {
        this.physical_damage = physical_damage;
    }

    public String getelectrical_damage() {
        return electrical_damage;
    }

    public void setelectrical_damage(String electrical_damage) {
        this.electrical_damage = electrical_damage;
    }

    public String getsounds_ok() {
        return sounds_ok;
    }

    public void setsounds_ok(String sounds_ok) {
        this.sounds_ok = sounds_ok;
    }

    public String getlights_ok() {
        return lights_ok;
    }

    public void setlights_ok(String lights_ok) {
        this.lights_ok = lights_ok;
    }

    public String getpedestrian_button_ok() {
        return pedestrian_button_ok;
    }

    public void setpedestrian_button_ok(String pedestrian_button_ok) {
        this.pedestrian_button_ok = pedestrian_button_ok;
    }

    public String getpedestrian_lights_ok() {
        return pedestrian_lights_ok;
    }

    public void setpedestrian_lights_ok(String pedestrian_lights_ok) {
        this.pedestrian_lights_ok = pedestrian_lights_ok;
    }

    public String getsequence_ok() {
        return sequence_ok;
    }

    public void setsequence_ok(String sequence_ok) {
        this.sequence_ok = sequence_ok;
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
