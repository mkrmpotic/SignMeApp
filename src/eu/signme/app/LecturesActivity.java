package eu.signme.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.VolleyError;

import eu.signme.app.adapter.LectureAdapter;
import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.GetLecturesHandler;
import eu.signme.app.api.response.GetLecturesResponse;
import eu.signme.app.model.Lecture;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.util.Utils;

public class LecturesActivity extends Activity {

	private ActionBar actionBar;
	private ListView lwLectures;
	private LectureAdapter adapter;
	private List<Lecture> lectures = new ArrayList<Lecture>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lectures);

		bindViews();
		
		actionBar.showPlusIcon();
		actionBar.setName(Utils.getFromPrefs("name"));

		SignMeAPI.getLectures(new GetLecturesHandler() {

			@Override
			public void onSuccess(GetLecturesResponse response) {
				lectures.clear();
				lectures.addAll(response.getLectures());
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub
				
			}

		
		});
	}

	private void bindViews() {
		actionBar = (ActionBar) findViewById(R.id.action_bar);
		lwLectures = (ListView) findViewById(R.id.list);

		adapter = new LectureAdapter(this, lectures);
		lwLectures.setAdapter(adapter);
	}
}
