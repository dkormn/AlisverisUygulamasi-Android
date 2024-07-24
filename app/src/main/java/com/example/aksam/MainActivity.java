package com.example.aksam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.aksam.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ProductAdapter adapter;
    private List<ProductItem> productList;
    private DatabaseHelper dbHelper;
    private Button sortAscendingButton, sortDescendingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        sortAscendingButton = binding.sortAscendingButton;
        sortDescendingButton = binding.sortDescendingButton;

        dbHelper = new DatabaseHelper(this);

        // Fragmentları ekleme
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.searchBarContainer.getId(), new SearchBarFragment());
        transaction.replace(binding.bottomMenuContainer.getId(), new BottomMenuFragment());
        transaction.commit();

        // Tüm ürünleri yükle
        productList = getProductItemsFromDatabase(null, null);
        adapter = new ProductAdapter(this, productList);
        binding.recyclerView.setAdapter(adapter);

        sortAscendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(productList, new Comparator<ProductItem>() {
                    @Override
                    public int compare(ProductItem o1, ProductItem o2) {
                        return Double.compare(o1.getPrice(), o2.getPrice());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });

        sortDescendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(productList, new Comparator<ProductItem>() {
                    @Override
                    public int compare(ProductItem o1, ProductItem o2) {
                        return Double.compare(o2.getPrice(), o1.getPrice());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }

    private List<ProductItem> getProductItemsFromDatabase(@Nullable String category, @Nullable ProductItem selectedProduct) {
        List<ProductItem> itemList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        if (selectedProduct != null) {
            String selection = "category = ? AND price BETWEEN ? AND ?";
            String[] selectionArgs = {
                    selectedProduct.getCategory(),
                    String.valueOf(selectedProduct.getPrice() * 0.8), // Fiyat aralığı (%20 düşük)
                    String.valueOf(selectedProduct.getPrice() * 1.2)  // Fiyat aralığı (%20 yüksek)
            };
            cursor = db.query("ProductItems", null, selection, selectionArgs, null, null, null);
        } else if (category != null && !category.isEmpty()) {
            String selection = "category LIKE ?";
            String[] selectionArgs = {"%" + category + "%"};
            cursor = db.query("ProductItems", null, selection, selectionArgs, null, null, null);
        } else {
            cursor = db.query("ProductItems", null, null, null, null, null, null);
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                String size = cursor.getString(cursor.getColumnIndexOrThrow("size"));
                String categoryResult = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                itemList.add(new ProductItem(id, name, price, imagePath, size, categoryResult, quantity));
            }
            cursor.close();
        }
        return itemList;
    }

    public void updateProductList(String category) {
        productList = getProductItemsFromDatabase(category, null);
        adapter.updateList(productList);
    }

    public void updateSimilarProductList(ProductItem selectedProduct) {
        productList = getProductItemsFromDatabase(null, selectedProduct);
        adapter.updateList(productList);
    }
}
