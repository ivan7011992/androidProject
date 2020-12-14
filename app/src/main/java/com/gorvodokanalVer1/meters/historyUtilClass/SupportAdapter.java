package com.gorvodokanalVer1.meters.historyUtilClass;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.SupportItem;

import java.util.ArrayList;
import java.util.Collections;

public class SupportAdapter  extends RecyclerView.Adapter< SupportAdapter.RecycleViewViewHolder> {
    private ArrayList<SupportItem> supportItems;
    private ArrayList<String> userData;
    public SupportAdapter(ArrayList< SupportItem> supportItems) {
        this.supportItems = supportItems;
        userData = new ArrayList<String>(Collections.nCopies(supportItems.size(), new String()));
    }

    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {
        public TextView  date_question;
        public TextView question;
        public TextView date_response;
        public TextView response;
        public EditText userDataInput;
        LinearLayout baseItemPass;
        String sumData;


        public RecycleViewViewHolder(@NonNull View itemView) {//коструктор,  View itemView, этот параметор это отльный элемент RecelceView
            super(itemView);
           date_question = itemView.findViewById(R.id.dataRequestValue);
            question = itemView.findViewById(R.id.questionValue);
           date_response = itemView.findViewById(R.id.date_response);
          response = itemView.findViewById(R.id.responseValue);


        }
    }


    @NonNull
    @Override
    public SupportAdapter.RecycleViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {// нужно передать разметку в наш адаптер
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.support_item, viewGroup, false);
        SupportAdapter.RecycleViewViewHolder recycleViewViewHolder = new SupportAdapter.RecycleViewViewHolder(view);
        return recycleViewViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull SupportAdapter.RecycleViewViewHolder recycleViewViewHolder, final int i) {
        SupportItem supportItem = supportItems.get(i);// при помощт i свящвваем каждый элемт из ArrayList с элметом RecycleVIew


        recycleViewViewHolder.date_question.setText(String.valueOf(supportItem.getDate_question()));
        recycleViewViewHolder.question.setText(String.valueOf(supportItem.getQuestion()));
        recycleViewViewHolder.date_response.setText(String.valueOf(supportItem.getDate_response()));
        recycleViewViewHolder.response.setText(String.valueOf(supportItem.getResponse()));




    }



    public ArrayList<String> getUserData() {
        return userData;
    }

    @Override
    public int getItemCount() {
        return supportItems.size();
    }

}
