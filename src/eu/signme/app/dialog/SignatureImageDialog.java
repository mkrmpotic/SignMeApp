package eu.signme.app.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import eu.signme.app.R;
import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.GetImageHandler;
import eu.signme.app.api.SignMeAPI.GetUserImagePathHandler;
import eu.signme.app.api.response.GetUserImagePathResponse;
import eu.signme.app.model.Signature;
import eu.signme.app.util.Fonts;

public class SignatureImageDialog extends DialogFragment {

	private ImageView imgSignature;
	private TextView txtName, txtNoImage;
	private Signature signature;

	public SignatureImageDialog(Signature signature) {
		this.signature = signature;
		SignMeAPI.getUserImagePath(signature.getIUserd(),
				new GetUserImagePathHandler() {

					@Override
					public void onSuccess(GetUserImagePathResponse response) {
						SignMeAPI.getImage(response.getPath(),
								new GetImageHandler() {

									@Override
									public void onSuccess(Bitmap response) {
										imgSignature.setImageBitmap(response);

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
						txtNoImage.setVisibility(View.VISIBLE);
					}
				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.dialog_signature_image, container);
		txtName = (TextView) view.findViewById(R.id.txt_name);
		txtNoImage = (TextView) view.findViewById(R.id.txt_no_signature);
		imgSignature = (ImageView) view.findViewById(R.id.img_signature);

		txtName.setText(signature.getUserName());

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

		return view;
	}

}
