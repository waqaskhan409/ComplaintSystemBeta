package com.example.complaintsystembeta.interfaace;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.complaintsystembeta.model.PermanentLogin;

import java.util.List;

@Dao
public interface PermanentLoginDao {

    @Insert
    void insert(PermanentLogin permanentLogin);

    @Update
    void update(PermanentLogin permanentLogin);

    @Delete
    void delete(PermanentLogin permanentLogin);

    @Query("select * from PermanentLogin")
    List<PermanentLogin> fetchData();

    @Query("UPDATE PermanentLogin SET isLoggedIn=:isUserLoggedIn where CNIC like :cnic")
    void updateUser(String cnic, boolean isUserLoggedIn);
}
