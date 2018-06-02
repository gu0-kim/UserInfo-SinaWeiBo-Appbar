package com.gu.appbar.refresh;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public abstract class AppbarRefreshActivity extends AppCompatActivity {

  public abstract IAppBarRefreshItem findAppBarRefreshItem();

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (findAppBarRefreshItem().isWorking()) return true;
    if (ev.getActionMasked() == MotionEvent.ACTION_UP) findAppBarRefreshItem().doPullBack();
    return super.dispatchTouchEvent(ev);
  }
}
