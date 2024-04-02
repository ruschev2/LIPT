/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * PlayerViewHolder.java, this describes the viewholder to display the player profile
 */

package com.example.lipt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    private final TextView playerItemView;

    private PlayerViewHolder(View itemView) {
        super(itemView);
        playerItemView = itemView.findViewById(R.id.textView);

    }

    public void bind(String text) {
        playerItemView.setText(text);
    }

    static PlayerViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PlayerViewHolder(view);
    }




}
