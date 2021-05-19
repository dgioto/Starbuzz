package com.example.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {

    //идентификатор напитка, выбранного пользователем
    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //получить напиток из данный интента
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        //создание курсора
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            //создаем курсор для получегия из таблицы DRINK  столбцов NAME, DESCRIPTION,
            //IMAGE_RESOURCE_ID тех записей у которых значение _id равно drinkId
            Cursor cursor = db.query("DRINK",
                    new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            //переход к первой записи в курсоре
            if (cursor.moveToFirst()){

                //получение данных напитка из курсора
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                //Если столбец FAVORITE содержит 1, это соответствует значению true
                boolean isFavorite = (cursor.getInt(3) == 1);

                //Заполнение названия напитка
                TextView name = findViewById(R.id.name);
                name.setText(nameText);

                //Заполнить описание напитка
                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                //Заполнить изображения напитка
                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                //Заполнение флажка любого напитка
                CheckBox favorite = findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();

        } catch (SQLException e){
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onFavoriteClicked(View view) {
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        new UpdateDrinkTask().execute(drinkId);
    }

    //приватный класс для работы сбазой данных в фоновом режиме
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean>{

        private  ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            //Получение значения флажка
            CheckBox favorite = findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            //значение флажка добавляем в в объект ContentValues
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];
            //получение ссылки на базу данных и обновление столбца FAVORITE
            SQLiteOpenHelper starbuzzDatabaseHelper =
                    new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                //Обновить столбец FAVORITE текущим значением флажка
                db.update("DRINK",
                        drinkValues,
                        "_id = ?",
                        new String[]{Integer.toString(drinkId)});
                db.close();
                return  true;
            } catch (SQLException e){
                return  false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success){
                Toast toast = Toast.makeText(DrinkActivity.this,
                        "Database unavailable",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}