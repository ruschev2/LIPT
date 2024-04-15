package com.example.lipt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lipt.Database.Player;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private List<Player> allPlayersList;


    public PlayerAdapter(List<Player> allPlayersList) {
        this.allPlayersList = allPlayersList;
    }

    public void submitList(List<Player> newList) {
        allPlayersList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_all_users_list_item, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = allPlayersList.get(position);
        holder.bind(player);

    }

    @Override
    public int getItemCount() {
        return allPlayersList.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView;
        private Button userDeleteButton;
        private Button userInfoButton;


        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_textView);
            userDeleteButton = itemView.findViewById(R.id.user_delete_button);
            userInfoButton = itemView.findViewById(R.id.user_info_button);


        }

        public void bind(Player player) {
            usernameTextView.setText(player.getUsername());

        }
    }
}
