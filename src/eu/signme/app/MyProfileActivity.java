package eu.signme.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.ChangeImageHandler;
import eu.signme.app.api.SignMeAPI.ChangeNameHandler;
import eu.signme.app.api.SignMeAPI.ChangePasswordHandler;
import eu.signme.app.api.SignMeAPI.GetImageHandler;
import eu.signme.app.api.SignMeAPI.GetUserImagePathHandler;
import eu.signme.app.api.response.ChangeImageResponse;
import eu.signme.app.api.response.ChangeNameResponse;
import eu.signme.app.api.response.ChangePasswordResponse;
import eu.signme.app.api.response.ErrorResponse;
import eu.signme.app.api.response.GetUserImagePathResponse;
import eu.signme.app.dialog.ChangePasswordDialog;
import eu.signme.app.dialog.ChangePasswordDialog.ChangePasswordListener;
import eu.signme.app.dialog.EditNameDialog;
import eu.signme.app.dialog.EditNameDialog.EditNameDialogListener;
import eu.signme.app.ui.ActionBar;
import eu.signme.app.ui.ActionBar.ActionBarListener;
import eu.signme.app.ui.FailToast;
import eu.signme.app.ui.SuccessToast;
import eu.signme.app.util.Fonts;
import eu.signme.app.util.NetworkUtil;
import eu.signme.app.util.Utils;

public class MyProfileActivity extends SignMeActivity implements
		ActionBarListener, OnClickListener, EditNameDialogListener,
		ChangePasswordListener {

	private ActionBar actionBar;
	private TextView txtName, txtBeer, txtImageNone;
	private ImageView imgSignature;
	private Button btnChangePass, btnEditName, btnChangeImg;
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

		showUserImage();
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
		imgSignature = (ImageView) findViewById(R.id.img_signature);
		txtImageNone = (TextView) findViewById(R.id.txt_image_none);
		btnChangePass = (Button) findViewById(R.id.btn_change_password);
		btnChangeImg = (Button) findViewById(R.id.btn_change_image);

		TextView txtLabelName = (TextView) findViewById(R.id.txt_label_name);
		TextView txtLabelPassword = (TextView) findViewById(R.id.txt_label_password);
		TextView txtPassword = (TextView) findViewById(R.id.txt_password);
		TextView txtLabelBeer = (TextView) findViewById(R.id.txt_label_beer);
		TextView txtLabelImage = (TextView) findViewById(R.id.txt_label_image);

		txtLabelName.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtName.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtLabelPassword.setTypeface(Fonts
				.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtPassword.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtLabelBeer.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtLabelImage.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtImageNone.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));
		txtBeer.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));

		btnEditName.setOnClickListener(this);
		btnChangePass.setOnClickListener(this);
		btnChangeImg.setOnClickListener(this);

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
		case R.id.btn_change_image:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, 1);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			Bitmap resized = null;
			try {
				resized = decodeUri(this, selectedImage, 400);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Uri resizedImage = saveImage(resized);

			if (NetworkUtil.getConnectivityStatus(this) != 0)
				SignMeAPI.changeImage(new File(resizedImage.getPath()),
						new ChangeImageHandler() {

							@Override
							public void onSuccess(ChangeImageResponse response) {
								SuccessToast toast = new SuccessToast(
										MyProfileActivity.this,
										getResources().getString(
												R.string.image_uploaded),
										Toast.LENGTH_LONG);
								toast.show();
								showUserImage();
							}

							@Override
							public void onError(VolleyError error) {
								FailToast toast = new FailToast(
										MyProfileActivity.this,
										getResources().getString(
												R.string.image_not_uploaded),
										Toast.LENGTH_LONG);
								toast.show();

							}
						});

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			// imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			// btnChangeImg.setText(picturePath);

		}

	}

	public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
			throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri),
				null, o);

		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;

		while (true) {
			if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(c.getContentResolver()
				.openInputStream(uri), null, o2);
	}

	private Uri saveImage(Bitmap finalBitmap) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/SignMe");
		myDir.mkdirs();
		Random generator = new Random();
		String fname = "my_signature.jpg";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Uri.fromFile(file);
	}

	private void showUserImage() {
		if (NetworkUtil.getConnectivityStatus(this) != 0)
			SignMeAPI.getUserImagePath(Utils.getIntFromPrefs("id"),
					new GetUserImagePathHandler() {

						@Override
						public void onSuccess(GetUserImagePathResponse response) {
							SignMeAPI.getImage(response.getPath(),
									new GetImageHandler() {

										@Override
										public void onSuccess(Bitmap response) {
											txtImageNone
													.setVisibility(View.GONE);
											imgSignature
													.setVisibility(View.VISIBLE);
											imgSignature
													.setImageBitmap(response);

										}

										@Override
										public void onError(VolleyError error) {
											// TODO Auto-generated method
											// stub

										}
									});

						}

						@Override
						public void onError(VolleyError error) {
							imgSignature.setVisibility(View.GONE);
							txtImageNone.setVisibility(View.VISIBLE);

						}
					});
	}
}
