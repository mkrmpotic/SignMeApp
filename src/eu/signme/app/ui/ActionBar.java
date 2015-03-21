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
	
	ImageView imgIconMenu;
	TextView txtEmail;
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
		imgIconMenu = (ImageView) view.findViewById(R.id.icon_menu);
		rlMenu = (RelativeLayout) view.findViewById(R.id.rl_menu);
		txtEmail = (TextView) view.findViewById(R.id.txt_email);
		
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
		}
	}
	
	private void showMenu() {
		imgIconMenu.setBackgroundColor(getResources().getColor(R.color.signme_green));
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
	
	public void setEmail(String email) {
		txtEmail.setText(email);
	}

}
