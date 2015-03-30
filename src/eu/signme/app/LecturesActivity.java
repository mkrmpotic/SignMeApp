package eu.signme.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import eu.signme.app.adapter.LectureAdapter;
import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.CreateLectureHandler;
import eu.signme.app.api.SignMeAPI.GetLecturesHandler;
import eu.signme.app.api.SignMeAPI.RegisterGcmHandler;
import eu.signme.app.api.response.CreateLectureResponse;
import eu.signme.app.api.response.GetLecturesResponse;
import eu.signme.app.api.response.RegisterGcmResponse;
import eu.signme.app.dialog.NewLectureDialog;
import eu.signme.app.dialog.NewLectureDialog.NewLectureDialogListener;
import eu.signme.app.model.Lecture;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.ui.ActionBar.ActionBarListener;
import eu.signme.app.util.Utils;

public class LecturesActivity extends FragmentActivity implements
		NewLectureDialogListener, ActionBarListener {

	GoogleCloudMessaging gcm;
	String regid;
	String PROJECT_NUMBER = "513360053176";

	private ActionBar actionBar;
	private ListView lwLectures;
	private LectureAdapter adapter;
	private List<Lecture> lectures = new ArrayList<Lecture>();
	private NewLectureDialog editNameDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lectures);

		bindViews();

		actionBar.setActionBarListener(this);
		actionBar.showPlusIcon();

		
		getRegId();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getLectures();
		actionBar.hideMenu();
		actionBar.setNameAndBeer(Utils.getStringFromPrefs("name"), Utils.getIntFromPrefs("beers"));
	}

	public void getRegId() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					regid = gcm.register(PROJECT_NUMBER);
					msg = "Device registered, registration ID=" + regid;
					Log.i("GCM", msg);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				SignMeAPI.registerGcm(regid, new RegisterGcmHandler() {

					@Override
					public void onSuccess(RegisterGcmResponse response) {
						// TODO Auto-generated method stub
						Log.i("MAROO", response.getDetail());
					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						Log.i("MAROO2", error.toString());
					}
				});
			}
		}.execute(null, null, null);
	}

	private void bindViews() {
		actionBar = (ActionBar) findViewById(R.id.action_bar);
		lwLectures = (ListView) findViewById(R.id.list);

		adapter = new LectureAdapter(this, lectures);
		lwLectures.setAdapter(adapter);

		lwLectures.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Lecture chosenLecture = (Lecture) adapter.getItem(position);
				Intent intent = new Intent(LecturesActivity.this,
						LectureActivity.class);
				intent.putExtra("id", chosenLecture.getId());
				intent.putExtra("lectureName", chosenLecture.getName());
				intent.putExtra("lectureDay",
						Utils.getRelativeDay(chosenLecture.getDate()));
				intent.putExtra("lectureHour",
						chosenLecture.getRoundStartHour());
				startActivity(intent);

			}
		});
	}

	private void showEditDialog() {
		FragmentManager fm = getSupportFragmentManager();
		editNameDialog = new NewLectureDialog();
		editNameDialog.show(fm, "dialog_new_lecture");
		editNameDialog.setNewLectureDialogListener(this);
	}

	@Override
	public void onFinishCreateLecture(String name, String start, String end,
			boolean today) {
		SignMeAPI.createLecture(name, start, end, today,
				new CreateLectureHandler() {

					@Override
					public void onSuccess(CreateLectureResponse response) {
						// TODO Auto-generated method stub
						editNameDialog.dismiss();
						getLectures();
					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						Log.i("22", error.toString());
					}
				});
	}

	@Override
	public void onMenuItemClicked(int itemId) {
		switch (itemId) {
		case ActionBar.ICON_LEFT:
			showEditDialog();
			break;
		case ActionBar.SETTINGS:
			Intent intent = new Intent(this, MyProfileActivity.class);
			startActivity(intent);
			break;
		case ActionBar.LOGOUT:
			Utils.clearPrefs();
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	private void getLectures() {
		SignMeAPI.getLectures(new GetLecturesHandler() {

			@Override
			public void onSuccess(GetLecturesResponse response) {
				lectures.clear();
				lectures.addAll(response.getLectures());
				adapter.notifyDataSetChanged();
				
				if (lectures.size() > 0)
					lwLectures.setVisibility(View.VISIBLE);
				else
					lwLectures.setVisibility(View.INVISIBLE);

			}

			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub

			}

		});
	}

}
