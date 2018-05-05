package com.tran.huunghia.ifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//TODO: Handle to save data into db or Files and SharePreference by Nghia
public class MainActivity extends AppCompatActivity {
    Button btnListFood;
    ImageView image;
    static String data = "";
    public OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnListFood = (Button)findViewById(R.id.btnListFood);
        image = (ImageView)findViewById(R.id.imageView);
        try
        {
            getDataFromURL();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        btnListFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListFood_Activity.class);
                startActivity(intent);
            }
        });

    }
    public void getDataFromURL() throws IOException
    {
        final Request request =  new Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/latest.php")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      try {
                                          data = response.body().string();
                                      } catch (IOException i) {
                                          i.printStackTrace();
                                      }
                                  }
                              }
                );
            }

        });
    }

    public String startNetworkConnection() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/latest.php")
                .build();
        return client.newCall(request)
                .execute()
                .body()
                .string();
    }
}
