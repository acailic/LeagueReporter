package pma.leaguereporter.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pma.leaguereporter.R;
import pma.leaguereporter.model.objects.Link;

import java.util.ArrayList;

public class LinksAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private ArrayList<Link> mList;

	public LinksAdapter(Context context, ArrayList<Link> list) {
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
	}

	@Override
	public int getCount() {
		if (mList != null) return mList.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Link item = mList.get(position);
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_link, parent, false);
		}
		((ImageView) convertView.findViewById(R.id.icon)).setImageResource(item.getIcon());
		((TextView) convertView.findViewById(R.id.link)).setText(item.getTitle());
		return convertView;
	}
}
