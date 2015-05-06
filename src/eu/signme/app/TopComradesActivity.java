package eu.signme.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import eu.signme.app.adapter.ComradeAdapter;
import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.GetTopComradesHandler;
import eu.signme.app.api.response.GetTopComradesResponse;
import eu.signme.app.model.Comrade;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.ui.ActionBar.ActionBarListener;
import eu.signme.app.util.Fonts;
import eu.signme.app.util.NetworkUtil;
import eu.signme.app.util.Utils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class TopComradesActivity extends SignMeActivity implements
		ActionBarListener {

	private ActionBar actionBar;

	private List<Comrade> comrades = new ArrayList<Comrade>();
	private SwipeRefreshLayout swipeRefreshLayout;
	private SmoothProgressBar progressBar;

	private RecyclerView mRecyclerView;
	private ComradeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_comrades);

		bindViews();

		actionBar.setActionBarListener(this);
		actionBar.showBackIcon();

	}

	@Override
	protected void onResume() {
		super.onResume();
		getComrades();
		actionBar.hideMenu();
		actionBar.setBeer(Utils.getIntFromPrefs("beer"));
	}

	private void bindViews() {
		actionBar = (ActionBar) findViewById(R.id.action_bar);
		progressBar = (SmoothProgressBar) findViewById(R.id.progress_bar);

		TextView txtName = (TextView) findViewById(R.id.txt_name);

		txtName.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_MEDIUM));

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_signatures);
		swipeRefreshLayout.setColorSchemeResources(R.color.signme_yellow,
				R.color.signme_red, R.color.signme_green);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getComrades();
			}
		});

		/* Initialize recycler view */
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		adapter = new ComradeAdapter(TopComradesActivity.this, comrades);
		mRecyclerView.setAdapter(adapter);

	}

	@Override
	public void onMenuItemClicked(int itemId) {
		Intent intent;
		switch (itemId) {
		case ActionBar.ICON_LEFT:
			finish();
			break;
		case ActionBar.TOP_COMRADES:
			intent = new Intent(this, TopComradesActivity.class);
			startActivity(intent);
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

	private void getComrades() {
		if (NetworkUtil.getConnectivityStatus(this) != 0)
			SignMeAPI.getTopComrades(new GetTopComradesHandler() {

				@Override
				public void onSuccess(GetTopComradesResponse response) {

					progressBar.setVisibility(View.GONE);
					comrades.clear();
					comrades.addAll(response.getComrades());
					adapter.notifyDataSetChanged();

					if (comrades.size() > 0) {
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

}
