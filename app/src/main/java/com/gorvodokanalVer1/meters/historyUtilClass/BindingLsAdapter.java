package com.gorvodokanalVer1.meters.historyUtilClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.BindingItem;
import com.gorvodokanalVer1.meters.model.SupportItem;

import java.util.ArrayList;
import java.util.Collections;

public class BindingLsAdapter  extends RecyclerView.Adapter< BindingLsAdapter.RecycleViewViewHolder>  {
    private ArrayList<BindingItem> bindingLs;

    private ArrayList<String> userData;
    public BindingLsAdapter (ArrayList< BindingItem> bindingItem) {
        this.bindingLs = bindingItem;
        userData = new ArrayList<String>(Collections.nCopies(bindingLs.size(), new String()));

    }
//  public static String bindingLsItems;
//    public BindingLsAdapter(String bindingItem) {
//        this.bindingLsItems = bindingItem;
//
//    }



    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {
        public TextView loginBinding;
        Button buttonBinding;


        public RecycleViewViewHolder(@NonNull View itemView) {//коструктор,  View itemView, этот параметор это отльный элемент RecelceView
            super(itemView);
            loginBinding = itemView.findViewById(R.id.loginBinding);
            buttonBinding = itemView.findViewById(R.id. buttonBinding);


        }
    }



    @NonNull
    @Override
    public BindingLsAdapter.RecycleViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.binding_account_items, viewGroup, false);
        BindingLsAdapter.RecycleViewViewHolder recycleViewViewHolder = new BindingLsAdapter.RecycleViewViewHolder(view);
        return recycleViewViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull BindingLsAdapter.RecycleViewViewHolder recycleViewViewHolder, final int i) {

        BindingItem bindingItem = bindingLs.get(i);
       recycleViewViewHolder.loginBinding.setText(bindingItem.getLoginls());



    }
    @Override
    public int getItemCount() {
      return bindingLs.size();
    }
}
