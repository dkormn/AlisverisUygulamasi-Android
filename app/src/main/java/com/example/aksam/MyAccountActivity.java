package com.example.aksam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.aksam.databinding.ActivityMyAccountBinding;

public class MyAccountActivity extends AppCompatActivity {

    private ActivityMyAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // SharedPreferences'dan kullanıcı bilgilerini al ve göster
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("firstName", "");
        String lastName = sharedPreferences.getString("lastName", "");
        String email = sharedPreferences.getString("email", "");

        binding.firstName.setText("Ad: " + firstName);
        binding.lastName.setText("Soyad: " + lastName);
        binding.email.setText("E-Mail: " + email);

        // Logout butonuna tıklama işlemi
        binding.logoutButton.setOnClickListener(v -> logout());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.bottomMenuContainer.getId(), new BottomMenuFragment());
        transaction.commit();
    }

    private void logout() {
        // SharedPreferences'daki giriş bilgilerini temizle
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Giriş ekranına yönlendir
        Intent intent = new Intent(MyAccountActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
