package com.gu.devel.sinaweibo.userinfo.appbar.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class ScrollInterceptViewPager extends ViewPager {
  private boolean canHorizontalScroll;
  private int mLastMotionX, mLastMotionY;
  private int mActivePointerId;
  private int mTouchSlop;
  private static final int INVALID_POINTER = -1;
  private static final String TAG = ScrollInterceptViewPager.class.getSimpleName();

  public ScrollInterceptViewPager(@NonNull Context context) {
    super(context);
    canHorizontalScroll = true;
    mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() / 2;
  }

  public ScrollInterceptViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    canHorizontalScroll = true;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
      mLastMotionX = (int) ev.getX();
      mLastMotionY = (int) ev.getY();
      mActivePointerId = ev.getPointerId(0);
      canHorizontalScroll = true;
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    switch (ev.getActionMasked()) {
      case MotionEvent.ACTION_MOVE:
        final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
        if (activePointerIndex == INVALID_POINTER) {
          Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
          break;
        }
        if (canHorizontalScroll) {
          int deltaY = Math.abs(mLastMotionY - (int) ev.getY());
          int deltaX = Math.abs(mLastMotionX - (int) ev.getX());
          // 条件越苛刻，越容易执行下拉操作，越不容易执行viewpager水平滚动
          if (deltaY > deltaX && deltaX > mTouchSlop && deltaY >= mTouchSlop) {
            canHorizontalScroll = false;
          }
        }
        mLastMotionX = (int) ev.getX();
        mLastMotionY = (int) ev.getY();
    }
    return canHorizontalScroll && super.onInterceptTouchEvent(ev);
  }

  public void setCanHorizontalScroll(boolean canHorizontalScroll) {
    this.canHorizontalScroll = canHorizontalScroll;
  }
}
