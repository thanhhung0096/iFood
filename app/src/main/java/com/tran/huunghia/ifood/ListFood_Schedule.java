package com.tran.huunghia.ifood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ListFood_Schedule extends AppCompatActivity {
    RecyclerView recyclerView;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    Realm realm;
    List<Food> listSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_schedule);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.S2RSchedule);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                Intent i =getIntent();
                startActivity(i);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_schedule);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        Realm.init(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Realm.init(this);
        Realm.init(this);
        realm = Realm.getInstance(new RealmConfiguration.Builder().name("schedule").build());
        realm.setAutoRefresh(true);
        RealmResults<Food> rs = realm.where(Food.class).findAll();
        listSchedule = realm.copyFromRealm(rs);
        ScheduleAdaper scheduleAdaper = new ScheduleAdaper((ArrayList<Food>) listSchedule, getApplicationContext());
        recyclerView.setAdapter(scheduleAdaper);




    }



}
