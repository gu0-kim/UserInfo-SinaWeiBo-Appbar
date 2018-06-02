package com.gu.devel.sinaweibo.userinfo.appbar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gu.devel.sinaweibo.userinfo.appbar.R;
import com.gu.mvp.view.adapter.IBaseAdapter;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataAdapter extends IBaseAdapter<String, RecyclerView.ViewHolder> {
  private LayoutInflater mInflater;
  private static final int DIVIDER_TYPE = 1;
  private static final int DATA_VIEW_TYPE = 2;
  private static final int HEADER_COUNT = 1;

  public DataAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return viewType == DIVIDER_TYPE
        ? new DescriptionViewHolder(mInflater.inflate(R.layout.des_layout, parent, false))
        : new ViewHolder(mInflater.inflate(R.layout.text_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (getItemViewType(position) == DIVIDER_TYPE) {
      ((DescriptionViewHolder) holder)
          .text.setText(String.format(Locale.getDefault(), "全部微博(%d)", data.size()));
    } else {
      ((TextView) holder.itemView).setText(data.get(position - HEADER_COUNT));
    }
  }

  @Override
  public int getItemViewType(int position) {
    return position == 0 ? DIVIDER_TYPE : DATA_VIEW_TYPE;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    ViewHolder(View itemView) {
      super(itemView);
    }
  }

  class DescriptionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.num_des_text)
    TextView text;

    DescriptionViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
