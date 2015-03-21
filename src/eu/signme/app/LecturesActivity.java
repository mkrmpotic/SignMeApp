package eu.signme.app;

import eu.signme.app.ui.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LecturesActivity extends Activity {
	
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lectures);
		
		bindViews();
		
		String email = getIntent().getStringExtra("email");
		actionBar.setEmail(email);
	}
	
	private void bindViews() {
		actionBar = (ActionBar) findViewById(R.id.action_bar);
	}
}
