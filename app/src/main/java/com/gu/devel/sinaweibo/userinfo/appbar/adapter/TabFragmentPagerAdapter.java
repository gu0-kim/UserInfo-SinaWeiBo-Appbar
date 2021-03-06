package com.gu.devel.sinaweibo.userinfo.appbar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.gu.devel.sinaweibo.userinfo.appbar.fragment.RecyclerViewFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

  private static final String[] TITLES = new String[] {"主页", "微博", "相册"};
  private Fragment mCurrentFragment;

  public TabFragmentPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    Log.e("TAG", "------------------getItem: position=" + position);
    return RecyclerViewFragment.newInstance();
  }

  @Override
  public int getCount() {
    return TITLES.length;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return TITLES[position];
  }

  @Override
  public void setPrimaryItem(ViewGroup container, int position, Object object) {
    super.setPrimaryItem(container, position, object);
    this.mCurrentFragment = (Fragment) object;
  }

  public Fragment getCurrentFragment() {
    return mCurrentFragment;
  }

  public void clear() {
    mCurrentFragment = null;
  }
}
