package com.tran.huunghia.ifood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    RotateLoading rotateLoading;
    ArrayList<Food> listFood;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rotateLoading.isStart()){
            rotateLoading.stop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listFood = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayoutHome);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home);
        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateloading);
        rotateLoading.start();
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        //Custom Design View in Layout Manager
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
//                linearLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        Realm.init(getContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("foods.realm")
                .build();
        Realm realm = Realm.getInstance(config);
        if(!MainActivity.data.equals("")) {
            View_init(MainActivity.data);
            realm.beginTransaction();
            realm.deleteAll();
            for(int i = 0 ; i < listFood.size();i++)
            {
                Food food = realm.copyToRealm(listFood.get(i));
            }
            realm.commitTransaction();

        }
        else
        {
            RealmResults<Food> rs = realm.where(Food.class).findAll();
            ArrayList<Food> fs = new ArrayList<>(rs);
            Food_Adapter food_adapter = new Food_Adapter(fs, getActivity());
            recyclerView.setAdapter(food_adapter);
        }
        return view;

    }

    void View_init(final String data) {

//        listFood = new ArrayList<Food>();
        try {
            //getDataFromURL("https://www.themealdb.com/api/json/v1/1/latest.php";
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("meals");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                Food food = gson.fromJson(tmp.toString(), Food.class);
                listFood.add(food);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Food_Adapter food_adapter = new Food_Adapter(listFood, getActivity());
        recyclerView.setAdapter(food_adapter);

        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshData(data);
            }
        });

    }

    public void swipeRefreshData(String dataTemp) {
        ArrayList<Food> listFood = new ArrayList<Food>();
        try {
            JSONObject jsonObject = new JSONObject(dataTemp);
            JSONArray jsonArray = jsonObject.getJSONArray("meals");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                Food food = gson.fromJson(tmp.toString(), Food.class);
                listFood.add(food);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Food_Adapter food_adapter = new Food_Adapter(listFood, getActivity());
        recyclerView.setAdapter(food_adapter);

    }
}
