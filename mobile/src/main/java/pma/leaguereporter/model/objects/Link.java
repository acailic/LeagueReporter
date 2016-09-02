package pma.leaguereporter.model.objects;

public class Link {

	private int mIcon;
	private String mTitle;
	private String mLink;

	public Link(int mIcon, String mTitle, String mLink) {
		this.mIcon = mIcon;
		this.mTitle = mTitle;
		this.mLink = mLink;
	}

	public int getIcon() {
		return mIcon;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getLink() {
		return mLink;
	}

}
