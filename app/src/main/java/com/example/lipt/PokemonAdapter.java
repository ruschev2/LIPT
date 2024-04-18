/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PokemonDisplayAdapter.java
 * this class describes the adapter used in recycler views to display Pokemon
 */

package com.example.lipt;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lipt.Database.Pokemon;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemons;
    private Context context;

    public PokemonAdapter(Context context, List<Pokemon> pokemons) {
        this.context = context;
        this.pokemons = pokemons;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemondisplay_layout, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.pokedexNumber.setText(String.valueOf(pokemon.getPokedexNumber()));
        holder.pokemonNameTextView.setText(pokemon.getName());
        holder.pokemonImageView.setImageResource(pokemon.getImageResourceId());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, pokemon.getSoundResourceId());
                Log.d(MainActivity.TAG, "Sound ID for Pokemon no. " + pokemon.getPokedexNumber() + ": " + pokemon.getSoundResourceId());
                mediaPlayer.start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {

        public TextView pokedexNumber;
        public TextView pokemonNameTextView;
        public ImageView pokemonImageView;
        Button button;

        public PokemonViewHolder(View pokemonView) {
            super(pokemonView);
            pokedexNumber = itemView.findViewById(R.id.pokedexNumberText);
            pokemonNameTextView = itemView.findViewById(R.id.pokemonNameTextView);
            pokemonImageView = itemView.findViewById(R.id.pokemonImageView);
            button = itemView.findViewById(R.id.pokedexSoundButton);
        }
    }

}
