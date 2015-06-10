package eu.signme.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import eu.signme.app.R;

public class ComradesRowHolder extends RecyclerView.ViewHolder {
	public TextView txtName, txtBeer, txtCount;
	public ImageView imgMedal;
	public View view;

	public ComradesRowHolder(View view) {
		super(view);

		this.view = (View) view;
		this.txtCount = (TextView) view.findViewById(R.id.txt_count);
		this.txtName = (TextView) view.findViewById(R.id.txt_name);
		this.txtBeer = (TextView) view.findViewById(R.id.txt_beer);
		this.imgMedal = (ImageView) view.findViewById(R.id.icon_medal);
	}

}
