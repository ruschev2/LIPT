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

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    private List<Player> allAdminsList;
    private ItemClickListener itemClickListener;

    public AdminAdapter(List<Player> allAdminsList, ItemClickListener itemClickListener) {
        this.allAdminsList = allAdminsList;
        this.itemClickListener = itemClickListener;
    }

    public void setAllAdminsList(List<Player> allAdminsList) {
        this.allAdminsList = allAdminsList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onDemoteButtonClick(Player player);
    }


    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_users_list_item, parent, false);
        return new AdminViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        Player player = allAdminsList.get(position);

        if(player.isAdmin()) {
            holder.statusLabelTextView.setText("Admin");
        } else {
            holder.statusLabelTextView.setText("User");
        }

        holder.adminNameTextView.setText(player.getUsername());
        holder.demoteAdminButton.setOnClickListener(view -> {
            if (position != RecyclerView.NO_POSITION) {
                Player item = allAdminsList.get(position);
                itemClickListener.onDemoteButtonClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allAdminsList.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        private TextView statusLabelTextView;
        private TextView adminNameTextView;
        private Button demoteAdminButton;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            statusLabelTextView = itemView.findViewById(R.id.icon_label_textView);
            adminNameTextView = itemView.findViewById(R.id.admin_name_textView);
            demoteAdminButton = itemView.findViewById(R.id.demote_admin_button);
        }
    }
}
