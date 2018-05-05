package com.tran.huunghia.ifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.squareup.picasso.Picasso;

public class Food_Detail_Activity extends AppCompatActivity {
    ImageView imageMeal;
    TextView foodName;
    String ingredients;
    String instructions;
    String area;
    String category;

    private final String[] array = {"Ingredients","Instructions","Area","Category"};
    private final String[] arrayTemp = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__detail_);

//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
//                R.layout.view_row, R.id.header_text, array);
//        final ExpandableLayoutListView expandableLayoutListView =
//                (ExpandableLayoutListView) findViewById(R.id.listview);
//
//        expandableLayoutListView.setAdapter(arrayAdapter);



        imageMeal = (ImageView) findViewById(R.id.img_detail);
        foodName = (TextView) findViewById(R.id.food_name);
//        tvIngredients = (TextView)findViewById(R.id.tvIngredient_detail);
//        tvInstructions = (TextView)findViewById(R.id.tvInstructions_detail);
//        tvArea = (TextView)findViewById(R.id.tvArea_detail);
//        tvCategory = (TextView)findViewById(R.id.tvCategory_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);

        Intent i = this.getIntent();
        String temp = i.getExtras().getString("name");
        myToolbar.setSubtitle(temp);
        foodName.setText(temp);
        ingredients = i.getExtras().getString("ingredients");
        instructions = i.getExtras().getString("instructions");
        area = i.getExtras().getString("area");
        category = i.getExtras().getString("category");
        Picasso.get().load(i.getExtras().getString("image")).into(imageMeal);
        arrayTemp[0] = ingredients;
        arrayTemp[1] = instructions;
        arrayTemp[2] = area;
        arrayTemp[3] = category;

        final ExpandableLayoutListView expandableLayoutListView =
                (ExpandableLayoutListView) findViewById(R.id.expanded_list_view);
        ExpandedAdapter expandedAdapter = new ExpandedAdapter(getApplicationContext(),array,arrayTemp);
        expandableLayoutListView.setAdapter(expandedAdapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:

                //TODO: Handle to add favourite meal into user device using db or Files by Hung
                Toast.makeText(this, "Favourite", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_white_24dp);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
