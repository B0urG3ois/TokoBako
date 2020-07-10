package umn.ac.id.uastokobako;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText loginNoHp, loginPass;
    Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);

        Log.d("DEBUGFB", "User saat ini : " + FirebaseAuth.getInstance().getCurrentUser());
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginNoHp = findViewById(R.id.loginNoHp);
                if (TextUtils.isEmpty(loginNoHp.getText().toString())) {
                    Toast.makeText(LoginActivity.this,"Nomor HP tidak boleh kosong !",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("noHP",loginNoHp.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
