package com.middle.east.shipo.ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.middle.east.shipo.R;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EnterPhone extends AppCompatActivity {
    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60*60*1000;

    private static final String TAG = "FirebasePhoneNumAuth";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth firebaseAuth;

    private String phoneNumber;
    private Button sendCodeButton;
    private Button verifyCodeButton;
    private Button signOutButton;

    private EditText phoneNum;
    private EditText verifyCodeET;

    //private FirebaseFirestore firestoreDB;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verifications);
        sendCodeButton = findViewById(R.id.send_code_b);
        verifyCodeButton = findViewById(R.id.verify_code_b);
        //signOutButton = findViewById(R.id.auth_logout_b);

        phoneNum = findViewById(R.id.phone);
        verifyCodeET = findViewById(R.id.phone_auth_code);

        addOnClickListeners();

        firebaseAuth = FirebaseAuth.getInstance();
        //firestoreDB = FirebaseFirestore.getInstance();

        createCallback();
        getInstallationIdentifier();
        //getVerificationDataFromFirestoreAndVerify(null);
    }

    private void addOnClickListeners() {
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pphone = phoneNum.getText().toString();
                if(pphone.isEmpty()){
                    phoneNum.setError("أدخل رقم الهاتف");
                }else {
                    Intent goToRegis = new Intent(EnterPhone.this,RegisterActivity.class);
                    goToRegis.putExtra("Phone",pphone);
                    startActivity(goToRegis);
                }

            }
        });
        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNumberCode();
            }
        });

    }

    private void createCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "verification completed" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "verification failed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phoneNum.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(EnterPhone.this,
                            "Trying too many timeS",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "code sent " + verificationId);
                //addVerificationDataToFirestore(phoneNumber, verificationId);
            }
        };
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNum.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    private void verifyPhoneNumberInit() {
        phoneNumber = "+2"+phoneNum.getText().toString();
        if (!validatePhoneNumber(phoneNumber)) {
            return;
        }
        verifyPhoneNumber(phoneNumber);

    }



    private void verifyPhoneNumber(String phno){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phno, 70,
                TimeUnit.SECONDS, this, callbacks);
    }
    private void verifyPhoneNumberCode() {
        final String phone_code = verifyCodeET.getText().toString();
        //getVerificationDataFromFirestoreAndVerify(phone_code);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "code verified signIn successful");
                            firebaseUser = task.getResult().getUser();
                            showSingInButtons();
                        } else {
                            Log.w(TAG, "code verification failed", task.getException());
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                verifyCodeET.setError("Invalid code.");
                            }
                        }
                    }
                });
    }
    private void createCredentialSignIn(String verificationId, String verifyCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.
                getCredential(verificationId, verifyCode);
        signInWithPhoneAuthCredential(credential);
    }
    private void signOut() {
        firebaseAuth.signOut();
        showSendCodeButton();
    }

    public synchronized String getInstallationIdentifier() {
        if (uniqueIdentifier == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueIdentifier = sharedPrefs.getString(UNIQUE_ID, null);
            if (uniqueIdentifier == null) {
                uniqueIdentifier = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(UNIQUE_ID, uniqueIdentifier);
                editor.commit();
            }
        }
        return uniqueIdentifier;
    }
    private void disableSendCodeButton(long codeSentTimestamp){
        long timeElapsed = System.currentTimeMillis()- codeSentTimestamp;
        if(timeElapsed > ONE_HOUR_MILLI){
            showSendCodeButton();
        }else{
            findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
            findViewById(R.id.phone_auth_code_items).setVisibility(View.VISIBLE);
            //findViewById(R.id.logout_items).setVisibility(View.GONE);
        }
    }
    private void showSendCodeButton(){
        findViewById(R.id.phone_auth_items).setVisibility(View.VISIBLE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
        //findViewById(R.id.logout_items).setVisibility(View.GONE);
    }
    private void showSingInButtons(){
        findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
        //findViewById(R.id.logout_items).setVisibility(View.VISIBLE);
    }
    private void initButtons(){
        findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
        //findViewById(R.id.logout_items).setVisibility(View.GONE);
    }
}
