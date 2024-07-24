package com.example.aksam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aksam.databinding.ActivityCartBinding;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {
    private ActivityCartBinding binding;
    private DatabaseHelper dbHelper;
    private CartAdapter cartAdapter;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);

        loadFragments();

        // RecyclerView ayarları
        List<CartItem> cartItemList = dbHelper.getCartItems(userId);
        cartAdapter = new CartAdapter(this, cartItemList, userId, this);
        binding.cartProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.cartProductsRecyclerView.setAdapter(cartAdapter);

        updateTotalPrice(cartItemList);

        // Siparişi Tamamla butonuna tıklama işlevi
        binding.cartCheckOutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }

    private void loadFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.searchBarContainer.getId(), new SearchBarFragment());
        transaction.replace(binding.bottomMenuContainer.getId(), new BottomMenuFragment());
        transaction.commit();
    }

    private void updateTotalPrice(List<CartItem> cartItemList) {
        double totalPrice = 0.0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        binding.cartTotalPrice.setText(String.format("Toplam: %.2f TL", totalPrice));
    }

    @Override
    public void onCartItemRemoved(int itemId) {
        dbHelper.deleteCartItem(itemId);
        List<CartItem> cartItemList = dbHelper.getCartItems(userId);
        updateTotalPrice(cartItemList);
    }
}
