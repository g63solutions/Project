package com.zmediaz.apps.fragtry;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Computer on 2/4/2017.
 */


public class ActivityMain
        extends AppCompatActivity
        implements FragmentMain.Callback {

    private static final String FRAGMENTDETAIL_TAG = "FDTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0f);

        setContentView(R.layout.layout_activity_main);

        if (findViewById(R.id.detail_container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_container, new FragmentDetail(), FRAGMENTDETAIL_TAG)
                        .commit();

            }
        } else {
            mTwoPane = false;
        }

        /*//Set Some Special Layout
        FragmentMain fragmentMain = ((FragmentMain) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_main));

        fragmentMain.setSomeLayout(!mTwoPane);*/
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

    @Override
    public void onItemSelected(Uri columnId) {
        if (mTwoPane) {

            Bundle args = new Bundle();
            args.putParcelable(FragmentDetail.DETAIL_URI, columnId);

            FragmentDetail fragment = new FragmentDetail();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, FRAGMENTDETAIL_TAG)
                    .commit();

        }else{
            /*Intent movieDetailIntent = new Intent(this, DetailActivity.class);
            Uri uriForTitleClicked = MovieContract.MovieEntry.buildMovieUriWithID(columnId);
            movieDetailIntent.setData(uriForTitleClicked);
            startActivity(movieDetailIntent);*/

            Intent intent = new Intent(this, ActivityDetail.class)
                    .setData(columnId);
            startActivity(intent);
        }
    }
}
