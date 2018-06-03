package com.gu.appbar.refresh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gu.mvp.view.fragment.BaseFragment;

/** fragment that wrapper AppBarRefreshItem contains a appbar which can be pull to refresh */
public abstract class AppbarRefreshFragment extends BaseFragment
    implements IAppBarRefreshItem.AppBarUiCallBack, IAppBarRefreshItem {

  private static final int APPBAR_MAX_PULL = 120;
  private IAppBarRefreshItem mHelper;

  public abstract AppBarLayout getAppBarLayout();

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    initAppBarRefreshHelper();
    return view;
  }

  // call after getAppBarLayout() return NOT NULL!
  public void initAppBarRefreshHelper() {
    if (getAppBarLayout() == null) {
      try {
        throw new Exception("----Appbar is null ! exception!----");
      } catch (Exception e) {
        e.printStackTrace();
      }
      return;
    }
    mHelper = new RefreshableAppBarHelper(getAppBarLayout(), APPBAR_MAX_PULL);
    ((RefreshableAppBarHelper) mHelper).setAppBarUiCallBack(this);
  }

  @Override
  public void destroyView() {
    clearAppBar();
  }

  @Override
  public void doPullBack() {
    mHelper.doPullBack();
  }

  @Override
  public boolean isWorking() {
    return mHelper.isWorking();
  }

  @Override
  public void refreshFin() {
    mHelper.refreshFin();
  }

  @Override
  public int getMaxPull() {
    return mHelper.getMaxPull();
  }

  @Override
  public int getMaxScroll() {
    return mHelper.getMaxScroll();
  }

  @Override
  public boolean isRelease2Refresh(int pullSize) {
    return mHelper.isRelease2Refresh(pullSize);
  }

  @Override
  public int getPull2RefreshSize() {
    return mHelper.getPull2RefreshSize();
  }

  @Override
  public void clearAppBar() {
    mHelper.clearAppBar();
    mHelper = null;
  }

  @Override
  public boolean isOpenState() {
    return mHelper.isOpenState();
  }
}
