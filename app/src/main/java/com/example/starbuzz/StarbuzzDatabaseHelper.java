package com.example.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "starbuzz";
    private static final int DB_VERSION = 2;

    public StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       updateMyDatabase(db, 0, DB_VERSION);
    }

    //метод для вставки одной строки таблицы БД
    private static void insertDrink(SQLiteDatabase db, String name,
                                    String description, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1) {
            //создание таблицы DRINK
            db.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");
            //данные каждого напитка вставляются в отдельной строке
            insertDrink(db, "Latte",
                    "Espresso and steamed milk",
                    R.drawable.latte);
            insertDrink(db, "Cappuccino",
                    "Espresso, hot milk and steamed-milk foam",
                    R.drawable.cappuccino);
            insertDrink(db, "Filter",
                    "Our best drip coffee",
                    R.drawable.filter);
        }
        if (oldVersion < 2){
            //объект с описанием обновляемых данных
            ContentValues drinkValues = new ContentValues();
            drinkValues.put("DESCRIPTION", "Tasty");
            //Значение Latte подставляется вместо ? в конструкции NAME = ?
            db.update("DRINK",
                    drinkValues,
                    "NAME = ?",
                    new String[] {"Latte"});
        }
    }
}
