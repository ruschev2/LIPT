/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PokemonDisplayAdapter.java
 * this class describes the adapter used in recycler views to display Pokemon
 */

package com.example.lipt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PokemonDisplayAdapter extends RecyclerView.Adapter<PokemonDisplayAdapter.ViewHolder> {

    private List<PokemonDisplay> displays;

    public PokemonDisplayAdapter(List<PokemonDisplay> displays) {
        this.displays = displays;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemondisplay_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PokemonDisplay display = displays.get(position);
        holder.pokemonTextViewName.setText(display.getName());
        holder.pokemonImageView.setImageResource(display.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return displays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pokemonTextViewName;
        public ImageView pokemonImageView;

        public ViewHolder(View pokemonView) {
            super(pokemonView);
            pokemonTextViewName = itemView.findViewById(R.id.pokemonTextViewName);
            pokemonImageView = itemView.findViewById(R.id.pokemonImageView);
        }
    }


}
