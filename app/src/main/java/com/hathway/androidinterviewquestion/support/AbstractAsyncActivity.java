package com.hathway.androidinterviewquestion.support;

import android.support.v7.app.ActionBarActivity;

import com.hathway.androidinterviewquestion.MainApplication;

public abstract class AbstractAsyncActivity extends ActionBarActivity implements AsyncActivity {

	@Override
	public MainApplication getApplicationContext() {
		return (MainApplication) super.getApplicationContext();
	}
}
