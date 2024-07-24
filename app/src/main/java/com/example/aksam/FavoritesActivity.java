package com.example.aksam;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.aksam.databinding.ActivityFavoritesBinding;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityFavoritesBinding binding;
    private ProductAdapter favoritesAdapter;
    private List<ProductItem> favoriteItemList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        loadFragments();

        binding.favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        favoriteItemList = dbHelper.getFavoriteItems();
        favoritesAdapter = new ProductAdapter(this, favoriteItemList);
        binding.favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Eğer favori ürün yoksa, kullanıcıya mesaj göster
        if (favoriteItemList.isEmpty()) {
            Toast.makeText(this, "Favori ürün bulunmamaktadır.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.searchBarContainer.getId(), new SearchBarFragment());
        transaction.replace(binding.bottomMenuContainer.getId(), new BottomMenuFragment());
        transaction.commit();
    }
}
