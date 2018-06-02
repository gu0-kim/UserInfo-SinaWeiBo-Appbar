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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class HomePageFragment extends AppbarRefreshFragment
    implements IHomePageView {
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

    viewPager.setAdapter(new TabFragmentPagerAdapter(getChildFragmentManager()));
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

    initAppBarRefreshHelper();
  }

  public void setSupportActionBar(Toolbar toolbar) {
    if (getActivity() != null) ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
  }

  @Override
  public void destroyView() {
    super.destroyView();
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
  public void rotateProgressBar(float delta) {
    pb.setPivotX(pb.getWidth() / 2);
    pb.setPivotY(pb.getHeight() / 2);
    pb.setRotation(delta * 360);
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
  }

  // ------------ AppBarUiCallBack ------------ //

  @Override
  public void onPull(int pullSize) {
    viewPager.setCanHorizontalScroll(pullSize == 0);
    headerLayout.setTranslationY(pullSize);
    pb.setImageLevel(pullSize == 0 ? 0 : 2);
    rotateProgressBar(0);
  }

  @Override
  public void onPullExceedRefreshSize(int exceedSize, int exceedMaxSize) {
    float rate = (float) exceedSize / exceedMaxSize;
    rotateProgressBar(rate);
  }

  @Override
  public void onRebound(int offset) {
    float rate = (float) offset / getMaxPull();
    rotateProgressBar(rate);
  }

  @Override
  public void onMiddle(int offset) {
    float rate = 1f - (float) offset / getMaxScroll();
    headerLayout.setAlpha(Math.max(0.5f, rate));
    //    if (offset != mHelper.getMaxScroll()) {
    username.setVisibility(View.GONE);
    mToolbar.setBackgroundColor(0x00ffffff);
    back.setImageLevel(0);
    search.setImageLevel(0);
    pb.setImageLevel(0);
    //    }
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
            .doOnNext(
                new Consumer<String>() {
                  @Override
                  public void accept(String s) throws Exception {
                    showLoading();
                  }
                })
            .delay(3000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<String>() {
                  @Override
                  public void accept(String s) throws Exception {
                    stopProgressBarAnim();
                    showButton();
                    rotateProgressBar(0);
                    refreshFin();
                  }
                }));
  }
}
