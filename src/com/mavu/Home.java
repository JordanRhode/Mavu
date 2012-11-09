package com.mavu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Home extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_layout, menu);
        return true;
    }
}
