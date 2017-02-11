package com.zappos.namanrajpal.ilovezappos;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zappos.namanrajpal.ilovezappos.Model.Product;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.BindingHolder> {
    private List<Product> products;
    private MainActivity myactivity;

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    public ProductsRecyclerAdapter(List<Product> recyclerUsers, MainActivity act) {
        this.products = recyclerUsers;
        this.myactivity = act;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_result_grid, parent, false);
        BindingHolder holder = new BindingHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder,final int position) {
        final Product product = products.get(position);
        holder.getBinding().setVariable(BR.product, product);
        holder.getBinding().executePendingBindings();
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myactivity.onItemClicked(position);
            }
        });
        holder.getBinding().getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myactivity.onItemLongClicked(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}