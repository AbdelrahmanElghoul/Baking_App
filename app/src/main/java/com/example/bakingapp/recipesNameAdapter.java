package com.example.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class recipesNameAdapter extends RecyclerView.Adapter<recipesNameAdapter.mViewHolder> {

    public recipesNameAdapter(Context context, List<Recipes> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    private Context context;
    private List<Recipes> recipes;

    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.recipes_name,parent,false);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        if (position==0) {
            holder.setIsInTheMiddle(true);
        }else{ holder.setIsInTheMiddle(false);}

        holder.recipesName.setText(recipes.get(position).getName());
        if(!recipes.get(position).getImage().isEmpty()){
            Picasso.get().load(recipes.get(position).getImage()).error(R.drawable.cake).into(holder.recipeImage);
        }

        holder.recipeCard.setOnClickListener(v -> {
            Intent i=new Intent(context,IngredientActivity.class);
            Bundle b=new Bundle();
            b.putParcelable(context.getString(R.string.Ingredient_KEY),recipes.get(position));
            i.putExtras(b);

            SharedPreferences sharedPreferences=context.getSharedPreferences(context.getString(R.string.app_name_KEY),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString( context.getString(R.string.Ingredient_KEY), new Gson().toJson( recipes.get(position).getIngredients()));
            editor.apply();
            int[] WidgetIds=AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,IngredientWidget.class));
            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(WidgetIds,R.id.ingredient_list_widget);
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        private boolean mIsInTheMiddle = false;

        @BindView(R.id.txt_recipes) TextView recipesName;
        @BindView(R.id.recipe_img) ImageView recipeImage;
        @BindView(R.id.recipes_card) CardView recipeCard;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        boolean getIsInTheMiddle() {
            return mIsInTheMiddle;
        }

        void setIsInTheMiddle(boolean isInTheMiddle) {
            mIsInTheMiddle = isInTheMiddle;
        }
    }
}
