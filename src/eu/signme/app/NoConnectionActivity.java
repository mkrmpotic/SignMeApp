package eu.signme.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import eu.signme.app.util.Fonts;
import eu.signme.app.util.NetworkUtil;

public class NoConnectionActivity extends Activity implements OnClickListener {

	private Button btnRetry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_connection);

		bindViews();
	}

	private void bindViews() {
		btnRetry = (Button) findViewById(R.id.btn_retry);
		
		TextView txtTitle= (TextView) findViewById(R.id.txt_title);
		TextView txtSubtitle= (TextView) findViewById(R.id.txt_subtitle);

		txtTitle.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_BOLD));
		txtSubtitle.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		
		btnRetry.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_retry:
			if(NetworkUtil.getConnectivityStatus(this) != 0)
				finish();
			break;
		}

	}
}
