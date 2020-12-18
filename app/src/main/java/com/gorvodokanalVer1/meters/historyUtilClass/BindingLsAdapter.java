package com.gorvodokanalVer1.meters.historyUtilClass;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.activity.AppActivity;
import com.gorvodokanalVer1.meters.activity.ListBindingLsFragment;
import com.gorvodokanalVer1.meters.activity.MainActivity;
import com.gorvodokanalVer1.meters.model.BindingItem;
import com.gorvodokanalVer1.meters.model.SupportItem;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BindingLsAdapter  extends RecyclerView.Adapter< BindingLsAdapter.RecycleViewViewHolder>  {
    private ArrayList<BindingItem> bindingLs;
    private Button buttonLs;
    ListBindingLsFragment viewFragment;



    private ArrayList<String> userData;
    public BindingLsAdapter (ArrayList< BindingItem> bindingItem, ListBindingLsFragment view) {
        this.bindingLs = bindingItem;
        userData = new ArrayList<String>(Collections.nCopies(bindingLs.size(), new String()));
        viewFragment = view;
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
       recycleViewViewHolder.buttonBinding.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               removeLs(bindingItem.getUser_id());
           }
       });



    }
    @Override
    public int getItemCount() {
      return bindingLs.size();
    }

    public void removeLs(String itemLs){


        final RequestQueue mQueue = RequestQueueSingleton.getInstance(viewFragment.getContext());
        Map<String,Object> data = new HashMap<>();
        data.put("userID", itemLs);
        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.REMOVE_BINDING_LS, data, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(viewFragment.getContext() , "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {


                        String errorMessage = response.getString("message");


                        Toast.makeText(viewFragment.getContext(), String.valueOf(errorMessage), Toast.LENGTH_LONG).show();
                        return;
                    }

                    viewFragment.getBindingLsUpdate();
                    Toast.makeText(viewFragment.getContext() , "Лицевой счёт отвязан", Toast.LENGTH_LONG).show();



                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

                showErrorDialog();
            }


        });

    }

    private void  showErrorDialog(){

        Toast.makeText(viewFragment.getContext(), "Произошла ошибка, попробуйте повторить попытку позже", Toast.LENGTH_LONG).show();

    }



}
