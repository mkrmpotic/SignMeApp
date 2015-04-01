package eu.signme.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

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
import eu.signme.app.ui.SuccessToast;
import eu.signme.app.util.NetworkUtil;
import eu.signme.app.util.Utils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class LecturesActivity extends SignMeActivity implements
		NewLectureDialogListener, ActionBarListener, OnClickListener {

	GoogleCloudMessaging gcm;
	String regid;
	String PROJECT_NUMBER = "513360053176";

	private ActionBar actionBar;
	private List<Lecture> lectures = new ArrayList<Lecture>();
	private NewLectureDialog editNameDialog;
	private SmoothProgressBar progressBar;
	private Button btnNewLecture;

	private RecyclerView mRecyclerView;
	private LectureAdapter adapter;

	private Context mContext;
	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lectures);

		bindViews();

		actionBar.setActionBarListener(this);
		actionBar.showPlusIcon();

		mContext = getApplicationContext();

		getRegId();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getLectures();
		actionBar.hideMenu();
		actionBar.setNameAndBeer(Utils.getStringFromPrefs("name"),
				Utils.getIntFromPrefs("beer"));
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
				if (NetworkUtil.getConnectivityStatus(mContext) != 0)
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
		progressBar = (SmoothProgressBar) findViewById(R.id.progress_bar);

		btnNewLecture = (Button) findViewById(R.id.btn_create_lecture);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_lectures);
		swipeRefreshLayout.setColorSchemeResources(R.color.signme_yellow,
				R.color.signme_red, R.color.signme_green);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getLectures();
			}
		});

		/* Initialize recycler view */
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		adapter = new LectureAdapter(LecturesActivity.this, lectures);
		mRecyclerView.setAdapter(adapter);

		btnNewLecture.setOnClickListener(this);
		adapter.setOnItemClickListener(new eu.signme.app.ui.swipe.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
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

		/*
		 * 
		 * lwLectures.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) {
		 * 
		 * Lecture chosenLecture = (Lecture) adapter.getItem(position); Intent
		 * intent = new Intent(LecturesActivity.this, LectureActivity.class);
		 * intent.putExtra("id", chosenLecture.getId());
		 * intent.putExtra("lectureName", chosenLecture.getName());
		 * intent.putExtra("lectureDay",
		 * Utils.getRelativeDay(chosenLecture.getDate()));
		 * intent.putExtra("lectureHour", chosenLecture.getRoundStartHour());
		 * startActivity(intent);
		 * 
		 * } });
		 */
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
		if (NetworkUtil.getConnectivityStatus(this) != 0)
			SignMeAPI.createLecture(name, start, end, today,
					new CreateLectureHandler() {

						@Override
						public void onSuccess(CreateLectureResponse response) {
							// TODO Auto-generated method stub
							editNameDialog.dismiss();
							SuccessToast toast = new SuccessToast(
									LecturesActivity.this,
									getResources().getString(
											R.string.lecture_created),
									Toast.LENGTH_LONG);
							toast.show();
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
		if (NetworkUtil.getConnectivityStatus(this) != 0)
			SignMeAPI.getLectures(new GetLecturesHandler() {

				@Override
				public void onSuccess(GetLecturesResponse response) {
					progressBar.setVisibility(View.GONE);
					lectures.clear();
					lectures.addAll(response.getLectures());
					adapter.notifyDataSetChanged();

					if (lectures.size() > 0)
						swipeRefreshLayout.setVisibility(View.VISIBLE);
					else
						swipeRefreshLayout.setVisibility(View.INVISIBLE);

					swipeRefreshLayout.setRefreshing(false);
				}

				@Override
				public void onError(VolleyError error) {

					swipeRefreshLayout.setRefreshing(false);

				}

			});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_create_lecture:
			showEditDialog();
			break;
		}
	}

}
