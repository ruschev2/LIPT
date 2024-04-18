/**
 * Guillermo Zendejas, Luis Hernandez
 * April 15, 2024
 * Adapter for binding list of all users (players) to a RecyclerView.
 */

package com.example.lipt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lipt.Database.Player;
import java.util.List;

/**
 * Adapter for binding list of all users (players) to a RecyclerView.
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private List<Player> allPlayersList;
    private ItemClickListener itemClickListener;
    private int loggedInId;

    public PlayerAdapter(List<Player> allPlayersList, ItemClickListener itemClickListener, int loggedInId) {
        this.allPlayersList = allPlayersList;
        this.itemClickListener = itemClickListener;
        this.loggedInId= loggedInId;
    }

    public void setAllPlayersList(List<Player> allPlayers) {
        this.allPlayersList = allPlayers;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onDeleteButtonClick(Player player);
        void onInfoButtonClick(Player player);
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

        if(player.isAdmin()) {
            holder.userIconLabelTextView.setText("Admin");
        } else {
            holder.userIconLabelTextView.setText("User");
        }

        holder.usernameTextView.setText(player.getUsername());

        if(loggedInId == player.getUserID()) {
            holder.userDeleteButton.setVisibility(View.INVISIBLE);
        }

        holder.userDeleteButton.setOnClickListener(view -> {
            if (position != RecyclerView.NO_POSITION) {
                Player item = allPlayersList.get(position);
                itemClickListener.onDeleteButtonClick(item);
            }
        });

        holder.userInfoButton.setOnClickListener(view -> {
            if (position != RecyclerView.NO_POSITION) {
                Player item = allPlayersList.get(position);
                itemClickListener.onInfoButtonClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allPlayersList.size();
    }

    /**
     * Custom ViewHolder to hold Player views from a RecyclerView item
     */
    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView userIconLabelTextView;
        private TextView usernameTextView;
        private Button userDeleteButton;
        private Button userInfoButton;


        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);

            userIconLabelTextView = itemView.findViewById(R.id.user_icon_label);
            usernameTextView = itemView.findViewById(R.id.username_textView);
            userDeleteButton = itemView.findViewById(R.id.user_delete_button);
            userInfoButton = itemView.findViewById(R.id.user_info_button);

        }
    }
}
