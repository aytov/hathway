package com.hathway.androidinterviewquestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hathway.androidinterviewquestion.support.AbstractAsyncActivity;

public class OptionsActivity extends AbstractAsyncActivity {
	private ConnectionRepository connectionRepository;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.connectionRepository = getApplicationContext().getConnectionRepository();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isConnected()) {
			showInstagramOptions();
		} else {
			showConnectOption();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private boolean isConnected() {
		return connectionRepository.getConnection() != null;
	}

	private void disconnect() {
		this.connectionRepository.removeConnection();
	}

	private void showConnectOption() {
		String[] options = { "Connect" };
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
		ListView listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				switch (position) {
				case 0:
					displayInstagramAuthorization();
					break;
				default:
					break;
				}
			}
		});
	}

	private void showInstagramOptions() {
		String[] options = { "Disconnect", "Popular Feed" };
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
		ListView listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				Intent intent;
				switch (position) {
				case 0:
					disconnect();
					showConnectOption();
					break;
				case 1:
					intent = new Intent();
					intent.setClass(parentView.getContext(), InstagramActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	private void displayInstagramAuthorization() {
		Intent intent = new Intent();
		intent.setClass(this, InstagramWebOAuthActivity.class);
		startActivity(intent);
		finish();
	}
}
