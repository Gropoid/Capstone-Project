package gropoid.punter.presenter.impl;

public interface LoadingCallback {
    void onLoadingProgress(int progress);
    void onLoadingFailure();
}
