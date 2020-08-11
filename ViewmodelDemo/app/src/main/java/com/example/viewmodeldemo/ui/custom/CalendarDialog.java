package com.example.viewmodeldemo.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.viewmodeldemo.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import static android.content.ContentValues.TAG;


public class CalendarDialog extends AlertDialog {

    private static String mDate;
    private Context mContext;
    //    private String mDate;
    private static int nowYear = 0;
    private static int nowMonth = 0;
    private static int nowDay = 0;

    /**
     * 点击日历图标弹出的对话框
     */

    public CalendarDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CalendarDialog(Context context, int nowYear, int nowMonth, int nowDay) {
        super(context);
        this.mContext = context;
        setNowYear(nowYear);
        setNowMonth(nowMonth);
        setNowDay(nowDay);
//        this.nowYear = nowYear;
//        this.nowMonth = nowMonth;
//        this.nowDay= nowDay;
    }


    private void setDate(String date) {
        this.mDate = date;
    }

    public static String getDate() {
        return mDate;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showCalenderDialog(TextView date) {
        //获取开源日历的布局文件
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup viewGroup = findViewById(R.id.calendar_layout);
        View layout = inflater.inflate(R.layout.calendar, viewGroup);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(layout).create();
        Button nextYear = layout.findViewById(R.id.next_year);
        Button lastYear = layout.findViewById(R.id.last_year);
        TextView year = layout.findViewById(R.id.year);
        CalendarView calendarView = layout.findViewById(R.id.calendar_show);
//        CalendarView calendarView = binding.calendarShow;
        Log.d(TAG, "onCalendarSelect: " + getNowYear());

        //设定显示的年份
//        if (getNowYear() == 0) {
//            setNowDay(calendarView.getCurYear());
//            setNowMonth(calendarView.getCurMonth());
//            setNowDay(calendarView.getCurDay());
////            nowYear = calendarView.getCurYear();
////            nowMonth = calendarView.getCurMonth();
////            nowDay = calendarView.getCurDay();
//        }

        year.setText(getNowYear() + "-" + getNowMonth());

        calendarView.scrollToCalendar(getNowYear(), getNowMonth(), getNowDay());

        //年份+1
        nextYear.setOnClickListener(view -> {
//            calendarView.scrollToCalendar(nowYear + 1, calendarView.getCurMonth(), calendarView.getCurDay());
            setNowYear(getNowYear() + 1);
            calendarView.scrollToCalendar(getNowYear(), getNowMonth(), getNowDay());

        });
        //年份-1
        lastYear.setOnClickListener(view -> {
//            calendarView.scrollToCalendar(nowYear - 1, calendarView.getCurMonth(), calendarView.getCurDay());
            setNowYear(getNowYear() - 1);
            calendarView.scrollToCalendar(getNowYear(), getNowMonth(), getNowDay());
        });
        //监听日历的点击传到预计出行日期
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {

                year.setText(calendar.getYear() + "-" + calendar.getMonth());

                if (isClick) {
                    String dateSelect = calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
                    Log.d(TAG, "onCalendarSelect: " + calendarView.getCurDay());
                    setDate(dateSelect);
                    date.setText(getDate());
                    alertDialog.dismiss();
                    //选择后设置为选中日期，然后被fragment获取再次传进来就是选择的日期
                    setNowYear(calendar.getYear());
                    setNowMonth(calendar.getMonth());
                    setNowDay(calendar.getDay());
                }
            }
        });
        alertDialog.show();
    }

    public static int getNowYear() {
        return nowYear;
    }

    public void setNowYear(int nowYear) {
        this.nowYear = nowYear;
    }

    public static int getNowMonth() {
        return nowMonth;
    }

    public void setNowMonth(int nowMonth) {
        this.nowMonth = nowMonth;
    }

    public static int getNowDay() {
        return nowDay;
    }

    public void setNowDay(int nowDay) {
        this.nowDay = nowDay;
    }
}
