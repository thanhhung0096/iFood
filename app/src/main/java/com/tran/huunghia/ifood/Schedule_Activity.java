package com.tran.huunghia.ifood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Schedule_Activity extends AppCompatActivity {
    Food food;
    ImageView imageFood;
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_schedule);

        Intent i = getIntent();
        food = (Food) i.getSerializableExtra("food");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageFood = (ImageView) findViewById(R.id.img_schhedule);
        Picasso.get().load(food.getStrMealThumb()).into(imageFood);

        Realm.init(this);

        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec;

        spec = tabs.newTabSpec("date");
        spec.setContent(R.id.datePicker);
        spec.setIndicator("Date");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("time");
        spec.setContent(R.id.timePicker);
        spec.setIndicator("Time");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                datePicker = (DatePicker) findViewById(R.id.datePicker);
                timePicker = (TimePicker) findViewById(R.id.timePicker);
                addEventToCalendar();
                food.setSchedule(true);

                RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                        .name("schedule").build();

                Realm realm = Realm.getInstance(realmConfiguration);
                realm.beginTransaction();
                Food foodToSave = realm.copyToRealm(food);
                realm.commitTransaction();

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void addEventToCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        int gio = timePicker.getCurrentHour();
        int phut = timePicker.getCurrentMinute();
        int ngay = datePicker.getDayOfMonth();
        int thang = datePicker.getMonth() + 1;
        int nam = datePicker.getYear();

        String stringGio = String.valueOf(gio);
        String stringPhut = String.valueOf(phut);

        String stringNgay = String.valueOf(ngay);
        String stringThang = String.valueOf(thang);
        String stringNam = String.valueOf(nam);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String timeSchedule = stringNam + "-" + stringThang + "-" + stringNgay + " " + stringGio + ":" + stringPhut;
        food.setTimeSchedule(timeSchedule);
        try {
            calendar.setTime(formatter.parse(timeSchedule));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Tittle = Cooking + Name's Food
        String tittle = "Cooking " + food.getStrMeal();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", calendar.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rule", "FREQ=YEARLY");
        intent.putExtra("endTime", calendar.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", tittle);
        intent.putExtra("description", food.getStrInstructions());
        startActivity(intent);
    }
}