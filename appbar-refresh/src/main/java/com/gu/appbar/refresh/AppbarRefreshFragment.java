package com.gu.appbar.refresh;

import android.support.design.widget.AppBarLayout;

import com.gu.mvp.view.fragment.BaseFragment;

/** fragment that wrapper AppBarRefreshItem contains a appbar which can be pull to refresh */
public abstract class AppbarRefreshFragment extends BaseFragment
    implements IAppBarRefreshItem.AppBarUiCallBack, IAppBarRefreshItem {

  private static final int APPBAR_MAX_PULL = 120;
  private IAppBarRefreshItem mHelper;

  public abstract AppBarLayout getAppBarLayout();

  public void initAppBarRefreshHelper() {
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
}
