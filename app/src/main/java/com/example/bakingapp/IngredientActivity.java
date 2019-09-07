package com.example.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class IngredientActivity extends AppCompatActivity implements replace_fragments{

    private static final String ROTATION_KEY="Rotate";
    Recipes recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        recipes = getIntent().getExtras().getParcelable(getString(R.string.Ingredient_KEY));

        if(savedInstanceState!=null && savedInstanceState.getBoolean(ROTATION_KEY)){
            final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.steps_frame);
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            Timber.d("Removed");

            replaceFragment(recipes.getSteps(),fragment_steps.getIndex());
            if(getResources().getBoolean(R.bool.Tablet)){
                final Fragment currentFragment2 = getSupportFragmentManager().findFragmentById(R.id.ingredient_frame);
                getSupportFragmentManager().beginTransaction().remove(currentFragment2).commit();
                LoadIngredientFrag(R.id.ingredient_frame);
            }
        }
        else if(getResources().getBoolean(R.bool.Tablet)) {
            LoadIngredientFrag(R.id.ingredient_frame);
            replaceFragment(recipes.getSteps(),fragment_steps.getIndex());
        }
        else{
            LoadIngredientFrag(R.id.steps_frame);
        }

    }

    private void LoadIngredientFrag(int resources) {
        ingredient_Fragment ingredient = new ingredient_Fragment();
        Bundle b = new Bundle();
        b.putParcelable(getString(R.string.Ingredient_KEY), recipes);
        ingredient.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .add(resources, ingredient)
                .commit();
    }

    @Override
    public void replaceFragment(List<Recipes.StepsBean> bean, int index) {
        fragment_steps steps = new fragment_steps();
        Bundle b = new Bundle();
        b.putParcelableArrayList(getString(R.string.Ingredient_KEY), (ArrayList<? extends Parcelable>) bean);
        b.putInt(getString(R.string.STEPS_INDEX_KEY),index);
        steps.setArguments(b);

        if(getResources().getBoolean(R.bool.Tablet))
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_frame, steps)
                .commit();
        else{getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_frame, steps)
                .addToBackStack(null)
                .commit();
        }

        Timber.d("added to back stack");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Timber.d("Back");
        if (getSupportFragmentManager().getBackStackEntryCount() != 0 && !getResources().getBoolean( R.bool.Tablet)) {
            final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.steps_frame);
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            getSupportFragmentManager().popBackStack();
            Timber.d("BackStack Count=%s",getSupportFragmentManager().getBackStackEntryCount());
        }else{
            super.onBackPressed();
            Timber.d("BackPressed");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ROTATION_KEY,true);
        Timber.d("saved");
    }
}
