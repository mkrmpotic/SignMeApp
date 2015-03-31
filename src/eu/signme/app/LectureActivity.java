package eu.signme.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import eu.signme.app.adapter.SignatureAdapter;
import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.GetSignaturesHandler;
import eu.signme.app.api.SignMeAPI.RequestSignHandler;
import eu.signme.app.api.SignMeAPI.SignUserHandler;
import eu.signme.app.api.response.GetSignaturesResponse;
import eu.signme.app.api.response.RequestSignResponse;
import eu.signme.app.api.response.SignUserResponse;
import eu.signme.app.model.Signature;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.ui.ActionBar.ActionBarListener;
import eu.signme.app.ui.swipe.RecyclerViewAdapter;
import eu.signme.app.ui.swipe.SwipeToDismissTouchListener;
import eu.signme.app.ui.swipe.SwipeableItemClickListener;
import eu.signme.app.ui.swipe.OnItemClickListener;
import eu.signme.app.util.Utils;

public class LectureActivity extends FragmentActivity implements
		ActionBarListener, OnClickListener {

	private ActionBar actionBar;
	private TextView txtName, txtDay;
	private Button btnSignMe;
	private RelativeLayout rlSignMe;
	private List<Signature> signatures = new ArrayList<Signature>();
	private int id, userId;
	private String lectureName, lectureDay, lectureHour;
	private SwipeRefreshLayout swipeRefreshLayout;

	private RecyclerView mRecyclerView;
	private SignatureAdapter adapter;
	
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture);

		userId = Utils.getIntFromPrefs("id");

		bindViews();
		
		mContext = getApplicationContext();

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
		
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_signatures);
		swipeRefreshLayout.setColorSchemeResources(R.color.signme_yellow, R.color.signme_red, R.color.signme_green);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getSignatures();
			}
		});

		/* Initialize recycler view */
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		rlSignMe = (RelativeLayout) findViewById(R.id.rl_sign_me);
		btnSignMe = (Button) findViewById(R.id.btn_sign_me);

		btnSignMe.setOnClickListener(this);

		adapter = new SignatureAdapter(LectureActivity.this, signatures, userId);
		mRecyclerView.setAdapter(adapter);

		final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
				new RecyclerViewAdapter(mRecyclerView),
				new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
					@Override
					public boolean canDismiss(int position) {
						Signature currentSignature = signatures.get(position);
						if (currentSignature.getStatus() == 0 && currentSignature.getIUserd() != userId)
							return true;
						else
							return false;
					}

					@Override
					public void onDismiss(RecyclerViewAdapter view, int position) {
						Signature currentSignature = signatures.get(position);
						signatures.remove(position);
						adapter.notifyDataSetChanged();
						SignMeAPI.signUser(currentSignature.getId(), new SignUserHandler() {
							
							@Override
							public void onSuccess(SignUserResponse response) {			
								getSignatures();
							}
							
							@Override
							public void onError(VolleyError error) {
								// TODO Auto-generated method stub
								
							}
						});
						
					}
				});
		mRecyclerView.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		mRecyclerView
				.setOnScrollListener((RecyclerView.OnScrollListener) touchListener
						.makeScrollListener());
		mRecyclerView.addOnItemTouchListener(new SwipeableItemClickListener(
				this, new OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						if (view.getId() == R.id.txt_signed) {
							touchListener.processPendingDismisses();
						} else { // R.id.txt_data
							Toast.makeText(LectureActivity.this,
									"Position " + position, Toast.LENGTH_SHORT)
									.show();
						}
					}
				}));

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
					swipeRefreshLayout.setVisibility(View.VISIBLE);
				} else {
					swipeRefreshLayout.setVisibility(View.INVISIBLE);
				}
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
		case R.id.btn_sign_me:
			SignMeAPI.requestSign(id, new RequestSignHandler() {

				@Override
				public void onSuccess(RequestSignResponse response) {
					rlSignMe.setVisibility(View.GONE);
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
