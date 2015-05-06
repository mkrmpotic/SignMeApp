package eu.signme.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import eu.signme.app.util.Fonts;

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

		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		TextView txtSubtitle = (TextView) findViewById(R.id.txt_subtitle);

		txtTitle.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_BOLD));
		txtSubtitle.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));

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
