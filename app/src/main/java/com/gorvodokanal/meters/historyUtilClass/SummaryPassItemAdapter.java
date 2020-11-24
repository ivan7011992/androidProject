package com.gorvodokanal.meters.historyUtilClass;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.activity.PassMetersFragment;
import com.gorvodokanal.meters.model.VodomerItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class SummaryPassItemAdapter  extends RecyclerView.Adapter< SummaryPassItemAdapter.RecycleViewViewHolder> {
    private ArrayList<VodomerItem> vodomerItems;
    private ArrayList<String> userData;
    public SummaryPassItemAdapter(ArrayList<VodomerItem> passMetersItems) {
        this.vodomerItems =  passMetersItems;
        userData = new ArrayList<String>(Collections.nCopies(passMetersItems.size(), new String()));
    }

    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {
        public TextView node;
        public TextView n_vodomer;
        public TextView enter_date;
        public TextView pokaz;
        public EditText userDataInput;
        LinearLayout baseItemPass;
        String sumData;


        public RecycleViewViewHolder(@NonNull View itemView) {//коструктор,  View itemView, этот параметор это отльный элемент RecelceView
            super(itemView);
            node = itemView.findViewById(R.id.nomerUzelValue);
            n_vodomer = itemView.findViewById(R.id.nameUzelValue);
            enter_date = itemView.findViewById(R.id.dataPassMetersValue);
            pokaz = itemView.findViewById(R.id.posledMetersValue);
            userDataInput = itemView.findViewById(R.id.userData);
            userDataInput.setMaxLines(1);
            userDataInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            userDataInput.setKeyListener(DigitsKeyListener.getInstance(true, true));
            baseItemPass = itemView.findViewById(R.id.passItemLayout);




        }
    }


    @NonNull
    @Override
    public SummaryPassItemAdapter.RecycleViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {// нужно передать разметку в наш адаптер
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pass_meters_items, viewGroup, false);
        SummaryPassItemAdapter.RecycleViewViewHolder recycleViewViewHolder = new SummaryPassItemAdapter.RecycleViewViewHolder(view);
        return recycleViewViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull SummaryPassItemAdapter.RecycleViewViewHolder recycleViewViewHolder, final int i) {
        VodomerItem vodomerItem = vodomerItems.get(i);// при помощт i свящвваем каждый элемт из ArrayList с элметом RecycleVIew


        recycleViewViewHolder.node.setText(String.valueOf(vodomerItem.getNode() + " " +  vodomerItem.getPr_vod()));
        recycleViewViewHolder.n_vodomer.setText(String.valueOf(vodomerItem.getNomerVodomer()));
        recycleViewViewHolder.enter_date.setText(String.valueOf(vodomerItem.getEnterDate()));
        recycleViewViewHolder.pokaz.setText(String.valueOf(vodomerItem.getPokaz()));
        if (vodomerItem.getDate_prom() != null) {

            ((ViewManager) recycleViewViewHolder.userDataInput.getParent()).removeView(recycleViewViewHolder.userDataInput);


        } else {

            recycleViewViewHolder.userDataInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userData.set(i, s.toString());
                    recycleViewViewHolder.sumData =  userData.set(i, s.toString());


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }



    public ArrayList<String> getUserData() {
        return userData;
    }

    @Override
    public int getItemCount() {
        return vodomerItems.size();
    }

}
