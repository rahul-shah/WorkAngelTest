package com.example.rahulshah.test.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Currency
{
    @SerializedName("name")
    @Expose
    private String name;

    public String getName()
    {
        return name;
    }
}