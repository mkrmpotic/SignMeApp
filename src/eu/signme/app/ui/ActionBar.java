package eu.signme.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.signme.app.R;

public class ActionBar extends RelativeLayout implements OnClickListener {

	public interface ActionBarListener {
		void onMenuItemClicked(int itemId);
	}

	public static final int ICON_LEFT = 0;
	
	ActionBarListener actionBarListener = null;

	ImageView imgIconLeft, imgIconMenu;
	TextView txtName;
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
		txtName = (TextView) view.findViewById(R.id.txt_name);

		imgIconLeft.setOnClickListener(this);
		imgIconMenu.setOnClickListener(this);

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
		}
	}
	
	public void setActionBarListener(ActionBarListener listener) {
		actionBarListener = listener;
	}

	private void showMenu() {
		imgIconMenu.setBackgroundColor(getResources().getColor(
				R.color.signme_green));
		imgIconMenu.setImageResource(R.drawable.ic_menu_white);
		rlMenu.setVisibility(View.VISIBLE);
		menuIsShown = true;
	}

	private void hideMenu() {
		imgIconMenu.setBackgroundColor(getResources().getColor(R.color.white));
		imgIconMenu.setImageResource(R.drawable.ic_menu);
		rlMenu.setVisibility(View.INVISIBLE);
		menuIsShown = false;
	}

	public void setName(String name) {
		txtName.setText(name);
	}

	public void showPlusIcon() {
		imgIconLeft.setVisibility(View.VISIBLE);
	}

}
