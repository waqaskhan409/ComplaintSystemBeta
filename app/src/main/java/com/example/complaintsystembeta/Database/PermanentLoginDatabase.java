package com.example.complaintsystembeta.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.complaintsystembeta.interfaace.PermanentLoginDao;
import com.example.complaintsystembeta.model.PermanentLogin;

@Database(entities = {PermanentLogin.class},  version = 3, exportSchema = false)
public abstract class PermanentLoginDatabase extends RoomDatabase {

    private static PermanentLoginDatabase instance;

    public abstract PermanentLoginDao permanentLoginDao();

    public static synchronized PermanentLoginDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PermanentLoginDatabase.class, "PermanentLogin")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsynTask(instance).execute();
        }

    };

    private static class PopulateDbAsynTask extends AsyncTask<Void, Void, Void>{
        private PermanentLoginDao dao;

        public PopulateDbAsynTask(PermanentLoginDatabase db) {
            this.dao = db.permanentLoginDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
