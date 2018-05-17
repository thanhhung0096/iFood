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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoryFragment extends Fragment {


    String category = "";
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new OkHttpHandler().execute("https://www.themealdb.com/api/json/v1/1/categories.php");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayoutCategory);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_category);

        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        View_init(category);
        return view;
    }

    void View_init(final String data) {

        ArrayList<Category> listCategory = new ArrayList<Category>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("categories");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                Category category = gson.fromJson(tmp.toString(), Category.class);
                listCategory.add(category);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Category_Adapter category_adapter = new Category_Adapter(listCategory, getActivity());
        recyclerView.setAdapter(category_adapter);

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
        ArrayList<Category> listCategory = new ArrayList<Category>();
        try {
            JSONObject jsonObject = new JSONObject(dataTemp);
            JSONArray jsonArray = jsonObject.getJSONArray("categories");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                Category category = gson.fromJson(tmp.toString(), Category.class);
                listCategory.add(category);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Category_Adapter category_adapter = new Category_Adapter(listCategory, getActivity());
        recyclerView.setAdapter(category_adapter);

    }

    private class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
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
                return category = response.body().string();
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
            if (s.equals("{\"categories\":null}")) {
                Toast.makeText(getActivity(), "Category not found", Toast.LENGTH_LONG).show();
            }
        }
    }

}
