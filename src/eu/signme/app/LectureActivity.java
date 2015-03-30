package eu.signme.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import eu.signme.app.adapter.SignatureAdapter;
import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.GetSignaturesHandler;
import eu.signme.app.api.SignMeAPI.RequestSignHandler;
import eu.signme.app.api.response.GetSignaturesResponse;
import eu.signme.app.api.response.RequestSignResponse;
import eu.signme.app.model.Signature;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.ui.ActionBar.ActionBarListener;
import eu.signme.app.util.Utils;

public class LectureActivity extends FragmentActivity implements
		ActionBarListener, OnClickListener {

	private ActionBar actionBar;
	private ListView lwSignatures;
	private TextView txtName, txtDay;
	private Button btnSignMe;
	private RelativeLayout rlSignMe;
	private SignatureAdapter adapter;
	private List<Signature> signatures = new ArrayList<Signature>();
	private int id, userId;
	private String lectureName, lectureDay, lectureHour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture);
		
		userId = Utils.getIntFromPrefs("id");

		bindViews();

		actionBar.setActionBarListener(this);
		actionBar.showBackIcon();

		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		lectureName = intent.getStringExtra("lectureName");
		lectureDay = intent.getStringExtra("lectureDay");
		lectureHour = intent.getStringExtra("lectureHour");

		String strDayFormat = getResources().getString(R.string.starts_at);
		String strDayMsg = String.format(strDayFormat, lectureDay, lectureHour);

		txtName.setText(lectureName);
		txtDay.setText(strDayMsg);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSignatures();
		actionBar.hideMenu();
		actionBar.setNameAndBeer(Utils.getStringFromPrefs("name"),
				Utils.getIntFromPrefs("beers"));
	}

	private void bindViews() {
		actionBar = (ActionBar) findViewById(R.id.action_bar);
		txtName = (TextView) findViewById(R.id.txt_name);
		txtDay = (TextView) findViewById(R.id.txt_day);
		lwSignatures = (ListView) findViewById(R.id.list);
		rlSignMe = (RelativeLayout) findViewById(R.id.rl_sign_me);
		btnSignMe = (Button) findViewById(R.id.btn_sign_me);

		btnSignMe.setOnClickListener(this);
		adapter = new SignatureAdapter(this, signatures, userId);
		lwSignatures.setAdapter(adapter);

	}

	@Override
	public void onMenuItemClicked(int itemId) {
		Intent intent;
		switch (itemId) {
		case ActionBar.ICON_LEFT:
			finish();
			break;
		case ActionBar.SETTINGS:
			intent = new Intent(this, MyProfileActivity.class);
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

	private void getSignatures() {
		SignMeAPI.getSignatures(id, new GetSignaturesHandler() {

			@Override
			public void onSuccess(GetSignaturesResponse response) {
				signatures.clear();
				signatures.addAll(response.getSignatures());
				adapter.notifyDataSetChanged();
				
				if (!signatures.contains(new Signature(userId))) {
					rlSignMe.setVisibility(View.VISIBLE);
				}

				if (signatures.size() > 0) {
					lwSignatures.setVisibility(View.VISIBLE);
				} else {
					lwSignatures.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sign_me:
			SignMeAPI.requestSign(id, new RequestSignHandler() {
				
				@Override
				public void onSuccess(RequestSignResponse response) {
					rlSignMe.setVisibility(View.INVISIBLE);
					getSignatures();	
				}
				
				@Override
				public void onError(VolleyError error) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		}

	}

}
