package com.gu.devel.sinaweibo.userinfo.appbar.fragment;

public interface IHomePageView {

  void showProgressBar();

  void showButton();

  void rotateProgressBar(float delta);

  void stopProgressBarAnim();

  void showLoading();
}
