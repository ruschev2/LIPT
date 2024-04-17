package com.example.lipt;

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


public class AdminUsersListFragment extends Fragment implements AdminAdapter.ItemClickListener {
    private PlayerViewModel playerViewModel;
    private int loggedInId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        loggedInId = getActivity().getIntent().getIntExtra(AdminActivity.ADMIN_ACTIVITY_USER_ID,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_users_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.admin_users_recyclerView);
        AdminAdapter adapter = new AdminAdapter(new ArrayList<>(), this, loggedInId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        playerViewModel.getAllAdmins().observe(getViewLifecycleOwner(), allAdmins -> {
            adapter.setAllAdminsList(allAdmins);
        });

        return rootView;
    }


    @Override
    public void onDemoteButtonClick(Player player) {
        playerViewModel.demotePlayerFromAdmin(player.getUserID());
    }
}