package pma.leaguereporter.model.interfaces;

public interface OnResultListener<S> extends OnResult<S>{
	void onError(int code);
}
