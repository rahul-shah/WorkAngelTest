package com.example.rahulshah.test;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends ActionBarActivity
{
    static ArrayList<Contact> phoneContactList = new ArrayList<Contact>();
    static ContentResolver cr = null;
    private ArrayAdapter<Contact> listAdapter;
    private ListView mainListView ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the ListView resource.
        mainListView = (ListView) findViewById(R.id.mainListView);

        cr = getContentResolver();

        //Load Contacts
        getContactsToDisplay();

        //Set our custom array adapter as the ListView's adapter.
        listAdapter = new ContactArrayAdapter(this,phoneContactList);
        mainListView.setAdapter( listAdapter );

        //When item is tapped, toggle checked properties of CheckBox and Planet.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView<?> parent,View item,int position, long id)
            {
                //Start new Activity with name of contact and other details on it.
                Intent tempIntent = new Intent(MainActivity.this,ContactDetailActivity.class);
                tempIntent.putExtra("USER_NAME",phoneContactList.get(position).getName());
                startActivity(tempIntent);
            }
        });
    }

    //Method to get contacts
    @SuppressLint("NewApi") public void getContactsToDisplay()
    {
        phoneContactList.clear();
        String email = "";
        String formattedPhoneNumber = "";
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                //Getting Contact Name
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // Create and populate Contacts
                phoneContactList.add(new Contact(name,formattedPhoneNumber,email));
            }
        }

        //Closing all cursors
        cur.close();

        //Sort the contact list
        Collections.sort(phoneContactList, new CustomComparator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Holds child views for one row. */
    class ContactViewHolder
    {
        private CheckBox checkBox ;
        private TextView textView ;
        public ContactViewHolder() {}
        public ContactViewHolder(TextView textView, CheckBox checkBox)
        {
            this.checkBox = checkBox ;
            this.textView = textView ;
        }
        public CheckBox getCheckBox()
        {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox)
        {
            this.checkBox = checkBox;
        }
        public TextView getTextView()
        {
            return textView;
        }
        public void setTextView(TextView textView)
        {
            this.textView = textView;
        }
    }

    //Custom Comparator to sort contact list
    class CustomComparator implements Comparator<Contact>
    {
        @Override
        public int compare(Contact o1,Contact o2)
        {
            return o1.getName().compareTo(o2.getName());
        }
    }

    /** Custom adapter for displaying an array of Contact objects. */
    class ContactArrayAdapter extends ArrayAdapter<Contact>
    {
        private LayoutInflater inflater;

        public ContactArrayAdapter( Context context, List<Contact> contactList )
        {
            super(context, R.layout.rowbuttonlayout, R.id.label, contactList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // Contact to display
            Contact contactToDisplay = (Contact) this.getItem( position );

            // The child views in each row.
            CheckBox checkBox ;
            TextView textView ;

            // Create a new row view
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.rowbuttonlayout, null);

                // Find the child views.
                textView = (TextView) convertView.findViewById( R.id.label );
                checkBox = (CheckBox) convertView.findViewById( R.id.check );

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag( new ContactViewHolder(textView,checkBox) );
                checkBox.setClickable(false);
            }
            // Reuse existing row view
            else
            {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                ContactViewHolder viewHolder = (ContactViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag(contactToDisplay);

            // Display planet data
            checkBox.setChecked(contactToDisplay.isChecked() );
            textView.setText(contactToDisplay.getName());

            return convertView;
        }
    }
}
