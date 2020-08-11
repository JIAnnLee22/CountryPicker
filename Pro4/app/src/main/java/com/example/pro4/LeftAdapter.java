package com.example.pro4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.ViewHolder> {

    private List<LeftBean> leftBeanList;
    private Boolean isSelected;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeName;
        CardView cardBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName = itemView.findViewById(R.id.type_name);
            cardBg = itemView.findViewById(R.id.card_bg);
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


    public LeftAdapter(List<LeftBean> leftBeanList) {
        this.leftBeanList = leftBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.left_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.typeName.setText(leftBeanList.get(position).getTypeName());
        cardBgRestore(holder.cardBg);
        if (leftBeanList.get(position).getTag().equals(getmTag())) {
            setmPosition(position);
            changeBg(holder.cardBg);
        }
        holder.cardBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iItem != null) {
                    iItem.setItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return leftBeanList.size();
    }

    public void cardBgRestore(CardView cardBg) {
        cardBg.setCardBackgroundColor(Color.GRAY);
    }

    public void changeBg(CardView cardBg) {
        cardBg.setCardBackgroundColor(Color.WHITE);
    }

    int mPosition;
    String mTag;
    private IItem iItem;

    public interface IItem {
        void setItem(int position);
    }

    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    private String getmTag() {
        return mTag;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

    public int getmPosition() {
        return mPosition;
    }

    private void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}

