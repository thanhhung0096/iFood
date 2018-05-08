package com.tran.huunghia.ifood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListFood_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static String hottestFood = "";
    public OkHttpClient client2 = new OkHttpClient();
    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    int[] tabIcons = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_search_white_24dp,
            R.drawable.ic_whatshot_white_24dp,
            R.drawable.ic_favorite_white_24dp_2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_);

        try {
            getHottestFoodFromURL();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Realm.init(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Handling ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addTabs(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment());
        adapter.addFrag(new SearchFragment());
        adapter.addFrag(new HotMealFragment());
        adapter.addFrag(new FavouriteFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
    }

    public void getHottestFoodFromURL() throws IOException {
        final Request request = new Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/random.php")
                .build();
        client2.newCall(request).enqueue(new Callback() {
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
                                          hottestFood = response.body().string();
                                      } catch (IOException i) {
                                          i.printStackTrace();
                                      }
                                  }
                              }
                );
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i = new Intent(this, ListFood_Activity.class);
        switch (id) {
            case R.id.home:
                startActivity(i);
                break;
            case R.id.collection:
                //TODO: Downloaded video will put here
                break;
            case R.id.schedule:

                break;
            case R.id.about:
                Toast.makeText(this, "Nhom 5", Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.START);
        return true;
    }
}
