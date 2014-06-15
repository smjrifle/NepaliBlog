package com.vimvox.nepaliblog;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3),
								getString(R.string.title_section4),
								getString(R.string.title_section5),
								getString(R.string.title_section99),
								getString(R.string.title_section100) }), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		Context context;
		ListView lv;
		List<String> headlines;
		List<String> links;
		static String url_feed = "";
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			context = getActivity().getApplicationContext();
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			lv = (ListView) rootView.findViewById(R.id.list);
			if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				get_home_page(rootView);
				url_feed = "http://smjrifle.net/feed";
			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
				get_home_page(rootView);
				url_feed = "http://motorscript.com/feed/";
			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
				get_home_page(rootView);
				url_feed = "http://merosanokatha.com/feed";
			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
				get_home_page(rootView);
				url_feed = "http://shikhabahety.com/feed/";
			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 5) {
				get_home_page(rootView);
				url_feed = "http://ashaswot.com.np/b/feed/";
			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 6) {
				Intent intent = new Intent(getActivity().getBaseContext(),
						SendEmail.class);
				startActivity(intent);
			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 7) {
				Intent intent = new Intent(getActivity().getBaseContext(),
						About.class);
				startActivity(intent);
			}
			return rootView;
		}

		public void get_home_page(final View rootView) {

			if (isConnectingToInternet()) {
				lv.setVisibility(View.VISIBLE);
				TextView tv = (TextView) rootView
						.findViewById(R.id.noConnection);
				Button but = (Button) rootView.findViewById(R.id.retry);
				tv.setVisibility(View.GONE);
				but.setVisibility(View.GONE);
				new RetrieveFeedTask().execute();
			} else {
				lv.setVisibility(View.GONE);
				TextView tv = (TextView) rootView
						.findViewById(R.id.noConnection);
				Button but = (Button) rootView.findViewById(R.id.retry);
				tv.setVisibility(View.VISIBLE);
				but.setVisibility(View.VISIBLE);
				but.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						get_home_page(rootView);
					}
				});
			}
		}

		public class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {
			ProgressDialog pd;
			protected void onPreExecute() {
				headlines = new ArrayList<String>();
				links = new ArrayList<String>();
				pd = new ProgressDialog(getActivity());
				pd.setTitle(null);
				pd.setMessage("Fetching Feeds, Please Wait...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

			protected Void doInBackground(Void... params) {
				// Initializing instance variables

				try {
					URL url = new URL(url_feed);
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(false);
					XmlPullParser xpp = factory.newPullParser();

					// We will get the XML from an input stream
					xpp.setInput(getInputStream(url), "UTF_8");

					boolean insideItem = false;

					// Returns the type of current event: START_TAG, END_TAG,
					// etc..
					int eventType = xpp.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if (eventType == XmlPullParser.START_TAG) {

							if (xpp.getName().equalsIgnoreCase("item")) {
								insideItem = true;
							} else if (xpp.getName().equalsIgnoreCase("title")) {
								if (insideItem)
									headlines.add(xpp.nextText()); // extract
																	// the
																	// headline
							} else if (xpp.getName().equalsIgnoreCase("link")) {
								if (insideItem)
									links.add(xpp.nextText()); // extract the
																// link
																// of article
							}
						} else if (eventType == XmlPullParser.END_TAG
								&& xpp.getName().equalsIgnoreCase("item")) {
							insideItem = false;
						}

						eventType = xpp.next(); // move to next element
					}

				} catch (Exception e) {
					Log.d("url", e.toString());
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				pd.cancel();
				try {
					ListView lv = (ListView) getActivity().findViewById(
							R.id.list);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							context, R.layout.xml_feed, R.id.event_name,
							headlines);
					lv.setAdapter(adapter);
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							// Intent intent = new
							// Intent(Intent.ACTION_VIEW).setData(Uri
							// .parse((String) links.get(position)));
							// startActivity(intent);
							Intent intent = new Intent(getActivity()
									.getBaseContext(), ArticleRead.class);
							Bundle b = new Bundle();
							b.putString("url", (String) links.get(position)); // Your
																				// id
							intent.putExtras(b);
							startActivity(intent);
						}

					});
					adapter.notifyDataSetChanged();
				} catch (Exception e) {

				}
			}
		}

		public InputStream getInputStream(URL url) {
			try {
				return url.openConnection().getInputStream();
			} catch (IOException e) {
				return null;
			}
		}

		public boolean isConnectingToInternet() {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null)
					for (int i = 0; i < info.length; i++)
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}

			}
			return false;
		}

	}
}
