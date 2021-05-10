package com.example.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
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
                    new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            //переход к первой записи в курсоре
            if (cursor.moveToFirst()){

                //получение данных напитка из курсора
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);

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

            }

        } catch (SQLException e){
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}