package com.example.mahmoudbahaa.expenses.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by MahmoudBahaa on 12/01/2019.
 */
@Entity
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)

    private int id;
    private String name;
    private String icon;
    private String type;

    @Ignore
    private Boolean Status;


    public Category(int id, String name, String icon, String type) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;

    }


    @Ignore
    public Category(String name, String icon, String type, Boolean status) {

        this.name = name;
        this.icon = icon;
        this.type = type;
        Status = status;
    }

    @Ignore
    public Category() {

    }





    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id == category.id;
    }

    @Override
    public int hashCode() {
        return id;
    }


}
