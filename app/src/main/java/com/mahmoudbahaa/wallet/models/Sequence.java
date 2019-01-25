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
public class Sequence {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    int seq;


    @Ignore
    public Sequence(String name, int seq) {
        this.name = name;
        this.seq = seq;
    }

    public Sequence(int id, String name, int seq) {
        this.id = id;
        this.name = name;
        this.seq = seq;
    }

    @Ignore
    public Sequence() {

    }


    public int getId() {
        return id;
    }


    @Ignore
    public Sequence(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();

        this.id = Integer.parseInt( object.get("id").toString());
        this.name = object.get("name").toString();
        this.seq = Integer.parseInt( object.get("seq").toString());
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }


    public static Sequence[] populateData() {
        return new Sequence[] {
                new Sequence("Account",1),
                new Sequence("Category",2),
                new Sequence("Expense",0),

        };
    }



}
