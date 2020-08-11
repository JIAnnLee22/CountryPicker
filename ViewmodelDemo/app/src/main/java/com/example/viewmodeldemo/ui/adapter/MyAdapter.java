package com.example.viewmodeldemo.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewmodeldemo.MainViewModel;
import com.example.viewmodeldemo.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final String TAG = "";
    private List<String> mNameList;
//    private Context context;

    public MyAdapter(List<String> nameList) {
        mNameList = nameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.name.setText(mNameList.get(position));
        holder.delete.setOnClickListener(view -> {
            Toast.makeText(holder.itemView.getContext(), "点了删除", Toast.LENGTH_SHORT).show();
        });
        holder.edit.setOnClickListener(view -> Toast.makeText(holder.itemView.getContext(), "点了编辑", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_text);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
