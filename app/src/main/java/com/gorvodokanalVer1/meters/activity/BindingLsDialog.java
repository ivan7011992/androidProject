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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.UserModel;
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

public class BindingLsDialog extends DialogFragment {
    Button bindingButton;
    ImageView imageView;

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.binding_ls_dialog, container, false);


        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
         EditText numberLsView = view.findViewById(R.id.numberLs);
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("__-_______");
        FormatWatcher formatWatcher = new MaskFormatWatcher( // форматировать текст будет вот он
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(numberLsView);
        EditText flat = view.findViewById(R.id.bindingFlat);
        flat.setInputType(InputType.TYPE_CLASS_NUMBER);


        bindingButton = view.findViewById(R.id.bindingButton);
        bindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   passDataBindingUser();
            }
        });

        imageView = view.findViewById(R.id.imageSupportBinding);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog("Привязка лицевого счёта.", "Для привязки аккаунта должны быть выполнены следующие условия:\n" +
                        "\n" +
                        "1)Должен быть открыт лицевой счёт\n" +
                        "\n" +
                        "2)Электронные адреса текущего и привязываемого аккаунта должны совпадать\n" +
                        "\n" +
                        "3)Почта должна быть подтверждена\n" +
                        "\n" +
                        "Если привязываемый аккаунт ещё не зарегистрирован, можете заполнить поля формы и подтвердить привязку.");
            }
        });




        return view;
    }


    public void passDataBindingUser() {
        View view = getView();
        String numberLs = ((EditText) view.findViewById(R.id.numberLs)).getText().toString();
        String bindingStreet = ((EditText) view.findViewById(R.id.bindingStreet)).getText().toString();
        String bindingHouse = ((EditText) view.findViewById(R.id.bindingHouse)).getText().toString();
        String bindingFlat = ((EditText) view.findViewById(R.id.bindingFlat)).getText().toString();
        String binfingFio = ((EditText) view.findViewById(R.id.binfingFio)).getText().toString();
        Map<String, String> requestData = new HashMap<>();
        requestData.put("numberLs", numberLs);
        requestData.put("bindingStreet", bindingStreet);
        requestData.put("bindingHouse", bindingHouse);
        requestData.put("bindingFlat", bindingFlat);
        requestData.put("binfingFio", binfingFio);


        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());

        HashMap<String, String> emptyErrorMessages = new HashMap<>();
        emptyErrorMessages.put("numberLs", "Введите код");
        emptyErrorMessages.put("bindingStreet", "Введите улицу");
        emptyErrorMessages.put("bindingHouse", "Введите дом");
        emptyErrorMessages.put("bindingFlat", "Введите квартиру");
        emptyErrorMessages.put("binfingFio", "Введите Введите фамилию");


        ArrayList<String> errors = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = requestData.entrySet();
        for (Map.Entry<String, String> errorEntry : entrySet) {


            if (errorEntry.getValue().isEmpty()) {
                String errorMessage = emptyErrorMessages.get(errorEntry.getKey());
                errors.add(errorMessage);
            }


        }

        if (!errors.isEmpty()) {

            StringBuilder allErrors = new StringBuilder();
            for (String error : errors) {
                allErrors.append(error).append("\n");

            }
            Toast.makeText(getContext(), allErrors.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        String userLogin = UserModel.getInstance().getLogin();
        if(requestData.get("numberLs").equals(userLogin)){
            Toast.makeText(getContext(), "Привязываемый ЛС не должен совпадать с текущим", Toast.LENGTH_LONG).show();
            return;
        }
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
                        String userIdS =  response.getString("userId");
                        Integer userId =  Integer.parseInt(userIdS);


                    getDialog().dismiss();
                    UserModel.getInstance().addLs(userId,numberLs);


                    Intent openSetting = new Intent(getActivity(), AppActivity.class);
                    openSetting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(openSetting);
                    getActivity().finish();

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
