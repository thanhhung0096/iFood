package com.tran.huunghia.ifood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
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

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    Button btnSearch;
    SearchView searchView;
    Spinner spinner;
    String urlTemp = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
    String[] filters = {"Name", "Main ingredient",
            "Category", "Area"};


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

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinner = (Spinner) view.findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the filter list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, filters);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        Search();

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int pos = spinner.getSelectedItemPosition();
//                Toast.makeText(getContext(), String.valueOf(pos),Toast.LENGTH_LONG).show();
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Search();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search();
                return false;
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

    public void Search() {
        CharSequence search = searchView.getQuery();
        search = urlTemp + search;

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            urlTemp = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
        } else if (position == 1) {
            urlTemp = "https://www.themealdb.com/api/json/v1/1/filter.php?i=";
        } else if (position == 2) {
            urlTemp = "https://www.themealdb.com/api/json/v1/1/filter.php?c=";
        } else {
            urlTemp = "https://www.themealdb.com/api/json/v1/1/filter.php?a=";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        urlTemp = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
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
                return response.body().string();
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

}
