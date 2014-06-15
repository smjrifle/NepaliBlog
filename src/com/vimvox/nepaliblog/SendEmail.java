package com.vimvox.nepaliblog;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendEmail extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_send_email);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.send_email, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void onBackPressed() {
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		startActivity(intent);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		static EditText Dname, Durl;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_send_email,
					container, false);
			Dname = (EditText) rootView.findViewById(R.id.donatorName);
			Durl = (EditText) rootView.findViewById(R.id.donatedAmount);
			Button email = (Button) rootView.findViewById(R.id.click_button);
			email.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Dname.getText().length() < 3) {
						Toast.makeText(getActivity(), "Please enter Blog name", Toast.LENGTH_SHORT).show();
						return;
					}
					if(Durl.getText().length() < 5) {
						Toast.makeText(getActivity(), "Please enter the Blog url", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					String[] recipients = new String[]{"vimvoxlab@gmail.com", "",};
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Add a new Blog");
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello vimvoxlab,\r\nI want to add " + Dname.getText() + " to the list, here is the URL " + Durl.getText() + ". Thank You.\r\nKeep Rocking,\nCheers");
					emailIntent.setType("text/plain");
					try {
						startActivity(Intent.createChooser(emailIntent, "Choose your email Client :"));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
					}
					
				}
				
			});
			return rootView;
		}
	}

}
