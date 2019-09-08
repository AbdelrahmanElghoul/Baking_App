package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientWidgetFactory(getApplicationContext());
    }

    class IngredientWidgetFactory implements RemoteViewsFactory{
        Context context;
        List<Recipes.IngredientsBean> ingredientsBeans;
        public IngredientWidgetFactory(Context context) {
            this.context = context;

        }

        @Override
        public void onCreate() {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name_KEY), MODE_PRIVATE);
            String json = sharedPreferences.getString(getString(R.string.Ingredient_KEY), "");
            if (!json.isEmpty()) {
                Type type = new TypeToken<List<Recipes.IngredientsBean>>() {
                }.getType();
                ingredientsBeans = gson.fromJson(json, type);
                if (ingredientsBeans == null)
                    this.ingredientsBeans = new ArrayList<>();
            }
        }
        @Override
        public void onDataSetChanged() {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name_KEY), MODE_PRIVATE);
            String json = sharedPreferences.getString(getString(R.string.Ingredient_KEY), null);
            if (json!=null &&!json.isEmpty()) {
                Type type = new TypeToken<List<Recipes.IngredientsBean>>() {
                }.getType();
                ingredientsBeans = gson.fromJson(json, type);
                if (ingredientsBeans == null)
                    this.ingredientsBeans = new ArrayList<>();
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(ingredientsBeans==null)
                return 0;
            return ingredientsBeans.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.recipes_ingredients);
            views.setTextViewText(R.id.txt_ingredient,ingredientsBeans.get(position).getIngredient());
            views.setTextViewText(R.id.txt_quantity
                    ,ingredientsBeans.get(position).getQuantity()+" "+ingredientsBeans.get(position).getMeasure());

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
