package com.tran.huunghia.ifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CategoryDetail extends AppCompatActivity {

    ImageView imageCategory;
    TextView categoryName;
    TextView categoryDescription;
    Category category;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        intent = this.getIntent();
        category = (Category) intent.getSerializableExtra("category");
        imageCategory = (ImageView) findViewById(R.id.img_category_detail);
        categoryName = (TextView) findViewById(R.id.category_name);
        categoryDescription = (TextView) findViewById(R.id.category_description);

        categoryName.setText(category.getStrCategory());
        categoryDescription.setText(category.getStrCategoryDescription());
        categoryDescription.setMovementMethod(new ScrollingMovementMethod());
        Picasso.get().load(category.getStrCategoryThumb()).into(imageCategory);
    }
}
