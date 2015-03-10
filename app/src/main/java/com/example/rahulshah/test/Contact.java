package com.example.rahulshah.test;

import java.io.Serializable;
import java.util.ArrayList;

public class Contact implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String mId;
	private String mName;
	private String mPhoneNumber;
	private String mEmail;
	private boolean mSelected;
	private boolean checked = false;
	private ArrayList<String> mListOfEmails = new ArrayList<String>();
	private ArrayList<String> mListOfPhones = new ArrayList<String>();

	public Contact()
	{

	}
	
	public Contact(String pId, String pName)
	{
		mId = pId;
		mName = pName;
	}
	
	public Contact(String pName, String pPhoneNumber, String pEmail)
	{
		mName = pName;
		mPhoneNumber = pPhoneNumber;
		mEmail = pEmail;
	}
	
	public Contact(String pName, ArrayList<String> pPhoneNumbers, ArrayList<String> pEmails)
	{
		mName = pName;
		mListOfPhones.addAll(pPhoneNumbers);
		mListOfEmails.addAll(pEmails);
	}
	
	public String getId()
	{
		return mId;
	}
	
	public void setId(String pId)
	{
		mId = pId;
	}
	
	public String getName()
	{
		return mName;
	}

	public void setName(String pName)
	{
		mName = pName;
	}

	public String getPhoneNumber()
	{
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String pPhoneNumber)
	{
		mPhoneNumber = pPhoneNumber;
	}
	
	public String getEmail()
	{
		return mEmail;
	}
	
	public void setEmail(String pEmail)
	{
		mEmail = pEmail;
	}
	
	public void setListOfEmails(ArrayList<String> emails)
	{
		mListOfEmails.clear();
		mListOfEmails.addAll(emails);
	}
	
	public ArrayList<String> getListOfEmails()
	{
		return mListOfEmails;
	}
	
	public void setListOfPhones(ArrayList<String> phones)
	{
		mListOfPhones.clear();
		mListOfPhones.addAll(phones);
	}
	
	public ArrayList<String> getListOfPhones()
	{
		return mListOfPhones;
	}
	
	public boolean isSelected() {
		
		return mSelected;
	}

	public void setSelected(boolean pSelected) {
		
		mSelected = pSelected;
	}
	
	@Override
    public String toString() 
	{
        return this.mName;
    }
	
	public boolean isChecked() 
	{
		return checked;
	}
	public void setChecked(boolean checked) 
	{
		this.checked = checked;
	}
	public void toggleChecked() 
	{
		checked = !checked ;
	}
}