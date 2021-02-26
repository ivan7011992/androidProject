package com.gorvodokanalVer1.meters.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.historyUtilClass.GeneralAdapter;
import com.gorvodokanalVer1.meters.model.SummaryGeneralItem;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanalVer1.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeneralInfoFragment extends Fragment {
    private static String address;
    LinearLayout fieldParrent;
    LinearLayout fieldParrent2;
    LinearLayout fieldParrent3;
    RelativeLayout fieldBlockTarif;
    RelativeLayout fieldParrentDataUser;
    public static int confirm = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_general_info, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getActivity());
        fieldParrent = view.findViewById(R.id.fieldParrent);
        fieldParrent2 = view.findViewById(R.id.PriborBlock);
        fieldParrent3 = view.findViewById(R.id.fieldBlockIPU0);
        fieldBlockTarif = view.findViewById(R.id.fieldBlockTarif);
        fieldParrentDataUser = view.findViewById(R.id.fieldParrentDataUser);
        ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Загрузка...");
        mDialog.setCancelable(false);
        mDialog.show();

        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.GENERAL_INFO_URL, new VolleyJsonSuccessCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        mDialog.dismiss();
                        try {
                            if (!response.has("success")) {
                                Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                                Toast.makeText(getActivity(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                                return;
                            }


                            JSONArray rows = response.getJSONArray("data");


                            JSONObject firstRow = (JSONObject) rows.get(0);
                            String IPU = firstRow.getString("IPU");
                            String kod = firstRow.getString("KOD");
                            UrlCollection.KOD = kod;


                            if (firstRow.has("FIO")) {
                                ((TextView) getView().findViewById(R.id.fio)).setText(firstRow.getString("FIO"));

                            }else{
                                TextView fio = getView().findViewById(R.id.fio);
                                fieldParrentDataUser.removeView(fio);
                                TextView fio_abonent  =getView().findViewById(R.id.fio_abonent);
                                fieldParrentDataUser.removeView(fio_abonent);
                            }
                            address = firstRow.getString("NAIMUL") + " " + firstRow.getString("DOM") + " " + "КВ" + " "
                                    + firstRow.getString("KVARTIRA");

                            ((TextView) getView().findViewById(R.id.address)).setText(address);
                            if (IPU.equals("1")) {
                                SummaryGeneralItem data = new SummaryGeneralItem(rows);
                                if (firstRow.has("ZENWODA")) {
                                    ((TextView) getView().findViewById(R.id.cool)).setText("Холодное водоснабжение");
                                    ((TextView) getView().findViewById(R.id.cool_data)).setText(firstRow.getString("ZENWODA"));

                                } else {
                                    TextView cool = getView().findViewById(R.id.cool);
                                    fieldBlockTarif.removeView(cool);
                                    TextView cool_data = getView().findViewById(R.id.cool_data);
                                    fieldBlockTarif.removeView(cool_data);
                                }
                                if (firstRow.has("ZENSTOK")) {
                                    ((TextView) getView().findViewById(R.id.voootv)).setText("Водоотведение");
                                    ((TextView) getView().findViewById(R.id.vootv_data)).setText(firstRow.getString("ZENSTOK"));
                                } else {
                                    TextView voootv = getView().findViewById(R.id.voootv);
                                    fieldBlockTarif.removeView(voootv);
                                    TextView vootv_data = getView().findViewById(R.id.vootv_data);
                                    fieldBlockTarif.removeView(vootv_data);
                                }
                                LinearLayout fieldBlock2 = getView().findViewById(R.id.fieldBlockIPU0);
                                fieldParrent.removeView(fieldBlock2);


                            } else {
                                ((TextView) getView().findViewById(R.id.fieldIPU0)).setText("Степень благоустройства:");
                                ((TextView) getView().findViewById(R.id.fieldValueIPU0)).setText(firstRow.getString("ST_BLAG"));


                                if (firstRow.has("WODA_KUB_MES")) {
                                    ((TextView) getView().findViewById(R.id.field1NormPotrebl)).setText("Нормативное потребление воды \n на одного чел/месяц");
                                    ((TextView) getView().findViewById(R.id.field1ValueNormPotrebl)).setText(firstRow.getString("WODA_KUB_MES"));
                                }else{
                                    LinearLayout fieldBlock3 = getView().findViewById(R.id.fieldBlockNormPotrebl);
                                    fieldParrent3.removeView(fieldBlock3);

                                }
                                if (firstRow.has("ZENWODA")) {
                                    ((TextView) getView().findViewById(R.id.field3)).setText("Стоимость 1м3 воды:");
                                    ((TextView) getView().findViewById(R.id.field3Value)).setText(firstRow.getString("ZENWODA"));
                                }else{
                                    LinearLayout fieldBlock3 = getView().findViewById(R.id.fieldBlock3);
                                    fieldParrent3.removeView(fieldBlock3);
                                }
                                if (firstRow.has("STOK_KUB_MES")) {
                                    ((TextView) getView().findViewById(R.id.field1NormVodootv)).setText("Норматив по водоотведению \n на 1 чел. в месяц:");
                                    ((TextView) getView().findViewById(R.id.field1ValueNormVodootv)).setText(firstRow.getString("STOK_KUB_MES"));
                                }else{
                                    LinearLayout fieldBlock1NormVodootv = getView().findViewById(R.id.fieldBlock1NormVodootv);
                                    fieldParrent3.removeView(fieldBlock1NormVodootv);

                                }

                                if (firstRow.has("ZENSTOK")) {
                                    ((TextView) getView().findViewById(R.id.field4Value)).setText(firstRow.getString("ZENSTOK"));
                                } else {
                                    LinearLayout fieldBlock4 = getView().findViewById(R.id.fieldBlock4);
                                    fieldParrent3.removeView(fieldBlock4);

                                }

                                LinearLayout fieldBlock1 = getView().findViewById(R.id.fieldBlockIPU1);
                                fieldParrent.removeView(fieldBlock1);
                                TextView fieldBlock2 = getView().findViewById(R.id.Probor);
                                fieldParrent2.removeView(fieldBlock2);
                                ((TextView) getView().findViewById(R.id.field5Value)).setText(firstRow.getString("KOLJIL"));

                            }


                            if (IPU.equals("1")) {
                                SummaryGeneralItem data = new SummaryGeneralItem(rows);
                                final GeneralAdapter adapter = new GeneralAdapter(data);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                RecyclerView generalView = (RecyclerView) getView().findViewById(R.id.generalItems);
                                generalView.setAdapter(adapter);
                                generalView.setLayoutManager(layoutManager);
                            } else {
                                SummaryGeneralItem data = new SummaryGeneralItem(rows);
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
                },
                new VolleyJsonErrorCallback() {
                    @Override
                    public void onError(VolleyError error) {
                        mDialog.dismiss();
                        showErrorDialog();
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
        inflater.inflate(R.menu.settings_menu, menu);

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

    private void showErrorDialog() {
        NoConnection dialog = new NoConnection();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");

    }
}