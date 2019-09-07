package com.example.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngreidientStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int STEPS_TYPE=234;
    private static final int INGREDIENT_TYPE=456;
    private Context context;
    private Recipes recipes;
    private replace_fragments fragment_replace;

    public IngreidientStepAdapter(Context context, Recipes recipes) {
        this.context = context;
        this.recipes = recipes;
        fragment_replace=(replace_fragments) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if(viewType==INGREDIENT_TYPE) {
            View v=layoutInflater.inflate(R.layout.recipes_ingredients,parent,false);
            return new IngredientViewHolder(v);
        }else{
            View v=layoutInflater.inflate(R.layout.recipes_steps,parent,false);
            return new StepsViewHolder(v);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position<recipes.getIngredients().size()) {
            return INGREDIENT_TYPE;
        } else {
            return STEPS_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  IngredientViewHolder) {
            ((IngredientViewHolder)holder).ingredient.setText(recipes.getIngredients().get(position).getIngredient());
            ((IngredientViewHolder)holder).quantity.setText(recipes.getIngredients().get(position).getQuantity() +" "+recipes.getIngredients().get(position).getMeasure());
        } else {
            ((StepsViewHolder)holder).steps.setText(recipes.getSteps().get(position-recipes.getIngredients().size()).getShortDescription());
            ((StepsViewHolder)holder).card.setOnClickListener(v -> fragment_replace.replaceFragment(recipes.getSteps(),position-recipes.getIngredients().size()));
        }
    }

    @Override
    public int getItemCount() {
        return recipes.getIngredients().size()+recipes.getSteps().size();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ingredient_card)  CardView card;
        @BindView(R.id.txt_steps) TextView steps;
        public StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_ingredient) TextView ingredient;
        @BindView(R.id.txt_quantity) TextView quantity;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
