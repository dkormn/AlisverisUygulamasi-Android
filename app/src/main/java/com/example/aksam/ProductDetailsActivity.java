package com.example.aksam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.aksam.databinding.ActivityProductDetailsBinding;

public class ProductDetailsActivity extends AppCompatActivity {

    private ActivityProductDetailsBinding binding;
    private DatabaseHelper dbHelper;
    private int productId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFragments();
        dbHelper = new DatabaseHelper(this);

        // Shared Preferences kullanarak oturum durumunu kontrol et
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        // Intent verilerini alma
        Intent intent = getIntent();
        String productImage = intent.getStringExtra("productImage");
        String productTitle = intent.getStringExtra("productName");
        double productPrice = intent.getDoubleExtra("productPrice", 0);
        String productSize = intent.getStringExtra("productSize");
        String productCategory = intent.getStringExtra("productCategory");
        int productQuantity = intent.getIntExtra("productQuantity", 1);
        productId = intent.getIntExtra("productId", -1);

        // Ürün verilerini arayüze yükleme
        Glide.with(this).load(productImage).into(binding.proDetailsImageView);
        binding.proDetailsTitleTv.setText(productTitle);
        binding.proDetailsPriceTv.setText(String.format("%.2f TL", productPrice));
        binding.proDetailsDescriptionTv.setText("Yüksek Kalite Ürün");

        // Size spinner ayarları
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.proDetailsSizeSpinner.setAdapter(adapter);

        // Sepete ekle butonuna tıklama işlevi
        binding.proDetailsAddCartBtn.setOnClickListener(v -> {
            String selectedSize = binding.proDetailsSizeSpinner.getSelectedItem().toString();
            addToCart(productTitle, productPrice, productImage, selectedSize, productCategory, productQuantity);
        });

        // Favorilere ekle butonuna tıklama işlevi
        binding.proDetailsLikeBtn.setOnClickListener(v -> addToFavorites());
    }

    private void loadFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.searchBarContainer.getId(), new SearchBarFragment());
        transaction.replace(binding.bottomMenuContainer.getId(), new BottomMenuFragment());
        transaction.commit();
    }

    private void addToCart(String name, double price, String imagePath, String size, String category, int quantity) {
        if (userId != -1) {
            CartItem cartItem = new CartItem(0, name, price, imagePath, size, category, quantity);
            dbHelper.addOrUpdateCartItem(cartItem, userId);

            Toast.makeText(this, name + " sepete eklendi", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToFavorites() {
        if (productId != -1) {
            boolean isAdded = dbHelper.addFavoriteItem(productId);
            if (isAdded) {
                Toast.makeText(this, "Favorilere eklendi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ürün favorilere eklenemedi", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ürün bilgisi eksik", Toast.LENGTH_SHORT).show();
        }
    }
}
