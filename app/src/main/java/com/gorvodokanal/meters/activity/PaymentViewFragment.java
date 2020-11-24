package com.gorvodokanal.meters.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gorvodokanal.R;


public class PaymentViewFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = getView().findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);



        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                if(url.startsWith("https://www.gorvodokanal.com/personal/payment/bad.php")) {

                    Uri uri = Uri.parse(url);
                    String errorMessage = uri.getQueryParameter("error");
                    if(errorMessage == null || errorMessage.isEmpty()){
                        errorMessage = ("Отмена операции");
                    }

                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();

                    final NavController navController = NavHostFragment.findNavController(PaymentViewFragment.this);
                    Bundle bundle = new Bundle();
                    bundle.putString("errorMessage", errorMessage);
                    navController.popBackStack(R.id.paymentFragment,false);

                    return true;
                }else if (url.startsWith("https://www.gorvodokanal.com/personal/payment/ok.php")){
                    Toast.makeText(getActivity(), "Оплата прошла успешно", Toast.LENGTH_LONG).show();

                    final NavController navController = NavHostFragment.findNavController(PaymentViewFragment.this);
                    navController.popBackStack(R.id.paymentFragment,false);

                    return true;
                }
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });
        webView.loadUrl(getArguments().getString("paymentUrl"));

    }
    private void  showErrorDialog(){
        NoConnection dialog = new NoConnection();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");

    }
}