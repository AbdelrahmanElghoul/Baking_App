package com.example.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.idling.CountingIdlingResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    CountingIdlingResource idlingResource=new CountingIdlingResource("Baking_App");
    private Call <List<Recipes>> call;
    private final String RESTORE_LIST_KEY="RS_LIST";
    @BindView(R.id.recipe_name_recycler_view) RecyclerView RecipesRecyclerView;
    List<Recipes> recipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        if (getResources().getBoolean(R.bool.Tablet)) {
            RecipesRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
            Timber.d("Tablet");
        } else {
            RecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            Timber.d("Phone");
        }
        RecipesRecyclerView.setHasFixedSize(true);
        if(savedInstanceState != null){
            recipesList=savedInstanceState.getParcelableArrayList(RESTORE_LIST_KEY);
            RecipesRecyclerView.setAdapter(new recipesNameAdapter(this,recipesList));
            Timber.d("List Restored");
        }
        else if(!new Internet().hasInternetAccess(this)){
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_LONG).show();
            Timber.e("No Internet");
            return;
        }else{
            idlingResource.increment();
            getData();
            RecipesRecyclerView.setAdapter(null);
        }
    }

   public void getData(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(API.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API data=retrofit.create(API.class);
        call=data.getRecipes();
        call.enqueue(new Callback<List<Recipes>>() {
            @Override
            public void onResponse(Call<List<Recipes>> call, Response<List<Recipes>> response) {
                recipesList=response.body();
                if(recipesList != null){
                    RecipesRecyclerView.setAdapter(new recipesNameAdapter(MainActivity.this,recipesList));
                    idlingResource.decrement();
                }
            }

            @Override
            public void onFailure(Call<List<Recipes>> call, Throwable t) {
                idlingResource.decrement();
                Timber.e(t);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null)
            call.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(recipesList!=null){
            outState.putParcelableArrayList(RESTORE_LIST_KEY, (ArrayList<? extends Parcelable>) recipesList);
            Timber.d("List Saved");
        }
    }

}
