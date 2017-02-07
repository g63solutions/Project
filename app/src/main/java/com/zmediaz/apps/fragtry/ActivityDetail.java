package com.zmediaz.apps.fragtry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

/**
 * Created by Computer on 2/4/2017.
 */




public class ActivityDetail extends AppCompatActivity {





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_detail);

        Toolbar collapsingToolbar =
                (Toolbar) findViewById(R.id.toolbar);

       setSupportActionBar(collapsingToolbar);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(FragmentDetail.DETAIL_URI, getIntent()
                            .getData());

            FragmentDetail fragment = new FragmentDetail();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();


        if (itemThatWasClickedId == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, Settings.class);
            startActivity(startSettingsActivity);
            return true;
        }
        // If you do NOT handle the menu click,
        // return super.onOptionsItemSelected to let Android handle the menu click
        return super.onOptionsItemSelected(item);
    }
}
