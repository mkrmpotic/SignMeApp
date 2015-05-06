package eu.signme.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.signme.app.R;
import eu.signme.app.util.Fonts;

public class ActionBar extends RelativeLayout implements OnClickListener {

	public interface ActionBarListener {
		void onMenuItemClicked(int itemId);
	}

	public static final int ICON_LEFT = 0;
	public static final int SETTINGS = 1;
	public static final int LOGOUT = 2;
	public static final int TOP_COMRADES = 3;

	ActionBarListener actionBarListener = null;

	ImageView imgIconLeft, imgIconMenu;
	TextView txtBeerCount;
	RelativeLayout rlMenu;
	boolean menuIsShown = false;

	public ActionBar(Context context) {
		super(context);
		init(context);
	}

	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	protected void init(Context context) {
		View view = inflate(context, R.layout.element_action_bar, null);
		imgIconLeft = (ImageView) view.findViewById(R.id.icon_left);
		imgIconMenu = (ImageView) view.findViewById(R.id.icon_menu);
		rlMenu = (RelativeLayout) view.findViewById(R.id.rl_menu);
		txtBeerCount = (TextView) view.findViewById(R.id.txt_beer_count);
		Button btnSettings = (Button) view.findViewById(R.id.btn_settings);
		Button btnLogout = (Button) view.findViewById(R.id.btn_logout);
		Button btnTopComrades = (Button) view
				.findViewById(R.id.btn_top_comrades);

		txtBeerCount.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_BOLD));
		btnTopComrades.setTypeface(Fonts.getTypeface(context,
				Fonts.ROBOTO_LIGHT));
		btnSettings.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_LIGHT));
		btnLogout.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_LIGHT));

		rlMenu.setOnClickListener(this);
		imgIconMenu.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnTopComrades.setOnClickListener(this);

		addView(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon_menu:
			if (menuIsShown)
				hideMenu();
			else
				showMenu();
			break;
		case R.id.icon_left:
			actionBarListener.onMenuItemClicked(ICON_LEFT);
			break;
		case R.id.btn_settings:
			actionBarListener.onMenuItemClicked(SETTINGS);
			break;
		case R.id.btn_logout:
			actionBarListener.onMenuItemClicked(LOGOUT);
			break;
		case R.id.btn_top_comrades:
			actionBarListener.onMenuItemClicked(TOP_COMRADES);
			break;
		default:
			break;
		}
	}

	public void setActionBarListener(ActionBarListener listener) {
		actionBarListener = listener;
	}

	private void showMenu() {
		imgIconMenu.setBackgroundColor(getResources().getColor(
				R.color.dark_gray_text));
		imgIconMenu.setImageResource(R.drawable.ic_menu_white);
		rlMenu.setVisibility(View.VISIBLE);
		menuIsShown = true;
	}

	public void hideMenu() {
		imgIconMenu.setBackgroundColor(getResources().getColor(R.color.white));
		imgIconMenu.setImageResource(R.drawable.ic_menu);
		rlMenu.setVisibility(View.INVISIBLE);
		menuIsShown = false;
	}

	public void setBeer(int beer) {
		txtBeerCount.setText(Integer.toString(beer));
	}

	public void showPlusIcon() {
		imgIconLeft.setOnClickListener(this);
		imgIconLeft.setVisibility(View.VISIBLE);
	}

	public void showBackIcon() {
		imgIconLeft.setOnClickListener(this);
		imgIconLeft.setImageResource(R.drawable.ic_back);
		imgIconLeft.setVisibility(View.VISIBLE);
	}

}
