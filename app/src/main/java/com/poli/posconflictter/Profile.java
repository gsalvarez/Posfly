package com.poli.posconflictter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by iGabetoPC on 21/08/2017.
 */

public class Profile  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.barra_menu);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event:
                setContentView(R.layout.fragment_event);
                return true;

            case R.id.museum:
                setContentView(R.layout.fragment_museum);
                return true;

            case R.id.profile:
                setContentView(R.layout.fragment_profile);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
