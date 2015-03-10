package com.example.rahulshah.test.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Almost all requests send a UUID and most send an Auth Token so will put them here.
 *
 * Created by dre on 24/07/14.
 */
public class AmountInput {

    @SerializedName("sendamount")
    private int sendamount;

    @SerializedName("sendcurrency")
    private String sendcurrency;

    @SerializedName("receiveamount")
    private int receiveamount;

    @SerializedName("receivecurrency")
    private String receivecurrency;

    @SerializedName("recipient")
    private String recipient;


    public void setsendamount(int authToken)
    {
        sendamount = authToken;
    }


    public void setsendcurrency(String uuid)
    {
        sendcurrency = uuid;
    }

    public void setreceiveamount(int uuid)
    {
        receiveamount = uuid;
    }

    public void setreceivecurrency(String uuid)
    {
        receivecurrency = uuid;
    }

    public void setrecipient(String uuid)
    {
        recipient = uuid;
    }
}