package com.fritzbang.downlow;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ChannelTabDefinition extends TabDefinition {

	private final int _tabTitleResourceId;
	private final int _tabTitleViewId;
	private final int _tabLayoutId;
	private final Fragment _fragment;

	public ChannelTabDefinition(int tabContentViewId, int tabLayoutId,
			int tabTitleResourceId, int tabTitleViewId, Fragment fragment) {
		super(tabContentViewId);

		_tabLayoutId = tabLayoutId;
		_tabTitleResourceId = tabTitleResourceId;
		_tabTitleViewId = tabTitleViewId;

		_fragment = fragment;

	}

	@Override
	public Fragment getFragment() {
		return _fragment;
	}

	@Override
	public View createTabView(LayoutInflater inflater, ViewGroup tabsView) {
		// we need to inflate the view based on the layout id specified when
		// this instance was created.
		View indicator = inflater.inflate(_tabLayoutId, tabsView, false);

		// set up the title of the tab. this will populate the text with the
		// string defined by the resource passed in when this instance was
		// created. the text will also be centered within the title control.
		TextView titleView = (TextView) indicator.findViewById(_tabTitleViewId);
		titleView.setText(_tabTitleResourceId);
		titleView.setGravity(Gravity.CENTER);

		// ensure the control we're inflating is layed out properly. this will
		// cause our tab titles to be placed evenly weighted across the top.
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.weight = 1;
		indicator.setLayoutParams(layoutParams);

		return indicator;
	}

}
