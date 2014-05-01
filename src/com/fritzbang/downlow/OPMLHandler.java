package com.fritzbang.downlow;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
//import android.util.Log;

public class OPMLHandler {
	// private static final String DEBUG_TAG = "OPMLHandler";
	private static final String ns = null;
	private ArrayList<String> URLs = new ArrayList<String>();

	public void parse(InputStream in) throws XmlPullParserException,
			IOException {
		// Log.d(DEBUG_TAG, "Starting to parse");
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			readOPML(parser);
		} finally {
			in.close();
		}
	}

	private void readOPML(XmlPullParser parser) throws XmlPullParserException,
			IOException {

		// Log.d(DEBUG_TAG, "Starting to parse opml");
		parser.require(XmlPullParser.START_TAG, ns, "opml");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for Item tag
			if (name.equals("body")) {
				readBody(parser);
				// Log.d(DEBUG_TAG, "found outline");
			} else {
				// Log.d(DEBUG_TAG, "name: " + name);
				skip(parser);
			}
		}
	}

	private void readBody(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, ns, "body");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Log.d(DEBUG_TAG, name);
			// Starts by looking for Item tag
			if (name.equals("outline")) {
				// Log.d(DEBUG_TAG, name);
				readOutline(parser);
			} else {
				skip(parser);
			}
		}

	}

	private void readOutline(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String[] linkInfo = new String[2];
		parser.require(XmlPullParser.START_TAG, ns, "outline");
		linkInfo[0] = parser.getAttributeValue(null, "text");
		// Log.d(DEBUG_TAG, "Starting to parse enclosure: " + linkInfo[0]);
		linkInfo[1] = parser.getAttributeValue(null, "xmlUrl");
		// Log.d(DEBUG_TAG, "Starting to parse enclosure: " + linkInfo[1]);
		URLs.add(linkInfo[1]);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, ns, "outline");

	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}

	}

	public ArrayList<String> getURLs() {
		return URLs;
	}

}
