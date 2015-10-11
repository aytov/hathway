package com.hathway.androidinterviewquestion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.getinch.retrogram.Instagram;
import com.getinch.retrogram.Scope;
import com.hathway.androidinterviewquestion.support.AbstractWebViewActivity;

import java.net.URISyntaxException;
import retrofit.RestAdapter;

public class InstagramWebOAuthActivity extends AbstractWebViewActivity {
	private static final String TAG = InstagramWebOAuthActivity.class.getSimpleName();

	private ConnectionRepository connectionRepository;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWebView().getSettings().setJavaScriptEnabled(true);
		getWebView().setWebViewClient(new InstagramOAuthWebViewClient());

		this.connectionRepository = getApplicationContext().getConnectionRepository();
	}

	@Override
	public void onStart() {
		super.onStart();

		// display the Instagram authorization page
		getWebView().loadUrl(getAuthorizeUrl());
	}

	@Override
	protected void onResume() {
		super.onResume();

		// clear the Instagram session cookie
		CookieManager.getInstance().removeAllCookie();
	}

	private String getAuthorizeUrl() {
		String url = "";
		String redirectUri = getString(R.string.redirect_uri);
		String clientId = getString(R.string.instagram_id);

		try {
			url = Instagram.requestOAuthUrl(clientId, redirectUri, Scope.basic);
		} catch (URISyntaxException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}

		return url;
	}

	private void displayInstagramMenuOptions() {
		Intent intent = new Intent();
		intent.setClass(this, OptionsActivity.class);
		startActivity(intent);
		finish();
	}

	private class InstagramOAuthWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Uri uri = Uri.parse(url);
			Log.d(TAG, url);

			String accessGrant = createAccessGrantFromUriFragment(uri.getFragment());
			if (accessGrant != null) {
				new CreateConnectionTask().execute(accessGrant);
			}

			if (uri.getQueryParameter("error") != null) {
				CharSequence errorReason = uri.getQueryParameter("error_description").replace("+", " ");
				Toast.makeText(getApplicationContext(), errorReason, Toast.LENGTH_LONG).show();
				displayInstagramMenuOptions();
			}
		}

		private String createAccessGrantFromUriFragment(String uriFragment) {
			if (uriFragment != null && uriFragment.startsWith("access_token=")) {

				try {
					String[] params = uriFragment.split("&");
					String[] accessTokenParam = params[0].split("=");
					String accessToken = accessTokenParam[1];
					return accessToken;
				} catch (Exception e) {
					// don't do anything if the parameters are not what is expected
					Log.d(TAG, e.getLocalizedMessage(), e);
				}
			}
			return null;
		}
	}

	private class CreateConnectionTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			if (params[0] != null) {
				Instagram instagramService = new Instagram(params[0], RestAdapter.LogLevel.HEADERS_AND_ARGS);
				getApplicationContext().setInstagramService(instagramService);

				connectionRepository.setConnection(params[0]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			displayInstagramMenuOptions();
		}
	}
}
