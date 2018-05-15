package com.tran.huunghia.ifood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    Button btnSearch;
    SearchView searchView;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textSearchView = (TextView) searchView.findViewById(id);
        textSearchView.setTextColor(getResources().getColor(R.color.colorPrimaryDarkMyApp));
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayoutSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
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

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Search();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    void View_init(final String data) {

        ArrayList<Food> listFood = new ArrayList<Food>();
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
                //TODO: Handle a bug loop swipe refresh when no data
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshData(data);

            }
        });

    }

    public void Search() {
        CharSequence search = searchView.getQuery();
        search = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + search;

        try {
            new OkHttpHandler().execute((String) search);
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... url) {

            final Request request = new Request.Builder()
                    .url(url[0])
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";

        }

        @Override
        protected void onPostExecute(String s) {
            View_init(s);
            if (s.equals("{\"meals\":null}")) {
                Toast.makeText(getActivity(), "Meal not found", Toast.LENGTH_LONG).show();
            }
        }
    }

}
