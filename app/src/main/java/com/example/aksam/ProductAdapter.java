package com.example.aksam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aksam.databinding.ItemProductBinding;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<ProductItem> productList;
    private DatabaseHelper dbHelper;
    private int userId;

    public ProductAdapter(Context context, List<ProductItem> productList) {
        this.context = context;
        this.productList = productList;
        dbHelper = new DatabaseHelper(context);
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemProductBinding binding = ItemProductBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem item = productList.get(position);
        holder.binding.itemName.setText(item.getName());
        holder.binding.itemPrice.setText(String.format("%.2f TL", item.getPrice()));
        holder.binding.itemCategory.setText(item.getCategory());
        Glide.with(context).load(item.getImagePath()).into(holder.binding.itemImage);

        // Ürün detayına gitme işlevi
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("productId", item.getId());  // productId'yi intent'e ekle
            intent.putExtra("productName", item.getName());
            intent.putExtra("productPrice", item.getPrice());
            intent.putExtra("productImage", item.getImagePath());
            intent.putExtra("productSize", item.getSize());
            intent.putExtra("productCategory", item.getCategory());
            intent.putExtra("productQuantity", item.getQuantity());
            context.startActivity(intent);
        });

        holder.binding.addToCart.setOnClickListener(v -> {
            if (userId != -1) {
                CartItem cartItem = new CartItem(0, item.getName(), item.getPrice(), item.getImagePath(), item.getSize(), item.getCategory(), item.getQuantity());
                dbHelper.addOrUpdateCartItem(cartItem, userId);
                Toast.makeText(context, item.getName() + " sepete eklendi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<ProductItem> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
