package eu.signme.app.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import eu.signme.app.R;
import eu.signme.app.model.Lecture;
import eu.signme.app.model.Signature;
import eu.signme.app.ui.swipe.OnItemClickListener;
import eu.signme.app.util.Fonts;
import eu.signme.app.viewholder.SignaturesRowHolder;

public class SignatureAdapter extends RecyclerView.Adapter<SignaturesRowHolder> {

	private List<Signature> signatures;
	private Context mContext;
	private int myId;
	private OnItemClickListener clickListener = null;

	public SignatureAdapter(Context context, List<Signature> signatures,
			int userId) {
		this.signatures = signatures;
		this.mContext = context;
		this.myId = userId;
	}

	@Override
	public SignaturesRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.signatures_item, viewGroup, false);
		SignaturesRowHolder mh = new SignaturesRowHolder(v);

		return mh;
	}

	@Override
	public void onBindViewHolder(SignaturesRowHolder feedListRowHolder, int i) {

		if (i % 2 == 0) {
			feedListRowHolder.rlSignature.setBackgroundColor(mContext
					.getResources().getColor(R.color.gray_background));
		} else {
			feedListRowHolder.rlSignature.setBackgroundColor(mContext
					.getResources().getColor(R.color.light_gray_background));
		}

		TextView txtName = feedListRowHolder.txtName;
		TextView txtBeer = feedListRowHolder.txtBeer;
		TextView txtCount = feedListRowHolder.txtCount;
		TextView txtSigned = feedListRowHolder.txtSigned;
		txtName.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_MEDIUM));
		txtBeer.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_BOLD));
		txtCount.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_THIN));
		txtSigned.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_THIN));

		Signature signature = signatures.get(i);

		if (signature.getIUserd() == myId) {
			txtName.setText("Me");
			txtName.setTextColor(mContext.getResources().getColor(
					R.color.signme_green));
		} else {

			txtName.setText(signature.getUserName());
			txtName.setTextColor(mContext.getResources().getColor(
					R.color.dark_gray_text));
		}

		if (signature.getStatus() == 0) {

			feedListRowHolder.imgThick.setVisibility(View.GONE);
			txtCount.setText(Integer.toString(i + 1));
		} else {
			feedListRowHolder.imgThick.setVisibility(View.VISIBLE);

		}

	}

	@Override
	public int getItemCount() {
		return (null != signatures ? signatures.size() : 0);
	}

	public Signature getItem(int location) {
		return signatures.get(location);
	}
}