package com.gorvodokanalVer1.meters.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class BindingLsDialogGroup extends DialogFragment {
    Button bindingButtonOk;
    ImageView imageSupportBindingGroup;

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binding_ls_dialog_group, container, false);


        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }


        bindingButtonOk = view.findViewById(R.id.bindingButtonOk);
        bindingButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataBindingUser();
            }
        });

        imageSupportBindingGroup = view.findViewById(R.id.imageSupportBindingGroup);
        imageSupportBindingGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog("Привязка лицевого счёта.", "Для привязки аккаунта должны быть выполнены следующие условия:\n" +
                        "\n" +
                        "1)Должен быть открыт лицевой счёт\n" +
                        "\n" +
                        "2)Email текущего и привязываемого аккаунта должны совпадать\n" +
                        "\n" +
                        "3)Почта привязываемого аккаунта должна быть подтверждена\n" +
                        "\n" );
            }
        });




        return view;
    }


    public void passDataBindingUser() {
        View view = getView();
        String email = null;
        String lsnumber = "";
        String flat = "";
        String numberLs = ((EditText) view.findViewById(R.id.numberLs)).getText().toString();


        Map<String, String> requestData = new HashMap<>();
        requestData.put("numberLs", numberLs);
        requestData.put("bindingFlat", flat);



        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());


        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.BILDING_LS, new HashMap<String, Object>(requestData), new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        //  Toast.makeText(Registration.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        JSONObject rows = response.getJSONObject("errors");

                        StringBuilder errorBuilder = new StringBuilder();
                        Iterator<String> temp = rows.keys();
                        while (temp.hasNext()) {
                            String key = temp.next();
                            String error = rows.getString(key);
                            errorBuilder.append(error).append("\n");
                        }
                        displayError("Обраружены ошибки: " + errorBuilder.toString());

                        return;
                    }
                    Toast.makeText(getContext(), "Аккаунт привязан", Toast.LENGTH_LONG).show();

                    getDialog().dismiss();



                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }


            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

                //  showErrorDialog();
            }
        });
    }
    private void displayError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
    private void createAlertDialog(String title, String content) {
        // объект Builder для создания диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // добавляем различные компоненты в диалоговое окно
        builder.setTitle(title);
        builder.setMessage(content);
        // устанавливаем кнопку, которая отвечает за позитивный ответ
        builder.setPositiveButton("OK",
                // устанавливаем слушатель
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // по нажатию создаем всплывающее окно с типом нажатой конпки

                    }
                });
        // объект Builder создал диалоговое окно и оно готово появиться на экране
        // вызываем этот метод, чтобы показать AlertDialog на экране пользователя
        builder.show();
    }
}
