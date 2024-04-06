/**
 * Luis Hernandez, Guillermo Zendejas
 * April 6, 2024
 * PrizeAdapter.java, this describes the adapter for displaying the prize items in views
 */

package com.example.lipt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lipt.Database.Prize;
import java.util.List;

public class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder>{

    private List<Prize> prizes;

    private Context context;

    public PrizeAdapter(Context context, List<Prize> prizes) {
        this.context = context;
        this.prizes = prizes;
    }

    @NonNull
    @Override
    public PrizeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prizedisplay_layout, parent, false);
        return new PrizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrizeViewHolder holder, int position) {
        Prize prize = prizes.get(position);
        holder.prizeName.setText(prize.getName());
        holder.prizeImageView.setImageResource(prize.getImageResourceId());
    }

    @Override
    public int getItemCount() {return prizes.size();}

    public static class PrizeViewHolder extends RecyclerView.ViewHolder {

        public TextView prizeName;

        public ImageView prizeImageView;

        public PrizeViewHolder(View prizeView) {
            super(prizeView);
            prizeName = itemView.findViewById(R.id.prizeNameTextView);
            prizeImageView = itemView.findViewById(R.id.prizeImageView);
        }

    }


}
