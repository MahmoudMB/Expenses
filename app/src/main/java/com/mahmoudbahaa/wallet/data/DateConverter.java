package com.mahmoudbahaa.wallet.data;

import java.util.Date;


/**
 * Created by MahmoudBahaa on 13/01/2019.
 */

import android.arch.persistence.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
