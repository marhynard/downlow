package com.fritzbang.downlow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPodcastActivity extends Activity {
	private static final String DEBUG_TAG = "AddPodcastActivity";

	// TODO fix the view so it matches what the other views are like

	private Button channelButton;
	// private Button refreshButton;

	private Button subscribeButton;
	private Button opmlButton;
	private String opmlFile = "/mnt/sdcard/podkicker_backup.opml";

	String rssURL = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_podcast);

		subscribeButton = (Button) findViewById(R.id.buttonSubscribe);
		subscribeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText rssurlEditTextView = (EditText) findViewById(R.id.editTextRSSURL);
				// rssurlEditTextView.setText("http://192.168.56.101/songs.htm");
				rssURL = rssurlEditTextView.getText().toString();
				// rssURL = "http://feeds.feedburner.com/Coverville";
				// rssURL = "http://leo.am/podcasts/sn";
				// rssURL = "http://192.168.56.101/Coverville.htm";
				// rssURL = "http://192.168.56.101/songs.htm";
				boolean exists = checkDatabase(rssURL);
				boolean goodConnection = false;
				// Check to see if there is a network connection
				if (!exists) {
					goodConnection = NetworkHandler
							.checkNetworkConnection(getApplicationContext());
					if (goodConnection) {
						new FeedLinkValidationTask().execute(rssURL);

					} else {
						// If there is no connection let the user know
						// TODO add a popup to send the user to the settings to
						// enable it
						Log.d(DEBUG_TAG, "Check Network Connection");
						Toast.makeText(getApplicationContext(),
								"Check Network Connection", Toast.LENGTH_SHORT)
								.show();
					}
				}

			}
		});

		opmlButton = (Button) findViewById(R.id.buttonImport);
		opmlButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d(DEBUG_TAG, "Import button pressed load the opml file");
				// TODO add file chooser to load file

				// Log.d(DEBUG_TAG, "parse the OPML file");
				ArrayList<String> URLs = new ArrayList<String>();
				try {
					URLs = parseOPML(opmlFile);
				} catch (XmlPullParserException e) {
					// Invalid XML please check file
					Toast.makeText(getApplicationContext(),
							"Invalid XML please check file", Toast.LENGTH_SHORT)
							.show();
				} catch (IOException e) {
					// File not found Exception
					Toast.makeText(
							getApplicationContext(),
							"Error opening/reading OPML file please check location",
							Toast.LENGTH_SHORT).show();

				}

				boolean goodConnection = false;
				// Check to see if there is a network connection
				goodConnection = NetworkHandler
						.checkNetworkConnection(getApplicationContext());

				if (goodConnection) {
					Log.d(DEBUG_TAG, "Check the URLS");
					for (int x = 0; x < URLs.size(); x++) {
						String rssURL = (String) URLs.get(x);
						boolean exists = checkDatabase(rssURL);
						if (!exists) {
							new FeedLinkValidationTask().execute(rssURL);
						}
					}
				} else {
					// If there is no connection let the user know
					// TODO add a popup to send the user to the settings to
					// enable it
					Log.d(DEBUG_TAG, "Check Network Connection");
					Toast.makeText(getApplicationContext(),
							"Check Network Connection", Toast.LENGTH_SHORT)
							.show();
				}
			}

		});

		channelButton = (Button) findViewById(R.id.buttonChannels);
		channelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent channelintent = new Intent();
				channelintent.setClassName("com.fritzbang.downlow",
						"com.fritzbang.downlow.ChannelActivity");
				startActivity(channelintent);
			}

		});

		Button newButton = (Button) findViewById(R.id.buttonNew);
		newButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent newintent = new Intent();
				newintent.setClassName("com.fritzbang.downlow",
						"com.fritzbang.downlow.NewActivity");
				startActivity(newintent);
			}

		});

		// TODO add recommendations functionality
		// TODO add search functionality

	}

	protected ArrayList<String> parseOPML(String opmlFile2)
			throws XmlPullParserException, IOException {

		OPMLHandler opmlHandler = new OPMLHandler();
		InputStream in = new FileInputStream(opmlFile2);
		opmlHandler.parse(in);
		return opmlHandler.getURLs();
	}

	protected boolean checkDatabase(String rssURL2) {
		DBAdapter db = new DBAdapter(getApplicationContext());
		db.open();
		boolean urlExists = db.verifyURL(rssURL2);
		db.close();
		if (urlExists)
			Log.d(DEBUG_TAG, "The feed exists in the database");
		else
			Log.d(DEBUG_TAG, "The feed does not exists in the database");
		return urlExists;
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

	private class FeedLinkValidationTask extends
			AsyncTask<String, Void, String> {
		private int response = -1;
		private String urlString;

		@Override
		protected String doInBackground(String... urls) {
			try {
				return verifyUrl(urls[0]);
			} catch (IOException e) {
				return "Unable to retrieve.  Feed Link may be invalid.";
			}
		}

		// Given a URL, establishes an HttpUrlConnection and retrieves
		// the web page content as a InputStream, which it returns as
		// a string.
		private String verifyUrl(String urlString) throws IOException {
			this.urlString = urlString;
			URL url = new URL(this.urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			response = conn.getResponseCode();
			Log.d(DEBUG_TAG, "The response is: " + response);

			return "URL is good: " + response;
		}

		protected void onPostExecute(String result) {

			if (response == 200) {
				// If the page exists the url will be added to the db
				DBAdapter db = new DBAdapter(getApplicationContext());
				db.open();
				db.insertRssLink(urlString, null, null, null, null, null);
				db.close();
				Log.d(DEBUG_TAG, "The feed has been added");
				Toast.makeText(getApplicationContext(),
						urlString + " has been added", Toast.LENGTH_SHORT)
						.show();
			} else {
				Log.d(DEBUG_TAG, urlString + " doesn't exist: " + response);
				Toast.makeText(getApplicationContext(),
						urlString + " doesn't exist: " + response,
						Toast.LENGTH_SHORT).show();
			}

		}

	}
}
