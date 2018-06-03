package com.gu.devel.sinaweibo.userinfo.appbar;

import android.os.Bundle;

import com.gu.appbar.refresh.AppbarRefreshActivity;
import com.gu.appbar.refresh.IAppBarRefreshItem;
import com.gu.devel.sinaweibo.userinfo.appbar.app.AppBarApplication;
import com.gu.devel.sinaweibo.userinfo.appbar.fragment.HomePageFragment;
import com.gu.mvp.utils.leaks.CleanLeakUtils;

public class MainActivity extends AppbarRefreshActivity {

  HomePageFragment mFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mFragment =
        (HomePageFragment) getSupportFragmentManager().findFragmentById(R.id.content_layout);
    if (mFragment == null) {
      mFragment = HomePageFragment.newInstance();
      getSupportFragmentManager().beginTransaction().add(R.id.content_layout, mFragment).commit();
    }
  }

  @Override
  public IAppBarRefreshItem findAppBarRefreshItem() {
    return mFragment;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mFragment = null;
    CleanLeakUtils.fixInputMethodManagerLeak(this);
    ((AppBarApplication) getApplication()).checkItem(this);
  }
}
