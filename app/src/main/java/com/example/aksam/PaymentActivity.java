package com.example.aksam;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.aksam.databinding.ActivityPaymentBinding;
import com.stripe.android.view.CardInputWidget;

public class PaymentActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "order_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final String SHARED_PREFS_NAME = "user_prefs";
    private static final String KEY_FULL_ADDRESS = "full_address";
    private static final String KEY_CITY = "city";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_NEIGHBORHOOD = "neighborhood";

    private ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFragments();
        createNotificationChannel();

        // SMS gönderme iznini kontrol et ve isteme
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }

        // Bildirim gönderme iznini kontrol et ve isteme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }

        // Adres bilgilerini SharedPreferences'dan yükle
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        binding.fullAdress.setText(sharedPreferences.getString(KEY_FULL_ADDRESS, ""));
        binding.city.setText(sharedPreferences.getString(KEY_CITY, ""));
        binding.district.setText(sharedPreferences.getString(KEY_DISTRICT, ""));
        binding.neighborhood.setText(sharedPreferences.getString(KEY_NEIGHBORHOOD, ""));

        binding.completePaymentButton.setOnClickListener(v -> {
            CardInputWidget cardInputWidget = binding.cardInputWidget;
            // Kart bilgilerini doğrulama işlemi yerine başarılı mesajı gösterme
            if (isValidCard(cardInputWidget)) {
                saveAddress();
                completePayment();
            } else {
                Toast.makeText(this, "Lütfen geçerli kart bilgilerini girin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.searchBarContainer.getId(), new SearchBarFragment());
        transaction.replace(binding.bottomMenuContainer.getId(), new BottomMenuFragment());
        transaction.commit();
    }

    private boolean isValidCard(CardInputWidget cardInputWidget) {
        // Burada kart bilgilerini doğrulama işlemi yapılabilir
        return true; // Örnek olarak her zaman geçerli kabul edelim
    }

    private void saveAddress() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FULL_ADDRESS, binding.fullAdress.getText().toString());
        editor.putString(KEY_CITY, binding.city.getText().toString());
        editor.putString(KEY_DISTRICT, binding.district.getText().toString());
        editor.putString(KEY_NEIGHBORHOOD, binding.neighborhood.getText().toString());
        editor.apply();
    }

    private void completePayment() {
        Toast.makeText(this, "Siparişiniz başarılı bir şekilde alındı", Toast.LENGTH_SHORT).show();
        sendSmsNotification();
        showNotification();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendSmsNotification() {
        String phoneNumber = "5554";
        String message = "Siparişiniz yola çıktı!";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS başarıyla gönderildi", Toast.LENGTH_SHORT).show();
            showNotification();
        } catch (Exception e) {
            Toast.makeText(this, "SMS gönderilemedi", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Sipariş Durumu")
                .setContentText("Siparişiniz yola çıktı!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Channel";
            String description = "Channel for order notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SMS izni verildi
            } else {
                Toast.makeText(this, "SMS izni gerekli", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Bildirim izni verildi
            } else {
                Toast.makeText(this, "Bildirim izni gerekli", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
