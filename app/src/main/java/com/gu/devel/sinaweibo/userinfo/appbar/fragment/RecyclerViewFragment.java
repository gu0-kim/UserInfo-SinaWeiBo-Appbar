package com.gu.devel.sinaweibo.userinfo.appbar.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gu.devel.sinaweibo.userinfo.appbar.R;
import com.gu.devel.sinaweibo.userinfo.appbar.adapter.DataAdapter;
import com.gu.devel.sinaweibo.userinfo.appbar.widget.recyclerview.decorator.DividerItemDecorator;
import com.gu.mvp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends BaseFragment {
  DataAdapter adapter;
  List<String> list;

  public static RecyclerViewFragment newInstance() {
    return new RecyclerViewFragment();
  }

  @Override
  public int getLayoutId() {
    return R.layout.recyclerview_layout;
  }

  @Override
  public void initView(View parent) {
    RecyclerView recyclerView = (RecyclerView) parent;
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.addItemDecoration(new DividerItemDecorator(getContext()));

    list = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      list.add(String.valueOf(i));
    }
    adapter = new DataAdapter(getContext());
    adapter.add(list);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void destroyView() {}
}
