package eu.signme.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
