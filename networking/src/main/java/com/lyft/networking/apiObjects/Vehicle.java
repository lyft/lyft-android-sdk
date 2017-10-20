package com.lyft.networking.apiObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Model containing vehicle meta data
 */

public class Vehicle
{
    /**
     * Vehicle year
     */
    @SerializedName("year")
    public int year;

    /**
     * Vehicle make
     */
    @SerializedName("make")
    public String make;

    /**
     * Vehicle model
     */
    @SerializedName("model")
    public String model;

    /**
     * Vehicle license plate
     */
    @SerializedName("license_plate")
    public String license_plate;

    /**
     * Vehicle license plate state
     */
    @SerializedName("license_plate_state")
    public String license_plate_state;

    /**
     * Vehicle color
     */
    @SerializedName("color")
    public String color;

    /**
     * Vehicle image_ur
     */
    @SerializedName("image_ur")
    public String image_url;

}
