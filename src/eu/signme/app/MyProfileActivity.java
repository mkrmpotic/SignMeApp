package eu.signme.app;

import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.ChangeNameHandler;
import eu.signme.app.api.SignMeAPI.ChangePasswordHandler;
import eu.signme.app.api.response.ChangeNameResponse;
import eu.signme.app.api.response.ChangePasswordResponse;
import eu.signme.app.api.response.ErrorResponse;
import eu.signme.app.dialog.ChangePasswordDialog;
import eu.signme.app.dialog.ChangePasswordDialog.ChangePasswordListener;
import eu.signme.app.dialog.EditNameDialog;
import eu.signme.app.dialog.EditNameDialog.EditNameDialogListener;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.ui.ActionBar.ActionBarListener;
import eu.signme.app.util.Utils;

public class MyProfileActivity extends FragmentActivity implements
		ActionBarListener, OnClickListener, EditNameDialogListener,
		ChangePasswordListener {

	private ActionBar actionBar;
	private ImageView imgSignature;
	private TextView txtName, txtBeers;
	private ImageButton btnEditName;
	private Button btnChangePass;
	private EditNameDialog editNameDialog;
	private ChangePasswordDialog changePassDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);

		bindViews();

		String name = Utils.getStringFromPrefs("name");

		actionBar.setActionBarListener(this);
		actionBar.showBackIcon();

		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_back);
		RoundedBitmapDrawable img = RoundedBitmapDrawableFactory.create(
				getResources(), bm);
		img.setCornerRadius(80.0f);

		imgSignature.setImageDrawable(img);
		txtName.setText(name);
		txtBeers.setText(Integer.toString(Utils.getIntFromPrefs("beers")));

	}

	@Override
	protected void onResume() {
		super.onResume();
		actionBar.hideMenu();
		actionBar.setNameAndBeer(Utils.getStringFromPrefs("name"),
				Utils.getIntFromPrefs("beers"));
	}

	private void bindViews() {
		actionBar = (ActionBar) findViewById(R.id.action_bar);
		imgSignature = (ImageView) findViewById(R.id.img_signature);
		txtName = (TextView) findViewById(R.id.txt_name);
		btnEditName = (ImageButton) findViewById(R.id.btn_edit_name);
		txtBeers = (TextView) findViewById(R.id.txt_beers);
		btnChangePass = (Button) findViewById(R.id.btn_change_password);

		btnEditName.setOnClickListener(this);
		btnChangePass.setOnClickListener(this);

	}

	@Override
	public void onMenuItemClicked(int itemId) {
		switch (itemId) {
		case ActionBar.ICON_LEFT:
			finish();
			break;
		case ActionBar.SETTINGS:
			Intent intent = new Intent(this, MyProfileActivity.class);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_name:
			showEditNameDialog();
			break;
		case R.id.btn_change_password:
			showChangePasswordDialog();
			break;
		}

	}

	private void showEditNameDialog() {
		FragmentManager fm = getSupportFragmentManager();
		editNameDialog = new EditNameDialog(Utils.getStringFromPrefs("name"));
		editNameDialog.show(fm, "dialog_edit_name");
		editNameDialog.setEditNameDialogListener(this);
	}

	private void showChangePasswordDialog() {
		FragmentManager fm = getSupportFragmentManager();
		changePassDialog = new ChangePasswordDialog();
		changePassDialog.show(fm, "dialog_change_password");
		changePassDialog.setChangePasswordListener(this);
	}

	@Override
	public void onFinishEditName(String name) {

		SignMeAPI.changeName(name, new ChangeNameHandler() {

			@Override
			public void onSuccess(ChangeNameResponse response) {
				// TODO Auto-generated method stub
				String name = response.getName();
				Utils.saveToPrefs("name", name);
				txtName.setText(name);
				actionBar.setName(name);
				editNameDialog.dismiss();
			}

			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onFinishChangePasswordDialog(String currentPassword,
			String newPassword) {
		SignMeAPI.changePassword(currentPassword, newPassword,
				new ChangePasswordHandler() {

					@Override
					public void onSuccess(ChangePasswordResponse response) {
						changePassDialog.dismiss();

					}

					@Override
					public void onError(VolleyError error) {

						try {
							String json = new String(
									error.networkResponse.data,
									HttpHeaderParser
											.parseCharset(error.networkResponse.headers));
							ErrorResponse errorObject = new Gson().fromJson(
									json, ErrorResponse.class);

							int stringResource = getResources().getIdentifier(
									"error_"
											+ Integer.toString(errorObject
													.getStatus()), "string",
									getPackageName());
							changePassDialog
									.setChangePasswordError(stringResource);

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}
}
