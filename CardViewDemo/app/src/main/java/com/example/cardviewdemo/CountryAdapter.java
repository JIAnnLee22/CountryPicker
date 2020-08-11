package com.example.cardviewdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<CountryBean> countryBean;

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
        holder.coutryName.setText(countryBean.get(position).getZh());
        holder.countryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + countryBean.get(position).getZh());
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryBean.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        private TextView coutryName;
        private CardView countryCard;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            coutryName = itemView.findViewById(R.id.country_name);
            countryCard = itemView.findViewById(R.id.country_card);
        }
    }

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

}
