package com.mahmoudbahaa.expenses;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by MahmoudBahaa on 19/01/2019.
 */

public class DateUtility {




    public Date NextDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date newDate = calendar.getTime();
        return newDate;
    }


    public Date PrevDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date newDate = calendar.getTime();
        return newDate;
    }



}
