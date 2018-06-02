package com.gu.appbar.refresh;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayoutSpringBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;

public class RefreshableAppBarHelper
    implements AppBarLayout.OnOffsetChangedListener,
        AppBarLayoutSpringBehavior.SpringOffsetCallback,
        IAppBarRefreshItem {

  private AppBarLayout mAppBarLayout;
  private int mMaxScroll;
  private static final float REFRESH_DISTANCE_RATE = 0.6f;
  private int maxPull;

  private AppBarUiCallBack mCallBack;
  private RefreshState mCurrentRefreshState = RefreshState.IDLE;
  private AppBarCollapseState mCurrentCollapseState;

  public void setAppBarUiCallBack(AppBarUiCallBack callback) {
    this.mCallBack = callback;
  }

  public RefreshableAppBarHelper(@NonNull AppBarLayout appBarLayout) {
    this(appBarLayout, 200);
  }

  public RefreshableAppBarHelper(@NonNull AppBarLayout appBarLayout, int maxPull) {
    this.mAppBarLayout = appBarLayout;
    this.maxPull = maxPull;
    mAppBarLayout.addOnOffsetChangedListener(this);
    CoordinatorLayout.Behavior behavior =
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
    if (behavior instanceof AppBarLayoutSpringBehavior) {
      AppBarLayoutSpringBehavior springBehavior = (AppBarLayoutSpringBehavior) behavior;
      springBehavior.setSpringOffsetCallback(this);
    }
  }

  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    if (mCallBack == null) return;
    if (verticalOffset == 0) {
      if (mCurrentCollapseState != AppBarCollapseState.OPEN) {
        mCurrentCollapseState = AppBarCollapseState.OPEN;
        mCallBack.onOpen();
      }
    } else if (-verticalOffset == getMaxScroll()) {
      if (mCurrentCollapseState != AppBarCollapseState.COLLAPSE) {
        mCurrentCollapseState = AppBarCollapseState.COLLAPSE;
        mCallBack.onCollapsed();
      }
    } else {
      mCallBack.onMiddle(-verticalOffset);
      mCurrentCollapseState = AppBarCollapseState.MIDDLE;
    }
  }

  @Override
  public void springCallback(int offset) {
    if (mCallBack == null) return;
    if (mCurrentRefreshState == RefreshState.REBOUNDING) {
      Log.e("TAG", "springCallback: ---------REBOUNDING!!!!--------");
      // offset=0 时弹回原位置，开始刷新任务！
      if (offset == 0) {
        mCallBack.startRefreshing();
        mCurrentRefreshState = RefreshState.REFRESHING;
      } else {
        mCallBack.onRebound(offset);
      }
    } else if (mCurrentRefreshState == RefreshState.REFRESHING) {
      // Do nothing!
    } else {
      if (!isRelease2Refresh(offset)) {
        mCurrentRefreshState = RefreshState.PULL;
        mCallBack.onPull(offset);
      } else {
        mCurrentRefreshState = RefreshState.RELEASE2REFRESH;
        mCallBack.onPullExceedRefreshSize(
            offset - getPull2RefreshSize(), maxPull - getPull2RefreshSize());
      }
    }
  }

  // -------------- IRefreshable -------------- //

  @Override
  public int getMaxScroll() {
    if (mMaxScroll == 0) {
      mMaxScroll = mAppBarLayout.getTotalScrollRange();
    }
    return mMaxScroll;
  }

  @Override
  public boolean isRelease2Refresh(int pullSize) {
    return pullSize >= getPull2RefreshSize();
  }

  @Override
  public int getMaxPull() {
    return maxPull;
  }

  @Override
  public int getPull2RefreshSize() {
    return (int) (maxPull * REFRESH_DISTANCE_RATE);
  }

  public void doPullBack() {
    if (mCurrentRefreshState == RefreshState.RELEASE2REFRESH) {
      mCurrentRefreshState = RefreshState.REBOUNDING;
    }
  }

  public void refreshFin() {
    mCurrentRefreshState = RefreshState.IDLE;
  }

  public boolean isWorking() {
    return mCurrentRefreshState == RefreshState.REBOUNDING
        || mCurrentRefreshState == RefreshState.REFRESHING;
  }

  /** 释放回收 */
  public void clearAppBar() {
    if (mAppBarLayout != null) {
      mAppBarLayout.removeOnOffsetChangedListener(this);
      CoordinatorLayout.Behavior behavior =
          ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
      if (behavior instanceof AppBarLayoutSpringBehavior) {
        AppBarLayoutSpringBehavior springBehavior = (AppBarLayoutSpringBehavior) behavior;
        springBehavior.setSpringOffsetCallback(null);
      }
      mAppBarLayout = null;
    }
    mCallBack = null;
  }
}
