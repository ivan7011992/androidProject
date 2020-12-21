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
        import com.gorvodokanalVer1.meters.model.HistoryItem;
        import com.gorvodokanalVer1.meters.model.SummaryHistoryItem;
        import com.gorvodokanalVer1.meters.model.SupportItem;

        import java.util.ArrayList;
        import java.util.Collections;

public class HistoryMetersDetailAdapter  extends RecyclerView.Adapter< HistoryMetersDetailAdapter.RecycleViewViewHolder> {

    private ArrayList<SummaryHistoryItem> historyItem;

    public HistoryMetersDetailAdapter(ArrayList<SummaryHistoryItem> historyItem) {
        this.historyItem = historyItem;

    }

    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {
        public TextView VidUslugi;
        public TextView saldoBegin;
        public TextView oplataPeriod;
        public  TextView nachisPeriod;
        public TextView dept;



        public RecycleViewViewHolder(@NonNull View itemView) {//коструктор,  View itemView, этот параметор это отльный элемент RecelceView
            super(itemView);
            VidUslugi = itemView.findViewById(R.id.VidUslugi);
            saldoBegin = itemView.findViewById(R.id.saldoBeginValue);
            nachisPeriod = itemView.findViewById(R.id.nachisPeriodValue);
            oplataPeriod = itemView.findViewById(R.id.oplataPeriodValue);
            dept = itemView.findViewById(R.id.deptValue);


        }
    }


    @NonNull
    @Override
    public HistoryMetersDetailAdapter.RecycleViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {// нужно передать разметку в наш адаптер
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_details_item, viewGroup, false);
        HistoryMetersDetailAdapter.RecycleViewViewHolder recycleViewViewHolder = new HistoryMetersDetailAdapter.RecycleViewViewHolder(view);
        return recycleViewViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull HistoryMetersDetailAdapter.RecycleViewViewHolder recycleViewViewHolder, final int i) {
      SummaryHistoryItem summaryHistoryItem = historyItem.get(i);// при помощт i свящвваем каждый элемт из ArrayList с элметом RecycleVIew
        recycleViewViewHolder.VidUslugi.setText(String.valueOf(summaryHistoryItem.getByVidUslugi()));
        recycleViewViewHolder.saldoBegin.setText(String.valueOf(summaryHistoryItem.saldoBegin()));
        recycleViewViewHolder.nachisPeriod.setText(String.valueOf(summaryHistoryItem.nachisleno()));
        recycleViewViewHolder.oplataPeriod.setText(String.valueOf(summaryHistoryItem.oplata()));
        recycleViewViewHolder.dept.setText(String.valueOf(summaryHistoryItem.debt()));



    }



//    public ArrayList<String> getUserData() {
//        return userData;
//    }

    @Override
    public int getItemCount() {
        return historyItem.size();
    }

}
