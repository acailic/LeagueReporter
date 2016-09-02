package pma.leaguereporter.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import pma.leaguereporter.BuildConfig;
import pma.leaguereporter.R;
import pma.leaguereporter.model.abstractions.AbstractFragment;
import pma.leaguereporter.model.adapters.LinksAdapter;
import pma.leaguereporter.model.objects.Link;
import pma.leaguereporter.util.L;

import java.util.ArrayList;

public class FragmentAbout extends AbstractFragment
		implements AdapterView.OnItemClickListener, View.OnClickListener {

	public static final String TAG = "FragmentAbout";

	private ListView mListView;
	private TextView mVersion;
	private LinksAdapter mAdapter;

	public static FragmentAbout getInstance(@Nullable Bundle data) {
		FragmentAbout fragment = new FragmentAbout();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, container, false);
		setupUI(view);
		showContent();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mVersion != null) mVersion = null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Link item = (Link) mAdapter.getItem(position);
		intent.setData(Uri.parse(item.getLink()));
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.api_site:
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(getString(R.string.link_football_api)));
				startActivity(intent);
				break;

			default:
				L.e(FragmentAbout.class, "default id");
		}
	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(getString(R.string.about_title));
			toolbar.setSubtitle(null);
		}
		mListView = (ListView) view.findViewById(R.id.list_view);
		mListView.setOnItemClickListener(this);
		view.findViewById(R.id.api_site).setOnClickListener(this);
		mVersion = (TextView) view.findViewById(R.id.version);
	}

	private void showContent() {
		if (mAdapter == null) mAdapter = getAdapter();
		mListView.setAdapter(mAdapter);
		mVersion.setText(BuildConfig.VERSION_NAME);
	}

	private LinksAdapter getAdapter() {
		ArrayList<Link> list = new ArrayList<>();
		list.add(new Link(
				R.drawable.ic_linkedin_box,
				getString(R.string.about_linkedin),
				getString(R.string.link_linkedin))
		);


		return new LinksAdapter(getActivity(), list);
	}

}
