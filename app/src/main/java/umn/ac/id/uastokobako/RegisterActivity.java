package umn.ac.id.uastokobako;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextPhone;
    private EditText editTextPass;
    private TextView textViewPhone;
    private EditText editTextNama;
    private EditText editTextConfPass;
    private EditText editTextAlamat;
    private Button btnRegis;
    private Button btnOTP;
    String phoneNumber;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String verifID;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("noHP");
        phoneNumber = "+62" + phone;
        btnRegis = (Button) findViewById(R.id.btnRegis);
        btnOTP = (Button) findViewById(R.id.btnVerif);

        textViewPhone = findViewById(R.id.regisPhone);
        textViewPhone.setText(phoneNumber);
        firebaseAuth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(RegisterActivity.this, "Verifikasi selesai", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegisterActivity.this, "Verifikasi gagal", Toast.LENGTH_SHORT).show();
                Log.d("COK","error : " + e);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Toast.makeText(RegisterActivity.this, "Code telah dikirim, tunggu 1 menit lagi untuk mengirim ulang",Toast.LENGTH_SHORT).show();
                verifID = s;
                token = forceResendingToken;
            }
        };
        btnOTP.setOnClickListener(this);
        btnRegis.setOnClickListener(this);
    }

    private void resendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                1,                 // Timeout duration
                TimeUnit.MINUTES,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallback,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void sendVerif(){
        textViewPhone = findViewById(R.id.regisPhone);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                1,
                TimeUnit.MINUTES,
                this,
                mCallback);
    }

    private void checkVerif(){
        editTextPass = findViewById(R.id.verifCode);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifID,editTextPass.getText().toString());

        progressDialog = ProgressDialog.show(RegisterActivity.this, "Verifying", "Processing...");

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    progressDialog = ProgressDialog.show(RegisterActivity.this, "Progress Dialog", "Login berhasil..");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(RegisterActivity.this, "Verifikasi gagal, silahkan coba lagi!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnVerif:
                sendVerif();
                break;

            case R.id.btnRegis:
                checkVerif();
                break;

            case R.id.btnResend:
                resendVerificationCode();
                break;
        }
    }
}
