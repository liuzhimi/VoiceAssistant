package com.lzm.voiceassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lzm.voiceassistant.R;
import com.lzm.voiceassistant.bean.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Morris
 * @email moon.liuzhimi@gmail.com
 * @date 2020-04-12 13:45
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    @NonNull
    private List<Name> orders = new ArrayList<>();

    private OrderCallback orderCallback;

    public OrderAdapter(OrderCallback orderCallback) {
        this.orderCallback = orderCallback;
    }

    public void setOrders(@NonNull List<Name> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.adapter_name_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        final Name order = getItem(position);
        if (order != null) {
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderCallback != null) {
                        orderCallback.showOrderDetail(order);
                    }
                }
            });
            holder.name.setText(order.getName());
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public Name getItem(int position) {
        return orders.size() > position ? orders.get(position) : null;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item;
        TextView name;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.view_content);
            name = itemView.findViewById(R.id.txt_name);
        }
    }

    public interface OrderCallback {
        void showOrderDetail(Name order);
    }
}
