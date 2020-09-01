package com.gorvodokanal.meters.activity;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.historyUtilClass.PaymentAdapter;
import com.gorvodokanal.meters.historyUtilClass.SummaryPassItemAdapter;
import com.gorvodokanal.meters.model.SummaryPaymentData;
import com.gorvodokanal.meters.model.VodomerItem;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PaymentFragment extends Fragment {
    EditText value1;
    EditText value2;
    EditText value3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());


        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.PAYMENT_METERS, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
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

                            final NavController navController = NavHostFragment.findNavController(PaymentFragment.this);

                            Bundle bundle = new Bundle();
                            bundle.putString("paymentUrl", "https://www.gorvodokanal.com/mobile_app/test_redirect.php");
                            navController.navigate(R.id.paymentViewFragment, bundle);
                        }
                    });
                } catch (Exception e) {
                    Log.e("valley", "Error", e);
                }


            }
        });


    }
}