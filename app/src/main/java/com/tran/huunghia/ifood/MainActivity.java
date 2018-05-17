package com.tran.huunghia.ifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    Button btnListFood;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        btnListFood = (Button) findViewById(R.id.btnListFood);
        image = (ImageView) findViewById(R.id.imageView);
        btnListFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListFood_Activity.class);
                startActivity(intent);
            }
        });
    }
}
