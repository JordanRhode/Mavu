package com.mavu;


import java.util.ArrayList;
import java.util.Vector;

import com.mavu.appcode.Account;
import com.mavu.appcode.LocalAccountsDataSource;

import android.R.array;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;

public class Settings extends PreferenceActivity {

	private LocalAccountsDataSource datasource;
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);
        
        /*datasource = new LocalAccountsDataSource(this);
        datasource.open();
        
         ArrayList<String> pAccounts = new ArrayList<String>();
        pAccounts = datasource.getAccountEmails();
        String[] possibleAccounts = new String[pAccounts.size()];
        possibleAccounts = pAccounts.toArray(possibleAccounts);

        
		@SuppressWarnings("deprecation")
		ListPreference lp = (ListPreference) findPreference("prefs_account_key");
        lp.setEntries(possibleAccounts);
        lp.setEntryValues(possibleAccounts); */
        
        
    }
    
}
