package com.gorvodokanalVer1.meters.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.historyUtilClass.BindingLsAdapter;
import com.gorvodokanalVer1.meters.historyUtilClass.SupportAdapter;
import com.gorvodokanalVer1.meters.model.BindingItem;
import com.gorvodokanalVer1.meters.model.SupportItem;

import java.util.ArrayList;


public class ListBindingLsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list_binding_ls, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView listBindingDataView = (RecyclerView) getView().findViewById(R.id.listBindingLs);
        listBindingDataView.setAdapter(null);

        ArrayList<BindingItem> recyclerViews = new ArrayList<>();
        recyclerViews.add(new BindingItem( "10-6666666"));


        final BindingLsAdapter adapter = new BindingLsAdapter(recyclerViews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        listBindingDataView.setAdapter(adapter);
        listBindingDataView.setNestedScrollingEnabled(false);
        listBindingDataView.setLayoutManager(layoutManager);

     }
}