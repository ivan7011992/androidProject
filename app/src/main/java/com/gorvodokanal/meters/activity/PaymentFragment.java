package com.gorvodokanal.meters.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.historyUtilClass.PaymentAdapter;
import com.gorvodokanal.meters.model.SummaryPaymentData;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanal.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PaymentFragment extends Fragment {
    EditText value1;
    EditText value2;
    EditText value3;
    ProgressDialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Загрузка...");
        mDialog.setCancelable(false);

        mDialog.show();
        return inflater.inflate(R.layout.fragment_payment, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null && getArguments().containsKey("errorMessage")) {
            Toast.makeText(getActivity(), getArguments().getString("errorMessage"), Toast.LENGTH_LONG).show();
        }
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());


        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.PAYMENT_METERS, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                mDialog.dismiss();
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray rows = response.getJSONArray("data");
                    SummaryPaymentData data = new SummaryPaymentData(rows);


                    final PaymentAdapter adapter = new PaymentAdapter(data);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    RecyclerView paymentMetersView = (RecyclerView) getView().findViewById(R.id.paymentMeters);
                    paymentMetersView.setAdapter(adapter);
                    paymentMetersView.setLayoutManager(layoutManager);
                    Button paymentValue = getView().findViewById(R.id.paymentValue);
                    paymentValue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<Integer, String> userInputData = adapter.getUserInputData();

                            Map<String, Object> requestData = new HashMap<>();
                            for(Map.Entry<Integer, String> userInputItem : userInputData.entrySet()) {
                                requestData.put(String.valueOf(userInputItem.getKey()), userInputItem.getValue());
                            }

                            final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());

                            PostRequest request = new PostRequest(mQueue);
                            request.makeRequest(UrlCollection.PAYMENT_GENERATE_URL, requestData, new VolleyJsonSuccessCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    mDialog.dismiss();
                                    try {
                                        if (!response.has("success")) {
                                            Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                                            Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        final boolean isSuccess = response.getBoolean("success");

                                        if (!isSuccess) {
                                            Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        Bundle bundle = new Bundle();
                                        String url = response.getString("url");
                                        bundle.putString("paymentUrl", url);
                                        final NavController navController = NavHostFragment.findNavController(PaymentFragment.this);
                                        navController.navigate(R.id.paymentViewFragment, bundle);



                                    } catch (Exception e) {
                                        Log.e("valley", "error", e);
                                    }
                                }
                            });





                        }
                    });
                } catch (Exception e) {
                    Log.e("valley", "Error", e);
                }


            }
        },new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mDialog.dismiss();
            }
        });


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

            Intent openSetting = new Intent(PaymentFragment.this.getActivity(), Setting.class);
            startActivity(openSetting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}