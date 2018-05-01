package com.tran.huunghia.ifood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListFood_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button btnSearch;
    SearchView searchView;
    String Result = "";
    public static Boolean got = false;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static String data2 = "";
    public static int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        searchView = (SearchView) findViewById(R.id.searchView);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        View_init(MainActivity.data);
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
    }

    void View_init(String data) {
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

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

        Food_Adapter food_adapter = new Food_Adapter(listFood, getApplicationContext());
        recyclerView.setAdapter(food_adapter);

    }

    OkHttpClient client = new OkHttpClient();

    public void Search() {
        CharSequence search = searchView.getQuery();
        search = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + search;

        try {
            new OkHttpHandler().execute((String)search);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i = new Intent(this, ListFood_Activity.class);

        if (id == R.id.bookmark) {
            Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.home) {
            startActivity(i);
        }
        if (id == R.id.categories) {
            num = 2;
            View_init(data2);
            startActivity(i);
        }
        if (id == R.id.ingredients) {

        }
        if (id == R.id.about) {
            Toast.makeText(this, "Nhom 5", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();
        Context c;

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
//            Toast.makeText(ListFood_Activity.this, s, Toast.LENGTH_LONG).show();
            View_init(s);
            if (s.equals("{\"meals\":null}")) {
                Toast.makeText(ListFood_Activity.this, "Meal not found", Toast.LENGTH_LONG).show();
            }
        }
    }
}
