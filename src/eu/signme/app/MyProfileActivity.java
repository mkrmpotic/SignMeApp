package eu.signme.app;

import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import eu.signme.app.ui.SuccessToast;
import eu.signme.app.util.Fonts;
import eu.signme.app.util.NetworkUtil;
import eu.signme.app.util.Utils;

public class MyProfileActivity extends SignMeActivity implements
		ActionBarListener, OnClickListener, EditNameDialogListener,
		ChangePasswordListener {

	private ActionBar actionBar;
	private TextView txtName, txtBeer;
	private Button btnChangePass, btnEditName;
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

		txtName.setText(name);
		txtBeer.setText(Integer.toString(Utils.getIntFromPrefs("beer")));

	}

	@Override
	protected void onResume() {
		super.onResume();
		actionBar.hideMenu();
		actionBar.setBeer(Utils.getIntFromPrefs("beer"));
	}

	private void bindViews() {
		actionBar = (ActionBar) findViewById(R.id.action_bar);
		txtName = (TextView) findViewById(R.id.txt_name);
		btnEditName = (Button) findViewById(R.id.btn_edit_name);
		txtBeer = (TextView) findViewById(R.id.txt_beers);
		btnChangePass = (Button) findViewById(R.id.btn_change_password);

		TextView txtLabelName = (TextView) findViewById(R.id.txt_label_name);
		TextView txtLabelPassword = (TextView) findViewById(R.id.txt_label_password);
		TextView txtPassword = (TextView) findViewById(R.id.txt_password);
		TextView txtLabelBeer = (TextView) findViewById(R.id.txt_label_beer);

		txtLabelName.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtName.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtLabelPassword.setTypeface(Fonts
				.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtPassword.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtLabelBeer.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtBeer.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));

		btnEditName.setOnClickListener(this);
		btnChangePass.setOnClickListener(this);

	}

	@Override
	public void onMenuItemClicked(int itemId) {
		Intent intent;
		switch (itemId) {
		case ActionBar.ICON_LEFT:
			finish();
			break;
		case ActionBar.TOP_COMRADES:
			intent = new Intent(this, TopComradesActivity.class);
			startActivity(intent);
			break;
		case ActionBar.SETTINGS:
			intent = new Intent(this, MyProfileActivity.class);
			startActivity(intent);
			finish();
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
		if (NetworkUtil.getConnectivityStatus(this) != 0)
			SignMeAPI.changeName(name, new ChangeNameHandler() {

				@Override
				public void onSuccess(ChangeNameResponse response) {
					// TODO Auto-generated method stub
					String name = response.getName();
					Utils.saveToPrefs("name", name);
					txtName.setText(name);
					editNameDialog.dismiss();
					SuccessToast toast = new SuccessToast(
							MyProfileActivity.this, getResources().getString(
									R.string.name_changed), Toast.LENGTH_LONG);
					toast.show();
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
		if (NetworkUtil.getConnectivityStatus(this) != 0)
			SignMeAPI.changePassword(currentPassword, newPassword,
					new ChangePasswordHandler() {

						@Override
						public void onSuccess(ChangePasswordResponse response) {
							changePassDialog.dismiss();
							SuccessToast toast = new SuccessToast(
									MyProfileActivity.this, getResources()
											.getString(
													R.string.password_changed),
									Toast.LENGTH_LONG);
							toast.show();
						}

						@Override
						public void onError(VolleyError error) {

							try {
								String json = new String(
										error.networkResponse.data,
										HttpHeaderParser
												.parseCharset(error.networkResponse.headers));
								ErrorResponse errorObject = new Gson()
										.fromJson(json, ErrorResponse.class);

								int stringResource = getResources()
										.getIdentifier(
												"error_"
														+ Integer
																.toString(errorObject
																		.getStatus()),
												"string", getPackageName());
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
