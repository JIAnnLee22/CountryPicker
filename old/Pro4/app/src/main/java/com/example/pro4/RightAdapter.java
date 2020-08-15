package com.example.pro4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RightAdapter extends RecyclerView.Adapter<RightAdapter.ViewHolder> {

    private List<RightBean> rightBeanList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.food_name);
        }
    }

    public RightAdapter(List<RightBean> rightBeanList){
        this.rightBeanList = rightBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RightAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.right_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(rightBeanList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return rightBeanList.size();
    }

    public String getTag(int position){
        return rightBeanList.get(position).getTag();
    }

}
