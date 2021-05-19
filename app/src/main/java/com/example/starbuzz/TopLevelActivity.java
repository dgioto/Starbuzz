package com.example.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TopLevelActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor favoritesCursor;

    //add BottomNavigationView
//    private ImageView imgMap, imgDial, imgMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        
        //вызов нового метода
        setupOptionsListView();

        setupFavoritesListView();

        //add BottomNavigationView
//        imgMap = findViewById(R.id.action_map);
//        imgDial = findViewById(R.id.action_dial);
//        imgMail = findViewById(R.id.action_mail);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                (item) ->{
                    switch (item.getItemId()){
                        case R.id.action_map:
//                            imgMap.setVisibility(View.VISIBLE);
//                            imgDial.setVisibility(View.GONE);
//                            imgMail.setVisibility(View.GONE);
                            break;
                        case R.id.action_dial:
//                            imgMap.setVisibility(View.GONE);
//                            imgDial.setVisibility(View.VISIBLE);
//                            imgMail.setVisibility(View.GONE);
                            break;
                        case R.id.action_mail:
//                            imgMap.setVisibility(View.GONE);
//                            imgDial.setVisibility(View.GONE);
//                            imgMail.setVisibility(View.VISIBLE);
                            break;
                    }
                    return false;
                }
        );
    }

    private void setupOptionsListView() {
        //создание объект слушателя
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                if (position == 0){
                    //Если пользователь выбрал Drink в списковом представлении list_options,
                    // открыть активность DrinkCategoryActivity
                    Intent intent = new Intent(
                            TopLevelActivity.this,
                            DrinkCategoryActivity.class);
                    startActivity(intent);
                }
                else if (position == 1){

                }
                else if (position == 2){

                }
            }
        };

        //Добавление слушателя к списковому представлению
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }

    //Метод для заполнения списка list_favorites и реагирования на щелчки
    private void setupFavoritesListView() {
        //Заполнение списка list_favorites из курсора
        ListView listFavorites = findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            //получаем ссілку на базу данніх
            db = starbuzzDatabaseHelper.getReadableDatabase();
            //Списковое представление list_favorites используется для получения данных
            favoritesCursor = db.query("DRINK",
                    new String[] {"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(TopLevelActivity.this,
                    android.R.layout.simple_list_item_1,
                    favoritesCursor,
                    new String[] {"NAME"},
                    new int[] {android.R.id.text1}, 0);
            //Адаптер курсора связывается со списковым представлением
            listFavorites.setAdapter(favoriteAdapter);
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        //Переход к при выборе напитка
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, DrinkActivity.class);
                //Запустить DrinkActivity и передать идентификатор выбранного напитка
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int)id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //создаем новую версию курсора
        Cursor newCursor = db.query("DRINK",
                new String[]{"_id", "NAME"},
                "FAVORITE = 1",
                null, null, null, null);
        ListView listFavorites = findViewById(R.id.list_favorites);
        CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
        //курсор используемый list_favorites, заменяется новім курсором
        adapter.changeCursor(newCursor);
        //значение favoritesCursor заменяется новым курсором, чтобы его можно было закрыть в методе
        //onDestroy() активности
        favoritesCursor = newCursor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }
}