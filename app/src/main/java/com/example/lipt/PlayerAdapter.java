package com.example.lipt;

import android.content.Context;

import com.example.lipt.Database.Player;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder> {

    private List<Player> players;

    private Context context;

    public PlayerAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }


}
