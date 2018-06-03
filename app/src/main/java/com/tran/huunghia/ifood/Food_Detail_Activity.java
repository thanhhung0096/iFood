package com.tran.huunghia.ifood;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Food_Detail_Activity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
//    ImageView imageMeal;
//    YouTubePlayerView youTubePlayerView;
    String keyVideo="";
    TextView foodName;
    String ingredients = "";
    String instructions;
    String area;
    String category;
    Food food;
    Intent i;
    YouTubePlayerSupportFragment frag;
    YouTubePlayer mYouTubePlayer;
    private final String[] array = {" Ingredients", " Instructions", " Area", " Category"};
    private final String[] arrayTemp = new String[4];

    String API_KEY = "AIzaSyCEQ0NAhUY8EF3aLBqLtqlLhiR9A4GilE8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__detail_);
        frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtubeView);
        frag.initialize( API_KEY, this);


        i = this.getIntent();
        food = (Food) i.getSerializableExtra("food");
        try {
            food.getStrYoutube();
            Init_View(food);
            mYouTubePlayer.cueVideo(getYoutubeVideoId(food.getStrYoutube()));
        }
        catch (Exception e)
        {
            String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + food.getStrMeal();
            new OkHttpHandler().execute(url);
            e.printStackTrace();
        }

    }

    public void Init_View(Food food)
    {

        food.setFavorite(checkFavorited(food));
        food.setSchedule(checkSchedule(food));
//        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeView);

        foodName = (TextView) findViewById(R.id.food_name);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String temp = food.getStrMeal();
        myToolbar.setSubtitle(temp);
        foodName.setText(temp);
//        Picasso.get().load(food.getStrMealThumb()).into(imageMeal);

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

    private class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute(){
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
            Gson gson = new Gson();
            Food f = new Food();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("meals");
                f = gson.fromJson(jsonArray.get(0).toString(),Food.class);
                Init_View(f);
                mYouTubePlayer.cueVideo(getYoutubeVideoId(f.getStrYoutube()));

            } catch (JSONException e) {
                e.printStackTrace();
            }


//            Toast.makeText(Food_Detail_Activity.this, f.getStrYoutube(), Toast.LENGTH_LONG).show();

            if (s.equals("{\"meals\":null}")) {
                Toast.makeText(Food_Detail_Activity.this, "Meal not found", Toast.LENGTH_LONG).show();
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar, menu);

//        food = (Food) i.getSerializableExtra("food");
        if (food.isFavorite()) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_white_24dp);
        }
        if(food.isSchedule()){
            menu.findItem(R.id.action_timePicker).setIcon(R.drawable.ic_time_picker_sub);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (!food.isFavorite()) {

                    //Handle to add favourite meal into user device using db or Files by Hung
                    food.setFavorite(true);
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    Food foodRealm = realm.copyToRealm(food);
                    realm.commitTransaction();
                    Toast.makeText(this, "Added to your Favourite Food!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    food.setFavorite(false);
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    //delete from Realm
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<Food> rs = realm.where(Food.class).equalTo("idMeal", food.getIdMeal()).findAll();
                            rs.deleteAllFromRealm();
                        }
                    });
                }

                break;
            case R.id.action_timePicker:

                if(food.isSchedule())
                {
                    item.setIcon(R.drawable.ic_time_picker);
                    food.setSchedule(false);

                    //delete from realm
                    Realm realm = Realm.getInstance(new RealmConfiguration.Builder().name("schedule").build());
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<Food> rs = realm.where(Food.class).equalTo("idMeal", food.getIdMeal()).findAll();
                            rs.deleteAllFromRealm();
                        }
                    });
                }
                else
                {
                    Intent intent = new Intent(this, Schedule_Activity.class);
                    intent.putExtra("food", food);
                    startActivity(intent);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public boolean checkSchedule(Food f) {
        Realm.init(this);
        Realm r = Realm.getInstance(new RealmConfiguration.Builder().name("schedule").build());
        RealmResults<Food> rs = r.where(Food.class).equalTo("idMeal", f.getIdMeal()).findAll();
        if (rs.size() > 0)
            return true;
        return false;
    }

    public boolean checkFavorited(Food f) {
        Realm.init(this);
        Realm r = Realm.getDefaultInstance();
        RealmResults<Food> rs = r.where(Food.class).equalTo("idMeal", f.getIdMeal()).findAll();
        if (rs.size() > 0)
            return true;
        return false;
    }

    public static String getYoutubeVideoId(String youtubeUrl)
    {
        String video_id="";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
        {

            String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches())
            {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mYouTubePlayer = youTubePlayer;

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError())
        {
            youTubeInitializationResult.getErrorDialog(Food_Detail_Activity.this, 100);
        }
        else
        {
            Toast.makeText(this,"Error!" ,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
            frag.initialize( API_KEY, this);

    }
}


