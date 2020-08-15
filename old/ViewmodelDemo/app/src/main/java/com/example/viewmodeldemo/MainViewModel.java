package com.example.viewmodeldemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.viewmodeldemo.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个viewmodel类，还在研究如何在adapter中使用viewmodel
 */
public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<List<Users>> nameList;
    private List<String> usersList;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public List<String> getUsersList(){
        if(usersList == null){
            usersList = new ArrayList<>();
            usersList.add("dsfs");
            usersList.add("dsfs");
            usersList.add("dsfs");
        }
        return usersList;
    }

    public void addName(String mName){
        usersList.add(mName);
    }

    public void delete(int i) {
        usersList.remove(i);
    }
//    public MutableLiveData<List<Users>> getName(){
//        if(nameList == null){
//            nameList = new MutableLiveData<List<Users>>();
//            usersList = new ArrayList<Users>();
//            usersList.add(new Users("lfidjsl"));
//            nameList.setValue(usersList);
//        }
//        return nameList;
//    }
//
//    public void add(Users users){
//        usersList.add(users);
//        nameList.setValue(usersList);
//    }
//    public void delete(Users users){
//        nameList.getValue().remove(users);
//        nameList.setValue(usersList);
//    }
}