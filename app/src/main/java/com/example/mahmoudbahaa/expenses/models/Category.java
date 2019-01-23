package com.example.mahmoudbahaa.expenses.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by MahmoudBahaa on 12/01/2019.
 */
@IgnoreExtraProperties
@Entity
public class Category implements Serializable {

    @PrimaryKey()
    private int id;
    private String name;
    private String icon;
    private String type;

    @Ignore
    private Boolean Status;

    private Boolean defaultCategory;


    public Category(int id, String name, String icon, String type,Boolean defaultCategory) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;
      this.setStatus(false);
      this.defaultCategory = defaultCategory;
    }


    @Ignore
    public Category(String name, String icon, String type,Boolean defaultCategory) {

        this.name = name;
        this.icon = icon;
        this.type = type;
        this.defaultCategory = defaultCategory;
    }

    @Ignore
    public Category() {

    }


    @Ignore
    public Category(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();

        this.id = Integer.parseInt( object.get("id").toString());
        this.name = object.get("name").toString();
        this.icon = object.get("icon").toString();
        this.type = object.get("type").toString();

        this.defaultCategory =  Boolean.parseBoolean(object.get("defaultCategory").toString());

    }



    public Boolean getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(Boolean defaultCategory) {
        this.defaultCategory = defaultCategory;
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



    public static Category[] populateData() {
        return new Category[] {
                new Category(1,"اخرى", "#8281ff", "Outcome",true),
                new Category(2,"اخرى", "#8281ff", "Income",true)
        };
    }


}
