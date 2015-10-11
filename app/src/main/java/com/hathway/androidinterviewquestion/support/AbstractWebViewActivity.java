package com.hathway.androidinterviewquestion.support;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.hathway.androidinterviewquestion.MainApplication;
import com.hathway.androidinterviewquestion.R;

public abstract class AbstractWebViewActivity extends Activity implements AsyncActivity {
	private Activity activity;
	private WebView webView;

	@Override
	public MainApplication getApplicationContext() {
		return (MainApplication) super.getApplicationContext();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
		this.webView = new WebView(this);
		setContentView(webView);
		this.activity = this;

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle("Loading...");
				activity.setProgress(progress * 100);
				if (progress == 100) {
					activity.setTitle(R.string.app_name);
				}
			}
		});
	}

	protected WebView getWebView() {
		return this.webView;
	}
}
