package eu.signme.app;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import eu.signme.app.adapter.LectureAdapter;
import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.GetLecturesHandler;
import eu.signme.app.api.response.GetLecturesResponse;
import eu.signme.app.dialog.NewLectureDialog;
import eu.signme.app.dialog.NewLectureDialog.NewLectureDialogListener;
import eu.signme.app.model.Lecture;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.ui.ActionBar.ActionBarListener;
import eu.signme.app.util.Utils;

public class LecturesActivity extends FragmentActivity implements
		NewLectureDialogListener, ActionBarListener {

	private ActionBar actionBar;
	private ListView lwLectures;
	private LectureAdapter adapter;
	private List<Lecture> lectures = new ArrayList<Lecture>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lectures);

		bindViews();

		actionBar.setActionBarListener(this);
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

	private void showEditDialog() {
		FragmentManager fm = getSupportFragmentManager();
		NewLectureDialog editNameDialog = new NewLectureDialog();
		editNameDialog.show(fm, "dialog_new_lecture");
	}

	@Override
	public void onFinishEditDialog(String inputText) {
		Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMenuItemClicked(int itemId) {
		showEditDialog();
	}

}
