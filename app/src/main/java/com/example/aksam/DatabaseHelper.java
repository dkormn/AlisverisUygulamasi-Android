package com.example.aksam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ShoppingAppDB";
    private static final int DATABASE_VERSION = 14;

    // Kullanıcı Tablosu
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Ürünler Tablosu
    private static final String TABLE_PRODUCT_ITEMS = "ProductItems";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IS_FAVORITE = "is_favorite"; // Favori kolonunu ekledik

    // Sepet Ürünleri Tablosu
    private static final String TABLE_CART_ITEMS = "CartItems";
    private static final String COLUMN_CART_ITEM_ID = "id";
    private static final String COLUMN_USER_ID_CART = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_IMAGE_PATH + " TEXT,"
                + COLUMN_SIZE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART_ITEMS + "("
                + COLUMN_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_IMAGE_PATH + " TEXT,"
                + COLUMN_SIZE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_USER_ID_CART + " INTEGER"
                + ")";
        db.execSQL(CREATE_CART_TABLE);

        addSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 12) {
            db.execSQL("ALTER TABLE " + TABLE_PRODUCT_ITEMS + " ADD COLUMN " + COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0");
        }
        if (oldVersion < 14) {
            if (!columnExists(db, TABLE_CART_ITEMS, COLUMN_USER_ID_CART)) {
                db.execSQL("ALTER TABLE " + TABLE_CART_ITEMS + " ADD COLUMN " + COLUMN_USER_ID_CART + " INTEGER");
            }
        }
    }

    private boolean columnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        while (cursor.moveToNext()) {
            String existingColumnName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            if (existingColumnName.equals(columnName)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    private void addSampleData(SQLiteDatabase db) {
        addProductItem(db, "Slim Leg Fit Mid-Rise Jeans", 990.00, "https://static.zara.net/assets/public/1bf4/4824/9f8248ff8545/00db2d31151d/08197001401-e1/08197001401-e1.jpg?ts=1707901086668&w=563", "S", "Pants", 1);
        addProductItem(db, "Relaxed Fit Mid-Rise Jeans", 690.00, "https://static.zara.net/assets/public/56b2/8fd9/78d74c50a5ac/999f21493e63/06840066802-e1/06840066802-e1.jpg?ts=1712140848081&w=563", "M", "Pants", 1);
        addProductItem(db, "Black Wide-Leg Trousers With Double Waistband", 1290.00, "https://static.zara.net/photos///2023/I/0/1/p/1608/225/800/2/w/563/1608225800_6_1_1.jpg?ts=1693551242889", "S", "Pants", 1);
        addProductItem(db, "Sand Wide-Leg Trousers With Double Waistband", 1290.00, "https://static.zara.net/assets/public/f612/7948/257d4b8f83a8/7bdf9c5d8885/01608525711-e1/01608525711-e1.jpg?ts=1705594276263&w=563", "L", "Pants", 1);
        addProductItem(db, "Pleated Satin Finish Skirt", 1290.00, "https://static.zara.net/assets/public/574d/10d7/a5184bf7961b/a74d859c37f1/09878066505-e1/09878066505-e1.jpg?ts=1711120363698&w=563", "XS", "Skirt", 1);
        addProductItem(db, "Flowing Skirt", 890.00, "https://static.zara.net/assets/public/c425/ea4d/a03c4453b91b/242726c58e1d/04661409737-e2/04661409737-e2.jpg?ts=1710868158892&w=563", "M", "Skirt", 1);
        addProductItem(db, "Oversize Blazer With Padded Shoulders", 1790.00, "https://static.zara.net/photos///2024/V/0/1/p/2753/232/505/2/w/563/2753232505_6_1_1.jpg?ts=1705921156248", "M", "Jacket", 1);
        addProductItem(db, "Siyah Deri Biker Ceket", 990.00, "https://static.pullandbear.net/2/photos//2024/V/0/2/p/8710/500/800/8710500800_2_6_8.jpg?t=1686312867713&imwidth=850", "M", "Jacket", 1);
        addProductItem(db, "Draped Camisole Dress", 1590.00, "https://static.zara.net/assets/public/2780/8ff5/1193448791ea/e708dc711bb0/04764303717-e1/04764303717-e1.jpg?ts=1716823349891&w=563", "XS", "Dress", 1);
        addProductItem(db, "Mini Dress With Collar", 1290.00, "https://static.zara.net/assets/public/a669/04d8/07b9443cb2ab/7387fd6b59d6/02674620712-e1/02674620712-e1.jpg?ts=1712135167704&w=563", "M", "Dress", 1);
        addProductItem(db, "Kısa Kollu Desenli T-shirt", 590.00, "https://static.pullandbear.net/2/photos//2024/I/0/2/p/7248/529/802/7248529802_2_6_0.jpg?ts=1713195963863&imwidth=850", "M", "Tshirt", 1);
        addProductItem(db, "Batik Desenli T-shirt", 590.00, "https://static.pullandbear.net/2/photos//2024/I/0/2/p/7248/515/802/7248515802_2_6_0.jpg?ts=1713769504058&imwidth=850", "M", "Tshirt", 1);
    }

    public void addProductItem(SQLiteDatabase db, String name, double price, String imagePath, String size, String category, int quantity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_PATH, imagePath);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_QUANTITY, quantity);

        db.insert(TABLE_PRODUCT_ITEMS, null, values);
    }

    public void addOrUpdateCartItem(CartItem cartItem, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEMS + " WHERE " + COLUMN_NAME + "=? AND " + COLUMN_SIZE + "=? AND user_id=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{cartItem.getName(), cartItem.getSize(), String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY, currentQuantity + cartItem.getQuantity());
            db.update(TABLE_CART_ITEMS, values, COLUMN_CART_ITEM_ID + "=?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ITEM_ID)))});
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, cartItem.getName());
            values.put(COLUMN_PRICE, cartItem.getPrice());
            values.put(COLUMN_IMAGE_PATH, cartItem.getImagePath());
            values.put(COLUMN_SIZE, cartItem.getSize());
            values.put(COLUMN_CATEGORY, cartItem.getCategory());
            values.put(COLUMN_QUANTITY, cartItem.getQuantity());
            values.put("user_id", userId);
            db.insert(TABLE_CART_ITEMS, null, values);
        }

        cursor.close();
        db.close();
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART_ITEMS, null, "user_id=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ITEM_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));

                itemList.add(new CartItem(id, name, price, imagePath, size, category, quantity));
            }
            cursor.close();
        }
        return itemList;
    }

    public void deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART_ITEMS, COLUMN_CART_ITEM_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Kullanıcı CRUD İşlemleri
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PASSWORD},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }

    public boolean addFavoriteItem(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, 1);
        int rowsAffected = db.update(TABLE_PRODUCT_ITEMS, values, COLUMN_ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<ProductItem> getFavoriteItems() {
        List<ProductItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT_ITEMS, null, COLUMN_IS_FAVORITE + "=?", new String[]{"1"}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));

                itemList.add(new ProductItem(id, name, price, imagePath, size, category, quantity));
            }
            cursor.close();
        }
        return itemList;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PASSWORD},
                COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }
    public void clearFavoriteItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, 0);
        db.update(TABLE_PRODUCT_ITEMS, values, null, null);
        db.close();
    }

}
