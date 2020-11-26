package com.gorvodokanalVer1.meters.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.historyUtilClass.SupportAdapter;
import com.gorvodokanalVer1.meters.model.SupportItem;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SupportFragment extends Fragment {
  EditText  textSupport;
    ArrayList<SupportItem> data;
    SupportItem supportItem;
Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_support, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = getView().findViewById(R.id.getMessegeSupport);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passDataSupport();
            }
        });


        viewSupoort();




    }

    public void passDataSupport() {
        String textSupportData = ((EditText) getView().findViewById(R.id.textSupport)).getText().toString();

        if (textSupportData.isEmpty()) {
            Toast.makeText(getContext(), "Введите текст", Toast.LENGTH_LONG).show();
            return;
        }
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());

        Map<String, Object> requestData = new HashMap<>();

        requestData.put("question", textSupportData);

        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.SET_DATA_QUESTION, requestData, new VolleyJsonSuccessCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            if (!response.has("success")) {
                                Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                                Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                                return;
                            }
                            final boolean isSuccess = response.getBoolean("success");

                            Toast.makeText(getContext(), "Вопрос оттправлен,на привязанную почту будет выслано письмо по вашей проблеме", Toast.LENGTH_LONG).show();


                        } catch (Exception e) {
                            Log.e("valley", "error", e);
                        }
                    }
                },
                new VolleyJsonErrorCallback() {
                    @Override
                    public void onError(VolleyError error) {

                    }

                });
    }


    public void viewSupoort() {

        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getActivity());
        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.GET_INFO_SUPPORT, new VolleyJsonSuccessCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        try {
                            if (!response.has("success")) {
                                Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                                Toast.makeText(getActivity(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                                return;
                            }

                            final boolean isSuccess = response.getBoolean("success");
                            if(!isSuccess){
                                Toast.makeText(getActivity(), "Данные не получены", Toast.LENGTH_LONG).show();
                            }
                            JSONArray rows = response.getJSONArray("data");
                            data = buildData(rows);

                            SupportDataView(data);


                        } catch (Exception e) {
                            Log.e("valley", "Error", e);
                        }
                    }
                },
                new VolleyJsonErrorCallback() {
                    @Override
                    public void onError(VolleyError error) {

                    }

                });
    }
    private ArrayList<SupportItem> buildData(JSONArray rows) throws JSONException {
        ArrayList<SupportItem> data = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) {
            SupportItem item = new SupportItem((JSONObject) rows.get(i));
            data.add(item);
        }
        return data;

    }

    private void SupportDataView(ArrayList<SupportItem> data) {

        RecyclerView supportDataView = (RecyclerView) getView().findViewById(R.id.supportData);
        supportDataView.setAdapter(null);
        final SupportAdapter adapter = new SupportAdapter(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        supportDataView.setAdapter(adapter);
        supportDataView.setNestedScrollingEnabled(false);
        supportDataView.setLayoutManager(layoutManager);




    }



}