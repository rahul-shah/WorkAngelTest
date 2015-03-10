package com.example.rahulshah.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.rahulshah.test.Adapter.SpinnerAdapter;
import com.example.rahulshah.test.Model.AmountInput;
import com.example.rahulshah.test.Model.Currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactDetailActivity extends Activity
{
    private StringRequest mJsonReq;
    private JsonObjectRequest mJsonReq1;
    private RequestQueue mQueue;
    private String mUri;
    private ListView mListOfQuals;
    private ArrayList<String> mAPIResponseList;
    public static Cache.Entry mEntry;
    private String mSendString;
    private String mReceiveString;
    private Spinner mCurrencySpinner;
    private Spinner mCurrencyReceiveSpinner;
    private Button mCalculateButton;
    private Button mSendButton;
    private EditText mAmountToSend;
    private ProgressDialog progressDialog;
    private String mReceiveAmount = null;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        mCurrencySpinner = (Spinner) findViewById(R.id.spinner_send);
        mCurrencyReceiveSpinner = (Spinner) findViewById(R.id.spinner_receive);
        mCalculateButton = (Button) findViewById(R.id.btnCalculate);
        mSendButton = (Button) findViewById(R.id.btnSend);
        mAmountToSend = (EditText) findViewById(R.id.send_amount);

        mCalculateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mAmountToSend.getText().toString().trim().isEmpty())
                {
                    getAmountFromAPI();
                    progressDialog = getLoadingDialog(ContactDetailActivity.this);
                    progressDialog.show();
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mAmountToSend.getText().toString().trim().isEmpty())
                {
                    if(mReceiveAmount == null)
                    {
                        new AlertDialog.Builder(ContactDetailActivity.this)
                                .setTitle("Error")
                                .setMessage("Please calculate amount before sending")
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                            }
                                        })
                                .setCancelable(false)
                                .show();
                    }
                    else
                    {
                        sendAmount();
                    }
                }
            }
        });

        mCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        mSendString = "GBP";
                        break;

                    case 1:
                        mSendString = "USD";
                        break;

                    case 2:
                        mSendString = "PHP";
                        break;

                    case 3:
                        mSendString = "EUR";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mCurrencyReceiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        mReceiveString = "GBP";
                        break;

                    case 1:
                        mReceiveString = "USD";
                        break;

                    case 2:
                        mReceiveString = "PHP";
                        break;

                    case 3:
                        mReceiveString = "EUR";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mUserName = getIntent().getStringExtra("USER_NAME");
        TextView userNameView = (TextView) findViewById(R.id.textView);
        userNameView.setText(mUserName);

        //Get Currencies from API
        getCurrencies();

    }

    public void sendAmount()
    {
        mUri = "https://wr-interview.herokuapp.com/api/send";

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();

        AmountInput inputObject = new AmountInput();
        inputObject.setsendamount(Integer.parseInt(mAmountToSend.getText().toString()));
        inputObject.setsendcurrency(mSendString);
        inputObject.setreceiveamount(Integer.parseInt(mReceiveAmount));
        inputObject.setreceivecurrency(mReceiveString);
        inputObject.setrecipient(mUserName);

        /*JSONObject tempObject = new JSONObject();
        try
        {
            tempObject.put("sendamount",Integer.parseInt(mAmountToSend.getText().toString()));
            tempObject.put("sendcurrency",mSendString);
            tempObject.put("receiveamount",Integer.parseInt(mReceiveAmount));
            tempObject.put("receivecurrency",mReceiveString);
            tempObject.put("recipient",mUserName);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        mJsonReq1 = new JsonObjectRequest(mUri,tempObject,new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                new AlertDialog.Builder(ContactDetailActivity.this)
                        .setTitle("Congratulations")
                        .setMessage("Your transaction has been processed")
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                })
                        .setCancelable(false)
                        .show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    }
                });
        mJsonReq1.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(mJsonReq1);*/

        mQueue.add(new SendAmountRequest(inputObject, Request.Method.POST, mUri,new Response.Listener<String>()
        {
            @Override
            public void onResponse(String output)
            {
                new AlertDialog.Builder(ContactDetailActivity.this)
                        .setTitle("Congratulations")
                        .setMessage("Your transaction has been processed")
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                        ContactDetailActivity.this.finish();
                                    }
                                })
                        .setCancelable(false)
                        .show();
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {

            }
        }));
    }

    public static ProgressDialog getLoadingDialog(final Context context)
    {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Calculating");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public void getAmountFromAPI()
    {
        mUri = "https://wr-interview.herokuapp.com/api/calculate?amount=" + mAmountToSend.getText().toString() + "&sendcurrency=" + mSendString + "&receivecurrency=" + mReceiveString;

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();

        mJsonReq = new StringRequest(mUri,new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                JsonParser parser = new JsonParser();

                JsonObject eventArray = parser.parse(response.toString()).getAsJsonObject();

                mReceiveAmount = eventArray.get("receiveamount").getAsString();

                new AlertDialog.Builder(ContactDetailActivity.this)
                        .setTitle("Receivable Amount")
                        .setMessage("Your friend would get " + eventArray.get("receiveamount"))
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                })
                        .setCancelable(false)
                        .show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }
                });
        mJsonReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(mJsonReq);
    }

    public void getCurrencies()
    {
        mUri = "https://wr-interview.herokuapp.com/api/currencies";

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();

        mEntry = mQueue.getCache().get("https://wr-interview.herokuapp.com/api/currencies");

        mJsonReq = new StringRequest(mUri,new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                mEntry = new Cache.Entry();
                mEntry.data = response.toString().getBytes();
                mQueue.getCache().put("https://wr-interview.herokuapp.com/api/currencies",mEntry);

                mAPIResponseList = new ArrayList<String>();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                JsonParser parser = new JsonParser();

                JsonArray eventArray = parser.parse(response.toString()).getAsJsonArray();

                for (JsonElement obj : eventArray)
                {
                    //Currency currencyFromAPI = gson.fromJson(obj, Currency.class);
                    mAPIResponseList.add(obj.getAsString());
                }

                //Add to spinner
                SpinnerAdapter adapter = new SpinnerAdapter(ContactDetailActivity.this,android.R.layout.simple_spinner_item,mAPIResponseList);
                adapter.setDropDownViewResource(R.layout.spinner_list_items);
                mCurrencySpinner.setAdapter(adapter);
                mCurrencyReceiveSpinner.setAdapter(adapter);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });
        mJsonReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(mEntry == null)
        {
            mJsonReq.setShouldCache(true);
            mQueue.add(mJsonReq);
        }
        //Load from cache but also look for new events
        else
        {
            String responseCacheString = new String(mEntry.data);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonParser parser = new JsonParser();

            mAPIResponseList = new ArrayList<String>();

            JsonArray eventArray = parser.parse(responseCacheString).getAsJsonArray();

            for (JsonElement obj : eventArray)
            {
                //Currency currencyFromAPI = gson.fromJson(obj, Currency.class);
                mAPIResponseList.add(obj.getAsString());
            }

            //Add to spinner
            SpinnerAdapter adapter = new SpinnerAdapter(ContactDetailActivity.this,android.R.layout.simple_spinner_item,mAPIResponseList);
            adapter.setDropDownViewResource(R.layout.spinner_list_items);
            mCurrencySpinner.setAdapter(adapter);
            mCurrencyReceiveSpinner.setAdapter(adapter);
        }
    }

    class SendAmountRequest extends StringRequest
    {

        private AmountInput mInput;

        public SendAmountRequest(final AmountInput input, final int method, final String url,
                                       final Response.Listener<String> listener,
                                       final Response.ErrorListener errorListener)
        {
            super(method, url, listener, errorListener);
            mInput = input;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept-Language", "en-US,en;q=0.8");
            return headers;
        }

        @Override
        public byte[] getBody() throws AuthFailureError
        {
            try
            {
                return new Gson().toJson(mInput).getBytes("utf-8");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            return super.getBody();
        }

        @Override
        public String getBodyContentType() {
            return "application/json";
        }
    }
}