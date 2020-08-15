package com.example.cardviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<CountryBean> countryBean;
    private Boolean isSearch = false;

    private OnCountryItemClickListener mOnCountryItemClickListener;

    public CountryAdapter(List<CountryBean> countryBean) {
        this.countryBean = countryBean;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, final int position) {
        holder.countryName.setText(countryBean.get(position).getZh());

        //外部实现item点击接口的时候传出国家名称
        holder.countryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCountryItemClickListener.itemClick(countryBean.get(position).getZh());
            }
        });


        holder.firstPinyin.setVisibility(View.GONE);

        if (!getSearch()) {
            //默认显示拼音首字母，每一个item都有首字母的布局
            holder.firstPinyin.setVisibility(View.VISIBLE);
            holder.firstPinyin.setText(PinyinUtil.getSpells(countryBean.get(position).getZh()));
            if (position != 0) {
                //获取当前和前一个的拼音首字母
                String now = PinyinUtil.getSpells(countryBean.get(position).getZh());
                String last = PinyinUtil.getSpells(countryBean.get(position - 1).getZh());

                //默认显示首字母
                holder.firstPinyin.setVisibility(View.VISIBLE);
                if (now.equals(last)) {
                    //如果当前的拼音首字母和上一个的相同，隐藏首字母布局
                    holder.firstPinyin.setVisibility(View.GONE);
                } else if (now.equals("-")) {
                    //如果识别为“-”也隐藏
                    if (last.equals(PinyinUtil.getSpells(countryBean.get(position + 1).getZh()))) {
                        holder.firstPinyin.setVisibility(View.GONE);
                    }
                } else {
                    //避免遇到中间有识别为“-”的item，跟上上个比较是否一样，一样就隐藏首字母布局
                    if (position > 2 && now.equals(PinyinUtil.getSpells(countryBean.get(position - 2).getZh()))) {
                        holder.firstPinyin.setVisibility(View.GONE);
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return countryBean.size();
    }

    public Boolean getSearch() {
        return isSearch;
    }

    public void setSearch(Boolean search) {
        isSearch = search;
    }

    //接口
    public void setOnCountryItemClickListener(OnCountryItemClickListener mOnCountryItemClickListener) {
        this.mOnCountryItemClickListener = mOnCountryItemClickListener;
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {

        private TextView countryName;
        private CardView countryCard;
        private TextView firstPinyin;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name);
            countryCard = itemView.findViewById(R.id.country_card);
            firstPinyin = itemView.findViewById(R.id.first_pinyin);
        }
    }

    //继承LinearSmoothScroller使滚动的条目到列表的最上方
    public static class TopSmoothScroller extends LinearSmoothScroller {
        TopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;//具体见源码注释
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;//具体见源码注释
        }
    }

    //暴露一个item的监听
    public interface OnCountryItemClickListener {
        void itemClick(String country);
    }
}
