package com.gu.devel.sinaweibo.userinfo.appbar.fragment;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gu.appbar.refresh.AppbarRefreshFragment;
import com.gu.devel.sinaweibo.userinfo.appbar.R;
import com.gu.devel.sinaweibo.userinfo.appbar.adapter.TabFragmentPagerAdapter;
import com.gu.devel.sinaweibo.userinfo.appbar.glide.GlideCircleTransformWithBorder;
import com.gu.devel.sinaweibo.userinfo.appbar.widget.tablayout.TabLayout;
import com.gu.devel.sinaweibo.userinfo.appbar.widget.viewpager.ScrollInterceptViewPager;
import com.gu.mvp.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HomePageFragment extends AppbarRefreshFragment implements IHomePageView {
  private Unbinder mUnBinder;

  @BindView(R.id.app_bar)
  AppBarLayout mAppBarLayout;

  @BindView(R.id.tabs_viewpager)
  ScrollInterceptViewPager viewPager;

  @BindView(R.id.tabs)
  TabLayout tabLayout;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.headerimg)
  ImageView headerImg;

  @BindView(R.id.headerlayout)
  LinearLayout headerLayout;

  @BindView(R.id.name_tv)
  TextView username;

  @BindView(R.id.pb)
  ImageView pb;

  @BindView(R.id.search)
  ImageView search;

  @BindView(R.id.back)
  ImageView back;

  private Animation mRotateAnimation;
  private CompositeDisposable mCompositeDisposable;
  private static final int PULL_CHANGE_ROTATE_DEGREE = 3;
  private TabFragmentPagerAdapter mAdapter;

  public static HomePageFragment newInstance() {
    return new HomePageFragment();
  }

  @Override
  public int getLayoutId() {
    return R.layout.home_page_layout;
  }

  @Override
  public AppBarLayout getAppBarLayout() {
    return mAppBarLayout;
  }

  @Override
  public void initView(View parent) {
    mUnBinder = ButterKnife.bind(this, parent);
    mCompositeDisposable = new CompositeDisposable();
    // 必须要有setSupportActionBar,关键
    setSupportActionBar(mToolbar);
    mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager());
    viewPager.setAdapter(mAdapter);
    viewPager.setOffscreenPageLimit(3);
    tabLayout.setTabMode(TabLayout.MODE_FIXED);
    tabLayout.setSelectedTabIndicatorHeight(10);
    tabLayout.setSelectedTabIndicatorColor(
        ContextCompat.getColor(getContext(), R.color.orange_800));
    tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
    tabLayout.setNeedSwitchAnimation(true);
    tabLayout.setIndicatorWidthWrapContent(true);
    tabLayout.setupWithViewPager(viewPager);
    // glide load
    glideLoadImage(getContext(), R.drawable.user_img, headerImg);
  }

  public void setSupportActionBar(Toolbar toolbar) {
    if (getActivity() != null) ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
  }

  @Override
  public void destroyView() {
    super.destroyView();
    mAdapter.clear();
    mCompositeDisposable.dispose();
    mCompositeDisposable = null;
    mUnBinder.unbind();
    mUnBinder = null;
  }

  @OnClick(R.id.headerlayout)
  public void onClickHeaderLayout() {
    Log.e("TAG", "onClickHeaderLayout: CLICKED!");
  }

  @OnClick(R.id.back)
  public void onClickBack() {
    mActivity.finish();
  }

  private void glideLoadImage(Context context, @DrawableRes int drawableId, ImageView target) {
    if (context != null)
      GlideApp.with(this)
          .load(drawableId)
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .transform(
              new GlideCircleTransformWithBorder(
                  context, 4, ContextCompat.getColor(context, R.color.grey_700)))
          .into(target);
  }

  // -------------- IHomePageView -------------- //
  @Override
  public void showProgressBar() {
    pb.setImageLevel(2);
  }

  @Override
  public void showButton() {
    pb.setImageLevel(0);
  }

  @Override
  public void rotateProgressBarBy(float delta) {
    pb.setPivotX(pb.getWidth() / 2);
    pb.setPivotY(pb.getHeight() / 2);
    pb.setRotation(pb.getRotation() + delta);
  }

  @Override
  public void rotateProgressBarTo(float degree) {
    pb.setPivotX(pb.getWidth() / 2);
    pb.setPivotY(pb.getHeight() / 2);
    pb.setRotation(degree);
  }

  @Override
  public void stopProgressBarAnim() {
    pb.clearAnimation();
  }

  @Override
  public void showLoading() {
    if (mRotateAnimation == null) {
      mRotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pb_rotate_anim);
    }
    pb.startAnimation(mRotateAnimation);
    ((RecyclerViewFragment) mAdapter.getCurrentFragment()).startRefresh();
  }

  // ------------ AppBarUiCallBack ------------ //

  @Override
  public void onPull(int pullSize) {
    if (isOpenState()) pb.setImageLevel(pullSize == 0 ? 0 : 2);
    rotateProgressBarTo(0);
    lastRate = 0;
  }

  private float lastRate;

  @Override
  public void onPullExceedRefreshSize(int exceedSize, int exceedMaxSize) {
    float currentRate = (float) exceedSize / exceedMaxSize;
    int degree = (int) ((currentRate - lastRate) * 120);
    lastRate = currentRate;
    rotateProgressBarBy(degree);
  }

  @Override
  public void onRebound(int offset) {
    rotateProgressBarBy(-PULL_CHANGE_ROTATE_DEGREE);
  }

  @Override
  public void onMiddle(int scroll, int maxScroll) {
    float rate = 1f - (float) scroll / maxScroll;
    headerLayout.setAlpha(Math.max(0.5f, rate));
    username.setVisibility(View.GONE);
    mToolbar.setBackgroundColor(0x00ffffff);
    back.setImageLevel(0);
    search.setImageLevel(0);
    pb.setImageLevel(0);
  }

  @Override
  public void onCollapsed() {
    if (getContext() != null) mToolbar.setBackgroundResource(R.drawable.toolbar_bg);
    username.setVisibility(View.VISIBLE);
    back.setImageLevel(1);
    search.setImageLevel(1);
    pb.setImageLevel(1);
  }

  @Override
  public void onOpen() {
    headerLayout.setAlpha(1f);
    back.setImageLevel(0);
  }

  @Override
  public void startRefreshing() {
    mCompositeDisposable.add(
        Observable.just("")
            .map(
                new Function<String, List<String>>() {
                  @Override
                  public List<String> apply(String s) {
                    showLoading();
                    List<String> data = new ArrayList<>();
                    for (int i = 0; i < 15; i++) {
                      data.add(String.valueOf(i));
                    }
                    return data;
                  }
                })
            .delay(3000, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<List<String>>() {
                  @Override
                  public void accept(List<String> data) {
                    stopProgressBarAnim();
                    showButton();
                    rotateProgressBarTo(0);
                    refreshFin();
                    ((RecyclerViewFragment) mAdapter.getCurrentFragment()).update(data);
                  }
                }));
  }
}
