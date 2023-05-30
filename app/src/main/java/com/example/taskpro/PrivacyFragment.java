package com.example.taskpro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PrivacyFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<String> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);

        recyclerView = view.findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Populate the itemList with your data
        itemList = new ArrayList<>();
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");

        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        return view;
    }
}
