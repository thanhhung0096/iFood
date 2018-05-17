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

/**
 * Created by Thanh Hung on 5/15/2018.
 */

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {
    private ArrayList<Category> listCategory;
    private Context context;

    public Category_Adapter(ArrayList<Category> listCategory, Context context) {
        this.listCategory = listCategory;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.category_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(listCategory.get(position).getStrCategoryThumb()).into(holder.imgCategory);
        holder.tvNameCategory.setText(listCategory.get(position).getStrCategory());
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgCategory;
        TextView tvNameCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            tvNameCategory = (TextView) itemView.findViewById(R.id.tvNameCategory);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Category category = listCategory.get(position);
            Intent i = new Intent(context, CategoryDetail.class);
            i.putExtra("category", category);
            context.startActivity(i);
        }
    }
}
