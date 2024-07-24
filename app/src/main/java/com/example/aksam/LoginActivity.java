package com.example.aksam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aksam.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isRemembered) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.loginButton.setOnClickListener(v -> {
            String emailText = binding.email.getText().toString().trim();
            String passwordText = binding.password.getText().toString().trim();

            if (validateInput(emailText, passwordText)) {
                binding.progressBar.setVisibility(View.VISIBLE);
                User user = databaseHelper.getUser(emailText, passwordText);
                binding.progressBar.setVisibility(View.GONE);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Giriş başarılı", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putInt("userId", user.getId());
                    editor.putString("firstName", user.getFirstName());
                    editor.putString("lastName", user.getLastName());
                    editor.putString("email", user.getEmail());
                    editor.apply();
                    databaseHelper.clearFavoriteItems();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Geçersiz email veya şifre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput(String email, String password) {
        boolean isValid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError("Geçerli bir email adresi girin");
            isValid = false;
        } else {
            binding.email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            binding.password.setError("Şifre en az 6 karakter olmalıdır");
            isValid = false;
        } else {
            binding.password.setError(null);
        }

        return isValid;
    }
}
