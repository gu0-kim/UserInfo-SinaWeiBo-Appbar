package com.gu.appbar.refresh;

public interface IAppBarRefreshItem {
  void doPullBack();

  boolean isWorking();

  void refreshFin();

  int getMaxPull();

  /**
   * 最大滚动的距离
   *
   * @return max scroll
   */
  int getMaxScroll();

  boolean isRelease2Refresh(int pullSize);

  int getPull2RefreshSize();

  void clearAppBar();

  //
  /** 界面回调接口 */
  interface AppBarUiCallBack {
    /** 下拉没有到达"刷新距离"时触发回调 */
    void onPull(int offset);

    /** 下拉超过"刷新距离"时触发回调 */
    void onPullExceedRefreshSize(int exceedSize, int exceedMaxSize);

    void onRebound(int offset);

    /** 折叠状态 */
    void onCollapsed();

    /** 打开状态 */
    void onOpen();

    /** 介于“打开”和“折叠”之间的状态 */
    void onMiddle(int offset);

    void startRefreshing();
  }

  enum RefreshState {
    // 空闲状态
    IDLE,
    // 下拉状态
    PULL,
    // 释放需要刷新的下拉状态
    RELEASE2REFRESH,
    // 已经超过刷新距离，释放后的回弹动画状态
    REBOUNDING,
    // 刷新状态
    REFRESHING
  }

  enum AppBarCollapseState {
    OPEN,
    COLLAPSE,
    MIDDLE
  }
}
