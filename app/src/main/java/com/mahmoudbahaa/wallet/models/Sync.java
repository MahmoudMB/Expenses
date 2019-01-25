package com.mahmoudbahaa.expenses.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

/**
 * Created by MahmoudBahaa on 21/01/2019.
 */

@Entity
public class Sync {

    @PrimaryKey(autoGenerate = true)
    int id;

    Long date;

    Boolean status;


    public Sync(int id, Long date, Boolean status) {
        this.id = id;
        this.date = date;
        this.status = status;
    }



    @Ignore
    public Sync(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();

        this.id = Integer.parseInt( object.get("id").toString());
        this.date = Long.parseLong( object.get("date").toString());
        this.status = Boolean.parseBoolean(object.get("status").toString());


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }




}
