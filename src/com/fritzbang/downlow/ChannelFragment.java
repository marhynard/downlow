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

public class ChannelFragment extends ListFragment {
	private static final String DEBUG_TAG = "ChannelFragment";

	private String selectedRowId = "-1";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO add the refresh and all the other buttons to the channel
		// activity

		// TODO refresh the view after deleting podcast

		// TODO change getActivity() and use onAttach if needed

		// TODO add this to a thread or task so it is not on the ui thread. use
		// the on result
		ArrayList<String> podcastTitles = new ArrayList<String>();
		ArrayList<String> imageNames = new ArrayList<String>();
		ArrayList<String> rssIDs = new ArrayList<String>();

		DBAdapter db = new DBAdapter(getActivity());
		db.open();
		Cursor cs = db.getAllRSSLinks();
		Log.d(DEBUG_TAG, "Count: " + cs.getCount());

		cs.moveToFirst();
		while (!cs.isAfterLast()) {

			String pcTitle = cs.getString(cs
					.getColumnIndex(DBAdapter.KEY_PODCAST_TITLE));
			String imageLink = cs.getString(cs
					.getColumnIndex(DBAdapter.KEY_LOCAL_IMAGE_LINK));
			String rssLink = cs.getString(cs
					.getColumnIndex(DBAdapter.KEY_RSS_LINK));
			String rssID = cs
					.getString(cs.getColumnIndex(DBAdapter.KEY_RSS_ID));

			if (pcTitle == null) {
				podcastTitles.add(rssLink);
			} else {
				podcastTitles.add(pcTitle);
			}

			imageNames.add(imageLink);
			rssIDs.add(rssID);
			cs.moveToNext();
		}

		db.close();

		setListAdapter(new ChannelArrayAdapter(getActivity(),
				podcastTitles.toArray(new String[podcastTitles.size()]),
				imageNames.toArray(new String[podcastTitles.size()]),
				rssIDs.toArray(new String[podcastTitles.size()])));

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Log.d(DEBUG_TAG, "Item is long clicked:" + id + " " + position);
				View parentView = (View) view.getParent();
				String textview1 = ((TextView) parentView
						.findViewById(R.id.podcast_title)).getText().toString();
				String rowid = (String) ((TextView) parentView
						.findViewById(R.id.podcast_title)).getTag();
				Log.d(DEBUG_TAG, "Title: " + textview1 + " : " + rowid);
				selectedRowId = rowid;
				showPodcastAlert();

				return true;
			}
		});
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(DEBUG_TAG, "Item is clicked:" + id + " " + position);
				// TODO start activity that lists the episodes for this channel

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void showPodcastAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Podcast Dialog(find better name)");
		alertDialog.setMessage("How can I help you?");

		alertDialog.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.d(DEBUG_TAG, "Call delete activity");
						new DeleteChannelTask().execute(getActivity(),
								selectedRowId);
					}
				});
		alertDialog.setNeutralButton("Info",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Intent intent = new Intent();
						// intent.setClassName("com.fritzbang.ridemanager",
						// "com.fritzbang.ridemanager.SaveTrackActivity");
						// startActivity(intent);
						// TODO create a podcast Link info page
						Log.d(DEBUG_TAG, "Show info");
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
