package com.example.lipt;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lipt.Database.Player;

import java.util.ArrayList;


public class AllUsersListFragment extends Fragment implements  PlayerAdapter.ItemClickListener {
    private PlayerViewModel playerViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_users_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.all_users_recyclerView);
        PlayerAdapter adapter = new PlayerAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        playerViewModel.getAllPlayers().observe(getViewLifecycleOwner(), allPlayers -> {
            adapter.setAllPlayersList(allPlayers);
        });

        return rootView;
    }

    @Override
    public void onDeleteClick(Player player) {
        playerViewModel.deletePlayerById(player.getUserID());
    }

    @Override
    public void onInfoButtonClick(Player player) {
        Intent intent = PlayerInfoActivity.adminActivityIntentFactory(getActivity(), player.getUserID());
        startActivity(intent);
    }
}