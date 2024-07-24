package com.example.aksam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aksam.databinding.CartListItemBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private int userId;
    private OnCartItemChangeListener onCartItemChangeListener;

    public CartAdapter(Context context, List<CartItem> cartItemList, int userId, OnCartItemChangeListener onCartItemChangeListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.userId = userId;
        this.onCartItemChangeListener = onCartItemChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        CartListItemBinding binding = CartListItemBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.binding.cartProductTitleTv.setText(item.getName());
        holder.binding.cartProductPriceTv.setText(String.format("%.2f TL", item.getPrice()));
        holder.binding.cartProductSizeTv.setText("Beden: " + item.getSize());
        holder.binding.cartProductQuantityTv.setText("Adet: " + item.getQuantity());
        Glide.with(context).load(item.getImagePath()).into(holder.binding.productImageView);

        holder.binding.cartProductDeleteBtn.setOnClickListener(v -> {
            onCartItemChangeListener.onCartItemRemoved(item.getId());
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public interface OnCartItemChangeListener {
        void onCartItemRemoved(int itemId);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CartListItemBinding binding;

        public CartViewHolder(CartListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
