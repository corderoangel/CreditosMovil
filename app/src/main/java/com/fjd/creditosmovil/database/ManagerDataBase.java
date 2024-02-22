package com.fjd.creditosmovil.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fjd.creditosmovil.database.entities.FotosEntity;
import com.fjd.creditosmovil.util.contracts.SessionUser;


@Database(entities = { FotosEntity.class}, version = 1)
public abstract class ManagerDataBase extends RoomDatabase {
    private static ManagerDataBase INSTANCE;

    public static ManagerDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, ManagerDataBase.class, SessionUser.getAccess(context, SessionUser.USER) + ".db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }


    public abstract DAO getDAO();

    public void destroyInstance() {
        INSTANCE = null;
    }
}
