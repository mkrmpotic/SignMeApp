package eu.signme.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import eu.signme.app.R;

public class ActionBar extends RelativeLayout {

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
		
		addView(view);
	}

}
