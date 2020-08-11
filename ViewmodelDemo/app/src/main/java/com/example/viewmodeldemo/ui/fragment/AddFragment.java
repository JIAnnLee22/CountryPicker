package com.example.viewmodeldemo.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.viewmodeldemo.ui.CountryActivity;
import com.example.viewmodeldemo.ui.custom.CalendarDialog;
import com.example.viewmodeldemo.R;
import com.example.viewmodeldemo.databinding.FragmentAddBinding;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private String nameZh;
    private String surnameEn;
    private String nameEn;
    private String type;
    private String idNum;
    private String until;
    private String birthday;
    private String gender;//性别
    private String country;
    private String phoneNum;
    private Boolean isOwner = false;
    private String mDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        click();
    }

    /**
     * 点击事件
     */
    private void click() {

        //进入识别证件的页面
        binding.credentialsSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.add_to_idcard);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        //创建日历对话
        CalendarDialog calendarDialog = new CalendarDialog(requireContext());

        //选择有效期至
        binding.untilSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    calendarDialog.showCalenderDialog(binding.setUntil);
                }
            }
        });

        //选择生日
        binding.birthdaySelect.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                calendarDialog.showCalenderDialog(binding.setBirthday);
            }
        });

        //选择国籍
        binding.countrySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), CountryActivity.class);
                startActivity(intent);
//                Navigation.findNavController(view).navigate(R.id.add_to_coutryselect);
            }
        });

        //立即支付
        binding.addPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.add_to_main);
            }
        });

    }

    private void setDate(String date) {
        this.mDate = date;
    }

    public String getDate() {
        return mDate;
    }

    private void setNameZh(String mNameZh) {
        this.nameZh = mNameZh;
    }

    public String getNameZh() {
        return nameZh;
    }

    private void setNameEn(String mNameEn) {
        this.nameEn = mNameEn;
    }

    public String getNameEn() {
        return nameEn;
    }

    private void setSurnameEn(String mSurname) {
        this.surnameEn = mSurname;
    }

    public String getSurnameEn() {
        return surnameEn;
    }

    private void setType(String mType) {
        this.type = mType;
    }

    public String getType() {
        return type;
    }

    private void setIdNum(String mIdNum) {
        this.idNum = mIdNum;
    }

    public String getIdNum() {
        return idNum;
    }

    private void setUntil(String mUntil) {
        this.until = mUntil;
    }

    public String getUntil() {
        return until;
    }

    private void setBirthday(String mBirthday) {
        this.birthday = mBirthday;
    }

    public String getBirthday() {
        return birthday;
    }

    private void setGender(String mGender) {
        this.gender = mGender;
    }

    public String getGender() {
        return gender;
    }

    private void setCountry(String mCountry) {
        this.country = mCountry;
    }

    public String getCountry() {
        return country;
    }

    private void setPhoneNum(String mPhoneNum) {
        this.phoneNum = mPhoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    private void setIsOwner(Boolean mIsOwner) {
        this.isOwner = mIsOwner;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }
}