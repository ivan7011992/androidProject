package com.gorvodokanalVer1.meters.historyUtilClass;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.gorvodokanalVer1.R;
        import com.gorvodokanalVer1.meters.model.HistoryItem;
        import com.gorvodokanalVer1.meters.model.SummaryHistoryItem;

public class HistoryMetersDetailAdapter  extends RecyclerView.Adapter< HistoryMetersDetailAdapter.RecycleViewViewHolder> {

    private SummaryHistoryItem summaryHistoryItem;

    public HistoryMetersDetailAdapter(SummaryHistoryItem historyItem) {
        this.summaryHistoryItem = historyItem;

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
     HistoryItem historyItem = this.summaryHistoryItem.getByIndex(i);// при помощт i свящвваем каждый элемт из ArrayList с элметом RecycleVIew
        recycleViewViewHolder.VidUslugi.setText(String.valueOf(historyItem.getNameUslugi()));
        recycleViewViewHolder.saldoBegin.setText(String.valueOf(historyItem.getSaldoBegin()));
        recycleViewViewHolder.nachisPeriod.setText(String.valueOf(historyItem.getNachisleno()));
        recycleViewViewHolder.oplataPeriod.setText(String.valueOf(historyItem.getOplata()));
        recycleViewViewHolder.dept.setText(String.valueOf(historyItem.getDept()));



    }



//    public ArrayList<String> getUserData() {
//        return userData;
//    }

    @Override
    public int getItemCount() {
        return summaryHistoryItem.size();
    }

}
