package eu.signme.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.signme.app.R;

public class SignaturesRowHolder extends RecyclerView.ViewHolder {
	public TextView txtName, txtCount;
	public ImageView imgThick;
	public RelativeLayout rlSignature;

	public SignaturesRowHolder(View view) {
		super(view);

		this.rlSignature = (RelativeLayout) view.findViewById(R.id.rl_signature);
		this.imgThick = (ImageView) view.findViewById(R.id.icon_thick);
		this.txtCount = (TextView) view.findViewById(R.id.txt_count);
		this.txtName = (TextView) view.findViewById(R.id.txt_name);
	}

}
