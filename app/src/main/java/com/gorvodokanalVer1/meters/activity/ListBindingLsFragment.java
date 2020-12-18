package com.gorvodokanalVer1.meters.activity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.historyUtilClass.BindingLsAdapter;
import com.gorvodokanalVer1.meters.historyUtilClass.SummaryHistoryItemAdapter;
import com.gorvodokanalVer1.meters.historyUtilClass.SupportAdapter;
import com.gorvodokanalVer1.meters.model.BindingItem;
import com.gorvodokanalVer1.meters.model.SummaryHistoryItem;
import com.gorvodokanalVer1.meters.model.SupportItem;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ListBindingLsFragment extends Fragment {
    ProgressDialog mDialog;
    ArrayList<BindingItem> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list_binding_ls, container, false);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBindingLs();



    }


public void getBindingLs(){

    final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
    GetRequest request = new GetRequest(mQueue);
    String requestUrl = UrlCollection.GET_BINDING_LS + "?getUser=get" ;
    request.makeRequest(requestUrl, new VolleyJsonSuccessCallback() {
        @Override
        public void onSuccess(JSONObject response) {

            try {
                if (!response.has("success")) {

                    Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                    Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                    return;
                }
                final boolean isSuccess = response.getBoolean("success");

                if (!isSuccess) {
                    Toast.makeText(getContext(), "Не удалось получить список", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONArray rows = response.getJSONArray("ls");
                data = buildData(rows);

                BindingDataView(data);

            } catch (Exception e) {
                Log.e("valley", "error", e);
            }
        }
    }, new VolleyJsonErrorCallback() {
        @Override
        public void onError(VolleyError error) {
            mDialog.dismiss();
            showErrorDialog();
        }
    });


}

    private ArrayList<BindingItem> buildData(JSONArray rows) throws JSONException {
        ArrayList<BindingItem> data = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) {
            BindingItem item = new BindingItem((JSONObject) rows.get(i));
            data.add(item);
        }
        return data;
    }
    private void  showErrorDialog(){
        NoConnection dialog = new NoConnection();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");

    }

    private void BindingDataView(ArrayList<BindingItem> data){

        RecyclerView listBindingDataView = (RecyclerView) getView().findViewById(R.id.listBindingLs);
        listBindingDataView.setAdapter(null);
        final BindingLsAdapter adapter = new BindingLsAdapter(data, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        listBindingDataView.setAdapter(adapter);
        listBindingDataView.setNestedScrollingEnabled(false);
        listBindingDataView.setLayoutManager(layoutManager);

    }

   public void getBindingLsUpdate(){
       getBindingLs();
   }
}