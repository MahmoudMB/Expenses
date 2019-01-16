package com.example.mahmoudbahaa.expenses.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by MahmoudBahaa on 11/01/2019.
 */


@Entity
public class Account implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String icon;
    @Ignore
    private Boolean Status;


    public Account(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }


@Ignore
    public Account( String name, String icon, Boolean status) {
        this.name = name;
        this.icon = icon;
        Status = status;
    }


    @Ignore
    public Account() {

    }


    public int getId() {
        return id;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
