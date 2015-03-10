package com.example.rahulshah.test.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.rahulshah.test.R;

import java.util.ArrayList;

/**
 * Created by Rahul Shah on 05/11/2014.
 */
public class SpinnerAdapter extends ArrayAdapter
{
    private Context context;
    private int layoutResourceId;
    public static boolean mIsUserLoggedIn = false;
    protected ImageLoader loader;
    ArrayList<String> mAgeList = new ArrayList<String>();

    public SpinnerAdapter(Context context, int layoutResourceId, ArrayList<String> ageList)
    {
        super(context, layoutResourceId,ageList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        mAgeList.addAll(ageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        NetworkImageView rowImage = null;
        ImageView arrowIcon = null;
        TextView rowText = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_list, parent, false);
        }

        rowText = (TextView)row.findViewById(R.id.ageTitle);
        rowText.setText(mAgeList.get(position));
        return row;
    }
}
