package eu.signme.app.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import eu.signme.app.R;
import eu.signme.app.model.Comrade;
import eu.signme.app.util.Fonts;
import eu.signme.app.viewholder.ComradesRowHolder;

public class ComradeAdapter extends RecyclerView.Adapter<ComradesRowHolder> {

	private List<Comrade> comrades;
	private Context mContext;

	public ComradeAdapter(Context context, List<Comrade> comrades) {
		this.comrades = comrades;
		this.mContext = context;
	}

	@Override
	public ComradesRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.comrades_item, viewGroup, false);
		ComradesRowHolder mh = new ComradesRowHolder(v);

		return mh;
	}

	@Override
	public void onBindViewHolder(ComradesRowHolder feedListRowHolder, int i) {

		if (i % 2 == 0) {
			feedListRowHolder.view.setBackgroundColor(mContext.getResources()
					.getColor(R.color.gray_background));
		} else {
			feedListRowHolder.view.setBackgroundColor(mContext.getResources()
					.getColor(R.color.light_gray_background));
		}

		TextView txtName = feedListRowHolder.txtName;
		TextView txtBeer = feedListRowHolder.txtBeer;
		TextView txtCount = feedListRowHolder.txtCount;
		txtName.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_MEDIUM));
		txtBeer.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_LIGHT));
		txtCount.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_THIN));

		Comrade comrade = comrades.get(i);

		txtName.setText(comrade.getName());

		txtBeer.setText(mContext.getString(R.string.earned_beers,
				comrade.getBeer()));

		txtCount.setText(Integer.toString(i + 1));

	}

	@Override
	public int getItemCount() {
		return (null != comrades ? comrades.size() : 0);
	}

	public Comrade getItem(int location) {
		return comrades.get(location);
	}

}