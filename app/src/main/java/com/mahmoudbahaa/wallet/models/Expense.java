package com.mahmoudbahaa.wallet.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;


/**
 * Created by MahmoudBahaa on 11/01/2019.
 */
@IgnoreExtraProperties
@Entity

public class Expense implements Parcelable {

@PrimaryKey()
    private int id;
    private String description;
  //  private String category;
   // private String account;
    private String type;
    private float price;
    private String memo;
    private String imagePath;

    private Date createdAt;

    private int accountId;
    private int categoryId;





@Ignore
    public Expense( String description, String type, float price, Date createdAt,String memo,String imagePath,int accountId,int categoryId) {
        this.description = description;
      //  this.category = category;
    //    this.account = account;
        this.type = type;
        this.price = price;
        this.createdAt = createdAt;
        this.memo = memo;
        this.imagePath = imagePath;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }
    public Expense(int id, String description, String type, float price, Date createdAt,String memo,String imagePath,int accountId,int categoryId) {
        this.id = id;
        this.description = description;
       // this.category = category;
       // this.account = account;
        this.type = type;
        this.price = price;
        this.createdAt = createdAt;
        this.memo = memo;
        this.imagePath = imagePath;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    @Ignore
    public Expense(){}


    @Ignore
    public Expense(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();

        this.id = Integer.parseInt( object.get("id").toString());
        this.description = object.get("description").toString();
      //  this.category = object.get("category").toString();
      //  this.account = object.get("account").toString();
        this.type = object.get("type").toString();
        this.price = Float.parseFloat(object.get("price").toString());
        this.createdAt =  new Date( Long.parseLong (object.get("createdAt").toString()));
        this.memo = object.get("memo").toString();
        this.imagePath = object.get("imagePath").toString();
        this.accountId = Integer.parseInt( object.get("accountId").toString());
        this.categoryId = Integer.parseInt( object.get("categoryId").toString());



    }



    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }*/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.description);
     //   dest.writeString(this.category);
      //  dest.writeString(this.account);
        dest.writeString(this.type);
        dest.writeFloat(this.price);
        dest.writeString(this.memo);


        dest.writeString(this.imagePath);


        dest.writeSerializable(this.createdAt);
        dest.writeInt(this.accountId);
        dest.writeInt(this.categoryId);
    }

    protected Expense(Parcel in) {
        this.id =in.readInt();
        this.description = in.readString();
       // this.category = in.readString();
       // this.account=in.readString();
        this.type = in.readString();
        this.price =in.readFloat();
        this.memo =in.readString();

        this.imagePath =in.readString();

        createdAt = (java.util.Date) in.readSerializable();

        this.accountId =in.readInt();
        this.categoryId =in.readInt();
    }

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel source) {
            return new Expense(source);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };




}
