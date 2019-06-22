package com.deekson.mpesa_darajasdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
@BindView(R.id.number) EditText mNumber;
@BindView(R.id.amount) EditText mAmount;
@BindView(R.id.button) Button mButton;
@BindView(R.id.progressBar) ProgressBar mLoad;
    private Daraja daraja;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mButton.setOnClickListener(this);
        daraja = Daraja.with("nrNgs4hdS0kkTOqqp6h5et24nlfqfJO8", "Syal7zSIUHO67ffN", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                Log.i(MainActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
            }

            @Override
            public void onError(String error) {
                Log.e(MainActivity.this.getClass().getSimpleName(), error);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == mButton){
            String phonenumber = mNumber.getText().toString().trim(); //collect the number from the user's input
            String Amount = mAmount.getText().toString().trim(); // amount privided by the user

            //check validity of a number
            if(phonenumber.isEmpty() || phonenumber.length() != 10){
                mNumber.setError("Invalid number");
                return;
            }
            //check validity of a number
            else if(Integer.valueOf(Amount) <= 0){
                mAmount.setError("Amount should be more than 0");
                return;
            }
            mLoad.setVisibility(View.VISIBLE);
            LNMExpress lnmExpress = new LNMExpress(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                    TransactionType.CustomerPayBillOnline,
                    Amount,
                    "0700000000",
                    "174379",
                    phonenumber,
                    "http://mycallbackurl.com/checkout.php",
                    "001ABC",
                    "Payment for goods and services of company A"
            );
            daraja.requestMPESAExpress(lnmExpress,
                    new DarajaListener<LNMResult>() {
                        @Override
                        public void onResult(@NonNull LNMResult lnmResult) {
                            Log.i(MainActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i(MainActivity.this.getClass().getSimpleName(), error);
                        }
                    }
            );
        }
    }
}
