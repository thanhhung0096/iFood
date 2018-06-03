package com.tran.huunghia.ifood;

import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    String lastestFood = "";
    ArrayList<Food> listFood;
    Realm realm;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if(isConnected())
                new OkHttpHandler().execute("https://www.themealdb.com/api/json/v1/1/latest.php");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayoutHome);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home);
        listFood = new ArrayList<>();

        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        //TODO: Take a lot of time to load realmdb => Handle?
        Realm.init(getContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("foods.realm")
                .build();
        realm = Realm.getInstance(config);
        try {
            if(isConnected())
                View_init(lastestFood);
            else
            {
                RealmResults<Food> rs = realm.where(Food.class).findAll();
                List<Food> fs = realm.copyFromRealm(rs);
                Food_Adapter food_adapter = new Food_Adapter((ArrayList<Food>) fs, getActivity());
                recyclerView.setAdapter(food_adapter);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        View_init(lastestFood);
        return view;
    }

    void View_init(final String data) {

//        ArrayList<Food> listFood = new ArrayList<Food>();
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
        //Realm save
        realm.beginTransaction();
        realm.deleteAll();
        for(int i = 0 ; i < listFood.size();i++)
        {
            Food food = realm.copyToRealm(listFood.get(i));
        }
        realm.commitTransaction();
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

    private class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute(){
            ListFood_Activity.rotateLoading.start();
        }

        @Override
        protected String doInBackground(String... url) {
            final Request request = new Request.Builder()
                    .url(url[0])
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                return lastestFood = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";

        }

        @Override
        protected void onPostExecute(String s) {
            View_init(s);
            if (ListFood_Activity.rotateLoading.isStart())
                ListFood_Activity.rotateLoading.stop();
            if (s.equals("{\"meals\":null}")) {
                Toast.makeText(getActivity(), "Meal not found", Toast.LENGTH_LONG).show();
            }
        }
    }
    public boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }
}
