package com.gorvodokanal.meters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.historyUtilClass.GeneralAdapter;
import com.gorvodokanal.meters.historyUtilClass.PaymentAdapter;
import com.gorvodokanal.meters.model.SummaryGeneralItem;
import com.gorvodokanal.meters.model.SummaryPaymentData;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;
import com.gorvodokanal.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeneralInfoFragment extends Fragment {
    private static String address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_general_info, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getActivity());

        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.GENERAL_INFO_URL, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(getActivity(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray rows = response.getJSONArray("data");
                    SummaryGeneralItem data = new SummaryGeneralItem(rows);

                    JSONObject firstRow = (JSONObject) rows.get(0);
                    String IPU =  firstRow.getString("IPU");
                    ((TextView) getView().findViewById(R.id.kod)).setText(firstRow.getString("KOD"));
                    ((TextView) getView().findViewById(R.id.fio)).setText(firstRow.getString("FIO"));
                 address = firstRow.getString("NAIMUL") + " " +  firstRow.getString("DOM") + " " + "КВ" + " "
                 + firstRow.getString("KVARTIRA");

                    ((TextView)  getView().findViewById(R.id.address)).setText(address);
                    if(IPU.equals("1")) {
                        ((TextView) getView().findViewById(R.id.field)).setText("Действуюшие тарифы");
                        ((TextView) getView().findViewById(R.id.field1)).setText("Холодное водоснабжение");
                        ((TextView) getView().findViewById(R.id.field1Value)).setText(firstRow.getString("ZENWODA"));
                        ((TextView) getView().findViewById(R.id.field2)).setText("Водоотведение");
                        ((TextView) getView().findViewById(R.id.field2Value)).setText(firstRow.getString("ZENSTOK"));

                        ((TextView) getView().findViewById(R.id.field2Value)).setText(firstRow.getString("ZENSTOK"));
                    }else {
                        ((TextView) getView().findViewById(R.id.field)).setText("Степень благоустройства:");
                        ((TextView) getView().findViewById(R.id.fieldValue)).setText("Жилые помещения (в т. ч. общежития квартирного типа) с холодным и горячим водоснабжением, канализованием, оборудованные ваннами длиной 1500-1700 мм, душами, раковинами, кухонными мойками и унитазами");
                        ((TextView) getView().findViewById(R.id.field1)).setText("Нормативное потребление воды на 1 чел. в месяц:");
                        ((TextView) getView().findViewById(R.id.field1Value)).setText(firstRow.getString("WODA_KUB_MES"));
                        ((TextView) getView().findViewById(R.id.field3)).setText("Стоимость 1м воды:");
                        ((TextView) getView().findViewById(R.id.field3Value)).setText(firstRow.getString("ZENWODA"));
                        ((TextView) getView().findViewById(R.id.field2)).setText("Норматив по водоотведению на 1 чел. в месяц");
                        ((TextView) getView().findViewById(R.id.field2Value)).setText(firstRow.getString("STOK_KUB_MES"));
                        ((TextView) getView().findViewById(R.id.field4)).setText("Стоимость 1м стоков:");
                        ((TextView) getView().findViewById(R.id.field4Value)).setText(firstRow.getString("ZENSTOK"));
                    }



                    if(IPU.equals("1")){
                        final GeneralAdapter adapter = new GeneralAdapter(data);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        RecyclerView generalView = (RecyclerView) getView().findViewById(R.id.generalItems);
                        generalView.setAdapter(adapter);
                        generalView.setLayoutManager(layoutManager);
                    }else{

                        final GeneralAdapter adapter = new GeneralAdapter(data);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        RecyclerView generalView = (RecyclerView) getView().findViewById(R.id.generalItems);
                        generalView.setAdapter(adapter);
                        generalView.setLayoutManager(layoutManager);
                    }


                } catch (Exception e) {
                    Log.e("valley", "Error", e);
                }
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        //MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.drawer_menu, menu);
        inflater.inflate(R.menu.settings_menu, menu) ;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//этот метод вызввает когда происходит нажатие на каком-то из эдлементов меню
        int id = item.getItemId();// получаем элемнт которые был выбран в меню
        if (id == R.id.action_setting) {

            Intent openSetting = new Intent(GeneralInfoFragment.this.getActivity(), Setting.class);
            startActivity(openSetting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}