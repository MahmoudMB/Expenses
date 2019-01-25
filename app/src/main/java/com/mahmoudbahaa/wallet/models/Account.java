package com.mahmoudbahaa.expenses.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by MahmoudBahaa on 11/01/2019.
 */

@IgnoreExtraProperties
@Entity

public class Account implements Serializable{

    @PrimaryKey()
    private int id;
    private String name;
    private String icon;
    private float total;
    @Ignore
    private Boolean Status;

    private Boolean defaultAccount;


    public Account(int id, String name,float total, String icon,Boolean defaultAccount ) {
        this.id = id;
        this.name = name;
        this.total = total;
        this.icon = icon;
        this.setStatus(false);
        this.defaultAccount = defaultAccount;
    }




@Ignore
    public Account( String name,float total, String icon,Boolean defaultAccount) {
        this.name = name;
    this.total = total;
    this.icon = icon;
        this.defaultAccount = defaultAccount;
    }


    @Ignore
    public Account() {

    }



    @Ignore
    public Account(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();



        this.id = Integer.parseInt( object.get("id").toString());
        this.name = object.get("name").toString();
        this.total = Float.parseFloat(object.get("total").toString());
        this.icon = object.get("icon").toString();
        this.defaultAccount = Boolean.parseBoolean(object.get("defaultAccount").toString());
    }


    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Boolean getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(Boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
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


    public static Account[] populateData() {
        return new Account[] {
                new Account(1,"الحساب الاساسي",0 ,"#8281ff", true)
        };
    }




}
