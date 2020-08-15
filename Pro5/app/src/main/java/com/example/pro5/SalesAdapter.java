package com.example.pro5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHodler> {

    private List<SaleBean> saleBeanList;

    public SalesAdapter(List<SaleBean> list) {
        saleBeanList = list;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHodler(LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        if (saleBeanList.size() == 0) {
            holder.ifNone.setText("暂无销售数据");
        } else {
            holder.ifNone.setText("");
            holder.hotSale.setText(saleBeanList.get(position).getSaleName());
            holder.sales.setText(String.valueOf(saleBeanList.get(position).getSales()));
            holder.saleAmount.setText(String.valueOf(saleBeanList.get(position).getSaleAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return saleBeanList.size();
    }

//    public List<SaleBean> getSaleBeanList() {
//        return saleBeanList;
//    }
//
//    public void setSaleBeanList(List<SaleBean> saleBeanList) {
//        this.saleBeanList = saleBeanList;
//    }

    public class ViewHodler extends RecyclerView.ViewHolder {

        private TextView hotSale;
        private TextView sales;
        private TextView saleAmount;
        private TextView ifNone;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            hotSale = itemView.findViewById(R.id.hotsales);
            sales = itemView.findViewById(R.id.sales);
            saleAmount = itemView.findViewById(R.id.sale_amount);
            ifNone = itemView.findViewById(R.id.if_none);
        }
    }
}
