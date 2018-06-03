package com.gu.devel.sinaweibo.userinfo.appbar.fragment;

public interface IHomePageView {

  void showProgressBar();

  void showButton();

  void rotateProgressBarBy(float delta);

  void rotateProgressBarTo(float degree);

  void stopProgressBarAnim();

  void showLoading();
}
