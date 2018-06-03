package com.tran.huunghia.ifood;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by user on 29/04/2018.
 */

public class Food_Adapter extends RecyclerView.Adapter<Food_Adapter.ViewHolder> {
    private ArrayList<Food> listFood;
    private Context context;

    public Food_Adapter(ArrayList<Food> listFood, Context context) {
        this.listFood = listFood;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.food_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get().load(listFood.get(position).getStrMealThumb()).into(holder.image);
        holder.tvName.setText(listFood.get(position).getStrMeal());
        holder.tvArea.setText(listFood.get(position).getStrArea());
        if (checkSchedule(listFood.get(position))) {
            holder.tvDateSchedule.setText("Scheduled on: " + listFood.get(position).getTimeSchedule());
        }

    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public boolean checkSchedule(Food f) {
        Realm.init(context);
        Realm r = Realm.getInstance(new RealmConfiguration.Builder().name("schedule").build());
        RealmResults<Food> rs = r.where(Food.class).equalTo("idMeal", f.getIdMeal()).findAll();
        if (rs.size() > 0) {
            f.setTimeSchedule(rs.get(0).getTimeSchedule());
            return true;
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvArea;
        TextView tvDateSchedule;
        ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.imgMeal);
            tvName = (TextView) itemView.findViewById(R.id.tvNameMeal);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea);
            tvDateSchedule = (TextView) itemView.findViewById(R.id.tvTimeSchedule);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Food food = listFood.get(position);
            Intent i = new Intent(context, Food_Detail_Activity.class);
            i.putExtra("food", food);
            context.startActivity(i);
        }
    }
}

