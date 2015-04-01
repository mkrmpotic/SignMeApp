package eu.signme.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AccountActivatedActivity extends SignMeActivity implements
		OnClickListener {

	private Button btnContinue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_activated);

		bindViews();
	}

	private void bindViews() {
		btnContinue = (Button) findViewById(R.id.btn_continue);
		
		btnContinue.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_continue:

			Intent intent = new Intent(AccountActivatedActivity.this,
					LecturesActivity.class);
			startActivity(intent);
			finish();

			break;
		}
	}
}
