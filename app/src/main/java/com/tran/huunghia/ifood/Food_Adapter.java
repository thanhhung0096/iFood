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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvArea;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.imgMeal);
            tvName = (TextView) itemView.findViewById(R.id.tvNameMeal);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea);

        }


        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Food food = listFood.get(position);

            //Handle ingredient array to intent Food_Detail_Activity
            String ingredients = "";
            for (int i = 1; i < 21; i++) {
                String nameMethod = "getStrIngredient" + i;
                try {
                    Method m = food.getClass().getMethod(nameMethod);
                    String ingredient = (String) m.invoke(food);
                    if (ingredient.equals("")) {
                        break;
                    }
                    if (i==1){
                        ingredients += ingredient;
                        continue;
                    }
                    ingredients += ", " + ingredient;

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
            openDetailActivity(food.getStrMeal(), food.getStrMealThumb(), ingredients,
                    food.getStrInstructions(), food.getStrArea(), food.getStrCategory());
        }
    }

    private void openDetailActivity(String nameMeal, String imageMeal, String Ingredient, String instructions, String Area, String category) {
        Intent i = new Intent(context, Food_Detail_Activity.class);
        //detailActivity.putExtra("result", Parcels.wrap(result));
        //PACK DATA TO SEND
        i.putExtra("name", nameMeal);
        i.putExtra("image", imageMeal);
        i.putExtra("ingredients", Ingredient);
        i.putExtra("instructions", instructions);
        i.putExtra("area", Area);
        i.putExtra("category", category);

        //open activity
        context.startActivity(i);

    }
}

