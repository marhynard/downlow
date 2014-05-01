package com.fritzbang.downlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainDownLowActivity extends FragmentActivity {

	private static final String DEBUG_TAG = "MainDownLowActivity";

	// TODO clean up code and put in comments
	// TODO remove auto-generated TODO statements

	// TODO check to see if storage location is available

	// TODO add ability to search for podcasts
	// TODO check out gpodder service for searching for podcasts

	// TODO create view for downloads

	// TODO create view to manage podcasts(subscribe import and find)

	// TODO create activity to clear the rss feed cache

	// TODO create a settings menu

	// TODO need to be able to update the views on database change

	// TODO remove all the debug log statements

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_view);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_down_low, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add:
			Log.d(DEBUG_TAG, "add_action");

			Intent addpodcastintent = new Intent();
			addpodcastintent.setClassName("com.fritzbang.downlow",
					"com.fritzbang.downlow.AddPodcastActivity");
			startActivity(addpodcastintent);

			return true;
		case R.id.action_refresh:
			Log.d(DEBUG_TAG, "refresh action");
			new FeedLinkInformationTask().execute(getApplicationContext());

			return true;
		case R.id.action_settings:
			Log.d(DEBUG_TAG, "settings action");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
