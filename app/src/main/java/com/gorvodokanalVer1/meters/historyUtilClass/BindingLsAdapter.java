package com.gorvodokanalVer1.meters.historyUtilClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.SupportItem;

import java.util.ArrayList;

public class BindingLsAdapter  extends RecyclerView.Adapter< BindingLsAdapter.RecycleViewViewHolder>  {

  public static ArrayList<String> bindingLsItems;


    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {
        public TextView loginBinding;
        Button buttonBinding;


        public RecycleViewViewHolder(@NonNull View itemView) {//коструктор,  View itemView, этот параметор это отльный элемент RecelceView
            super(itemView);
            loginBinding = itemView.findViewById(R.id.loginBinding);
            buttonBinding = itemView.findViewById(R.id. buttonBinding);
            bindingLsItems.add(0, "10-6666666");

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
      String login = bindingLsItems.get(i);

        recycleViewViewHolder.loginBinding.setText(login);





    }
    @Override
    public int getItemCount() {
        return 0;
    }
}
