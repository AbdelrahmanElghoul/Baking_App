package com.example.bakingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ingredient_Fragment extends Fragment {


    @BindView(R.id.steps_recycler_view) RecyclerView StepsRecyclerView;
    Recipes recipes;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recipes=getArguments().getParcelable(getString(R.string.Ingredient_KEY));

        StepsRecyclerView.setHasFixedSize(true);
        StepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StepsRecyclerView.setAdapter(new IngredientStepAdapter(getContext(),recipes));

    }


}
