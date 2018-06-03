package com.gu.devel.sinaweibo.userinfo.appbar.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.gu.devel.sinaweibo.userinfo.appbar.R;
import com.gu.devel.sinaweibo.userinfo.appbar.adapter.DataAdapter;
import com.gu.devel.sinaweibo.userinfo.appbar.widget.recyclerview.decorator.DividerItemDecorator;
import com.gu.mvp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecyclerViewFragment extends BaseFragment {
  DataAdapter adapter;
  List<String> list;

  Unbinder mUnbinder;

  @BindView(R.id.loading_view)
  FrameLayout loading_view;

  @BindView(R.id.list)
  RecyclerView mRecyclerView;

  public static RecyclerViewFragment newInstance() {
    return new RecyclerViewFragment();
  }

  @Override
  public int getLayoutId() {
    return R.layout.recyclerview_layout;
  }

  @Override
  public void initView(View parent) {
    mUnbinder = ButterKnife.bind(this, parent);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.addItemDecoration(new DividerItemDecorator(getContext()));

    list = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      list.add(String.valueOf(i));
    }
    adapter = new DataAdapter(getContext());
    adapter.add(list);
    mRecyclerView.setAdapter(adapter);
  }

  @Override
  public void destroyView() {
    mUnbinder.unbind();
    mUnbinder = null;
    adapter.clearAll();
  }

  public void update(List<String> data) {
    adapter.addAll(data);
    adapter.notifyDataSetChanged();
    mRecyclerView.invalidateItemDecorations();
    loading_view.setVisibility(View.GONE);
  }

  public void startRefresh() {
    loading_view.setVisibility(View.VISIBLE);
  }
}
