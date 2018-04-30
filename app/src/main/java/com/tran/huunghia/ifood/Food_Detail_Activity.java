package com.tran.huunghia.ifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Food_Detail_Activity extends AppCompatActivity {
    ImageView imageMeal;
    TextView tvNameMeal;
    TextView tvIngredients;
    TextView tvInstructions;
    TextView tvArea;
    TextView tvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__detail_);
        imageMeal = (ImageView)findViewById(R.id.img_detail);
        tvNameMeal = (TextView)findViewById(R.id.tvMeal_detail);
        tvIngredients = (TextView)findViewById(R.id.tvIngredient_detail);
        tvInstructions = (TextView)findViewById(R.id.tvInstructions_detail);
        tvArea = (TextView)findViewById(R.id.tvArea_detail);
        tvCategory = (TextView)findViewById(R.id.tvCategory_detail);

        Intent i  = this.getIntent();
        tvNameMeal.setText(i.getExtras().getString("name"));
        tvCategory.setText(i.getExtras().getString("category"));
        tvIngredients.setText(i.getExtras().getString("ingredients"));
        tvInstructions.setText(i.getExtras().getString("instructions"));
        tvArea.setText(i.getExtras().getString("area"));
        Picasso.get().load(i.getExtras().getString("image")).into(imageMeal);

    }
}
