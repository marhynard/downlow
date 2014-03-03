package com.fritzbang.downlow;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class DownloadFragment extends ListFragment {

	private static final String DEBUG_TAG = "DownloadFragment";

	private String selectedEpisodeId = "-1";
	private String selectedEpisodeLink = "-1";

	// TODO display the space available and location where the files are
	// TODO be able to update the view when a change has occured
	// TODO Delete the files that have been downloaded

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayList<String> podcastTitles = new ArrayList<String>();
		ArrayList<String> episodeTitle = new ArrayList<String>();
		ArrayList<String> downloaded = new ArrayList<String>();
		ArrayList<String> listened = new ArrayList<String>();
		ArrayList<String> episodeIDs = new ArrayList<String>();
		ArrayList<String> rssIDs = new ArrayList<String>();
		ArrayList<String> episodeLinks = new ArrayList<String>();
		ArrayList<String> lengths = new ArrayList<String>();
		ArrayList<String> locations = new ArrayList<String>();

		Log.d(DEBUG_TAG, "This is in the DownloadActivity");

		DBAdapter db = new DBAdapter(getActivity());
		db.open();
		Cursor cs = db.getAllPodcastsInfo();

		Log.d(DEBUG_TAG, "Count: " + cs.getCount());

		cs.moveToFirst();
		// loop through the cursor
		while (!cs.isAfterLast()) {

			// add the episode only if it has been downloaded
			Log.d(DEBUG_TAG,
					"KEY_DOWNLOADED: "
							+ cs.getString(cs
									.getColumnIndex(DBAdapter.KEY_DOWNLOADED)));
			if (cs.getString(cs.getColumnIndex(DBAdapter.KEY_DOWNLOADED))
					.equals("1")) {
				podcastTitles.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_PODCAST_TITLE)));
				episodeTitle.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_EPISODE_TITLE)));
				downloaded.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_DOWNLOADED)));
				listened.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_LISTENED)));
				episodeIDs.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_EPISODE_ID)));
				rssIDs.add(cs.getString(cs.getColumnIndex(DBAdapter.KEY_RSS_ID)));
				episodeLinks.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_EPISODE_LINK)));
				lengths.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_LENGTH)));
				locations.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_LOCATION)));
			}
			cs.moveToNext();
		}

		db.close();

		setListAdapter(new DownloadArrayAdapter(getActivity(),
				episodeTitle.toArray(new String[podcastTitles.size()]),
				podcastTitles.toArray(new String[podcastTitles.size()]),
				rssIDs.toArray(new String[rssIDs.size()]),
				episodeLinks.toArray(new String[episodeLinks.size()]),
				downloaded.toArray(new String[podcastTitles.size()]),
				listened.toArray(new String[podcastTitles.size()]),
				episodeIDs.toArray(new String[podcastTitles.size()])));

		this.getListView().setLongClickable(true);
		this.getListView().setClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {

						Log.d(DEBUG_TAG, "Item is long clicked:" + id + " "
								+ position);
						// View parentView = (View) view.getParent();
						String textview1 = ((TextView) view
								.findViewById(R.id.podcast_episode_title))
								.getText().toString();
						String episodeid = (String) ((TextView) view
								.findViewById(R.id.podcast_episode_title))
								.getTag(R.id.TAG_EPISODE_ID);
						String episodeLink = (String) ((TextView) view
								.findViewById(R.id.podcast_episode_title))
								.getTag(R.id.TAG_EPISODE_LINK_ID);
						Log.d(DEBUG_TAG, "Title: " + textview1 + " : "
								+ episodeid + " : " + episodeLink);
						selectedEpisodeId = episodeid;
						selectedEpisodeLink = episodeLink;
						showPodcastAlert();

						return true;
					}
				});
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(DEBUG_TAG, "Item is clicked:" + id + " " + position);
				String inPlaylist = ((TextView) view
						.findViewById(R.id.in_playlist)).getText().toString();

				DBAdapter db = new DBAdapter(getActivity()
						.getApplicationContext());
				db.open();

				if (inPlaylist.equals("")) {
					((TextView) view.findViewById(R.id.in_playlist))
							.setText("in Playlist");

					String episodeid = (String) ((TextView) view
							.findViewById(R.id.podcast_episode_title))
							.getTag(R.id.TAG_EPISODE_ID);

					Log.d(DEBUG_TAG, "episodeID: " + episodeid);
					db.insertPlaylistEntry(null, null, episodeid);

				} else {
					((TextView) view.findViewById(R.id.in_playlist))
							.setText("");
					db.deletePlaylistEntry();
				}
				db.close();
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onResume() {
		super.onResume();

		ArrayList<String> podcastTitles = new ArrayList<String>();
		ArrayList<String> episodeTitle = new ArrayList<String>();
		ArrayList<String> downloaded = new ArrayList<String>();
		ArrayList<String> listened = new ArrayList<String>();
		ArrayList<String> episodeIDs = new ArrayList<String>();
		ArrayList<String> rssIDs = new ArrayList<String>();
		ArrayList<String> episodeLinks = new ArrayList<String>();
		ArrayList<String> lengths = new ArrayList<String>();
		ArrayList<String> locations = new ArrayList<String>();

		Log.d(DEBUG_TAG, "This is in the DownloadActivity");

		DBAdapter db = new DBAdapter(getActivity());
		db.open();
		Cursor cs = db.getAllPodcastsInfo();

		Log.d(DEBUG_TAG, "Count: " + cs.getCount());

		cs.moveToFirst();
		// loop through the cursor
		while (!cs.isAfterLast()) {

			// add the episode only if it has been downloaded
			Log.d(DEBUG_TAG,
					"KEY_DOWNLOADED: "
							+ cs.getString(cs
									.getColumnIndex(DBAdapter.KEY_DOWNLOADED)));
			if (cs.getString(cs.getColumnIndex(DBAdapter.KEY_DOWNLOADED))
					.equals("1")) {
				podcastTitles.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_PODCAST_TITLE)));
				episodeTitle.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_EPISODE_TITLE)));
				downloaded.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_DOWNLOADED)));
				listened.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_LISTENED)));
				episodeIDs.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_EPISODE_ID)));
				rssIDs.add(cs.getString(cs.getColumnIndex(DBAdapter.KEY_RSS_ID)));
				episodeLinks.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_EPISODE_LINK)));
				lengths.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_LENGTH)));
				locations.add(cs.getString(cs
						.getColumnIndex(DBAdapter.KEY_LOCATION)));
			}
			cs.moveToNext();
		}

		db.close();

		setListAdapter(new DownloadArrayAdapter(getActivity(),
				episodeTitle.toArray(new String[podcastTitles.size()]),
				podcastTitles.toArray(new String[podcastTitles.size()]),
				rssIDs.toArray(new String[rssIDs.size()]),
				episodeLinks.toArray(new String[episodeLinks.size()]),
				downloaded.toArray(new String[podcastTitles.size()]),
				listened.toArray(new String[podcastTitles.size()]),
				episodeIDs.toArray(new String[podcastTitles.size()])));

		this.getListView().setLongClickable(true);
		this.getListView().setClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {

						Log.d(DEBUG_TAG, "Item is long clicked:" + id + " "
								+ position);
						// View parentView = (View) view.getParent();
						String textview1 = ((TextView) view
								.findViewById(R.id.podcast_episode_title))
								.getText().toString();
						String episodeid = (String) ((TextView) view
								.findViewById(R.id.podcast_episode_title))
								.getTag(R.id.TAG_EPISODE_ID);
						String episodeLink = (String) ((TextView) view
								.findViewById(R.id.podcast_episode_title))
								.getTag(R.id.TAG_EPISODE_LINK_ID);
						Log.d(DEBUG_TAG, "Title: " + textview1 + " : "
								+ episodeid + " : " + episodeLink);
						selectedEpisodeId = episodeid;
						selectedEpisodeLink = episodeLink;
						showPodcastAlert();

						return true;
					}
				});
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(DEBUG_TAG, "Item is clicked:" + id + " " + position);
				String inPlaylist = ((TextView) view
						.findViewById(R.id.in_playlist)).getText().toString();

				DBAdapter db = new DBAdapter(getActivity()
						.getApplicationContext());
				db.open();

				if (inPlaylist.equals("")) {
					((TextView) view.findViewById(R.id.in_playlist))
							.setText("in Playlist");

					String episodeid = (String) ((TextView) view
							.findViewById(R.id.podcast_episode_title))
							.getTag(R.id.TAG_EPISODE_ID);

					Log.d(DEBUG_TAG, "episodeID: " + episodeid);
					db.insertPlaylistEntry(null, null, episodeid);

				} else {
					((TextView) view.findViewById(R.id.in_playlist))
							.setText("");
					db.deletePlaylistEntry();
				}
				db.close();
			}
		});
	}

	public void showPodcastAlert() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Podcast Dialog(find better name)");
		alertDialog.setMessage("How can I help you?");

		alertDialog.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.d(DEBUG_TAG, "Delete the episode");

						dialog.cancel();
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.d(DEBUG_TAG, "Do nothing");
						dialog.cancel();

					}
				});

		alertDialog.show();
	}

}
