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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.realm.Realm;
import io.realm.RealmResults;

public class Food_Detail_Activity extends AppCompatActivity {
    ImageView imageMeal;
    TextView foodName;
    String ingredients;
    String instructions;
    String area;
    String category;
    Food food;
    Intent i ;
    private final String[] array = {"Ingredients","Instructions","Area","Category"};
    private final String[] arrayTemp = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__detail_);
        i = this.getIntent();
        food = (Food) i.getSerializableExtra("food");
        food.setFavorite(checkFavorited(food));
        imageMeal = (ImageView) findViewById(R.id.img_detail);
        foodName = (TextView) findViewById(R.id.food_name);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String temp = food.getStrMeal();
        myToolbar.setSubtitle(temp);
        foodName.setText(temp);
        Picasso.get().load(food.getStrMealThumb()).into(imageMeal);

        for (int j = 1; j < 21; j++) {
            String nameMethod = "getStrIngredient" + j;
            try {
                Method m = food.getClass().getMethod(nameMethod);
                String ingredient = (String) m.invoke(food);
                if (ingredient.equals("")) {
                    break;
                }
                if (j == 1) {
                    ingredients += ingredient;
                    continue;
                }
                ingredients += ", " + ingredient;

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            instructions = food.getStrInstructions();
            area = food.getStrArea();
            category = food.getStrCategory();

            arrayTemp[0] = ingredients;
            arrayTemp[1] = instructions;
            arrayTemp[2] = area;
            arrayTemp[3] = category;

            final ExpandableLayoutListView expandableLayoutListView =
                    (ExpandableLayoutListView) findViewById(R.id.expanded_list_view);
            ExpandedAdapter expandedAdapter = new ExpandedAdapter(getApplicationContext(), array, arrayTemp);
            expandableLayoutListView.setAdapter(expandedAdapter);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar, menu);

//        food = (Food) i.getSerializableExtra("food");
        if (food.isFavorite())
        {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (!food.isFavorite())
                {
                    //TODO: Handle to add favourite meal into user device using db or Files by Hung
                    food.setFavorite(true);
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    Food foodRealm = realm.copyToRealm(food);
                    realm.commitTransaction();
                    Toast.makeText(this, "Favourite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    food.setFavorite(false);
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    //delete from Realm
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<Food> rs = realm.where(Food.class).equalTo("idMeal",food.getIdMeal()).findAll();
                            rs.deleteAllFromRealm();
                        }
                    });
                }

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public boolean checkFavorited(Food f)
    {
        Realm.init(this);
        Realm r = Realm.getDefaultInstance();
        RealmResults<Food> rs = r.where(Food.class).equalTo("idMeal",f.getIdMeal()).findAll();
        if(rs.size() >0)
            return true;
        return false;
    }
}


