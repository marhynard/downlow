package com.fritzbang.downlow;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadTask extends AsyncTask<Object, Void, Void> {

	private static final String DEBUG_TAG = "DownloadTask";
	public static ArrayList<String> downloadQueue = new ArrayList<String>();
	public static ArrayList<String> downloadQueueEpisodeID = new ArrayList<String>();
	private final int MAX_NUMBER_DOWNLOADS = 1;

	DownloadTask() {

	}

	@Override
	protected Void doInBackground(Object... arg0) {
		int currentNumberRunning = 0;
		while (!downloadQueue.isEmpty()) {
			String fileToDownload = downloadQueue.remove(0);
			String episodeID = (String) downloadQueueEpisodeID.remove(0);

			Log.d(DEBUG_TAG, "downloadQueue size: " + downloadQueue.size()
					+ " " + fileToDownload);
			Context context = (Context) arg0[0];

			// TODO figure out the location and change the null to the file
			// location

			// TODO set up notifications to add the icon for downloading
			// TODO add a publishProgress() to show progress
			// TODO add a onProgressUpdate() to the UI in order to show progress
			// String episodeLink = DownloadManager.DownloadFromUrl(context
			// .getFilesDir().getPath(), fileToDownload);

			String episodeLink = DownloadManager.DownloadFromUrl(
					"/mnt/sdcard/com.fritzbang.downlow/", fileToDownload);
			// TODO need to insert the filename name into the database so we
			// know were it is located
			Log.d(DEBUG_TAG, "file: " + episodeLink + " " + episodeID);
			if (episodeLink != null) {
				DBAdapter db = new DBAdapter(context);
				db.open();
				db.updateEpisodeInfoDownload(episodeID, episodeLink, true);
				db.close();

			}

			// TODO Send the notification letting the user know that the item
			// has been downloaded
			/*
			 * Notification.Builder notificationBuilder = new
			 * Notification.Builder(mApplicationContext)
			 * .setTicker("New Tweets received", mContentView)
			 * .setContentTitle("New Tweets received")
			 * .setContentText("Tweets have been downloaded")
			 * .setSmallIcon(android.R.drawable.stat_sys_warning)
			 * .setAutoCancel(true) .setContentIntent(pendingIntent)
			 * .setContent(mContentView);
			 * 
			 * 
			 * 
			 * NotificationManager mNotificationManager =
			 * (NotificationManager)mApplicationContext
			 * .getSystemService(Context.NOTIFICATION_SERVICE);
			 * mNotificationManager
			 * .notify(MY_NOTIFICATION_ID,notificationBuilder.build());
			 */
		}
		// Log.d(DEBUG_TAG, "downloadQueue size: " + downloadQueue.size());
		return null;
	}
}
