package online.jne.com.jneapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import online.jne.com.jneapps.connection.ApiClient;
import online.jne.com.jneapps.helper.SessionManager;
import online.jne.com.jneapps.model.User;
import online.jne.com.jneapps.model.UserResponse;
import online.jne.com.jneapps.service.MyFirebaseMessagingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static online.jne.com.jneapps.helper.Utils.LOG_TAG;


public class LoginActivity extends AppCompatActivity {


    SessionManager session;
    Button btnLogin;
    Button btnLinkToRegister;
    MaterialEditText inputPhone;
    MaterialEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);
        inputPhone = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    inputPhone.setEnabled(false);
                    inputPassword.setEnabled(false);
                    btnLogin.setEnabled(false);
                    loginProcessWithRetrofit(email,password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnLinkToRegister.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    RegisterActivity.class);
            startActivity(i);
        });

    }
    private void loginProcessWithRetrofit(final String phone, String password){

        String token = MyFirebaseMessagingService.getToken(getApplicationContext());
        ApiClient.getTiketApiClient().authenticate(phone,password,token).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if(!userResponse.isError()) {
                    User user = userResponse.getUser();
                    //Log.d(LOG_TAG, new Gson().toJson(response.body()));
                    Log.d(LOG_TAG, "iduser =" +userResponse.getUser().getIdCustomer());

                    session.setLogin(true,user.getName(),user.getEmail(),session.getImage(),user.getIdCustomer());
                    Intent x = new Intent(LoginActivity.this, MainActivity.class);
                    x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(x);
                    finish();
                }
                else {
                    inputPhone.setEnabled(true);
                    btnLogin.setEnabled(true);
                    inputPassword.setText("");
                    inputPassword.setEnabled(true);
                    Toast.makeText(LoginActivity.this,"Failed to login" ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                inputPhone.setEnabled(true);
                inputPassword.setText("");
                btnLogin.setEnabled(true);
                inputPassword.setEnabled(true);
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this,"Failed to login",Toast.LENGTH_SHORT).show();
            }
        });

    }



}
