package com.example.aksam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aksam.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.registerButton.setOnClickListener(v -> {
            String firstNameText = binding.firstName.getText().toString().trim();
            String lastNameText = binding.lastName.getText().toString().trim();
            String emailText = binding.email.getText().toString().trim();
            String passwordText = binding.password.getText().toString().trim();
            String confirmPasswordText = binding.confirmPassword.getText().toString().trim();

            if (validateInput(firstNameText, lastNameText, emailText, passwordText, confirmPasswordText)) {
                binding.progressBar.setVisibility(View.VISIBLE);
                User user = new User(firstNameText, lastNameText, emailText, passwordText);
                databaseHelper.addUser(user);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private boolean validateInput(String firstName, String lastName, String email, String password, String confirmPassword) {
        boolean isValid = true;

        if (firstName.isEmpty() || firstName.length() < 2) {
            binding.firstName.setError("Ad en az 2 karakter olmalıdır");
            isValid = false;
        } else {
            binding.firstName.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 2) {
            binding.lastName.setError("Soyad en az 2 karakter olmalıdır");
            isValid = false;
        } else {
            binding.lastName.setError(null);
        }

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

        if (!password.equals(confirmPassword)) {
            binding.confirmPassword.setError("Şifreler uyuşmuyor");
            isValid = false;
        } else {
            binding.confirmPassword.setError(null);
        }

        return isValid;
    }
}
