package online.jne.com.jneapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import online.jne.com.jneapps.connection.ApiClient;
import online.jne.com.jneapps.model.User;
import online.jne.com.jneapps.model.UserResponse;
import online.jne.com.jneapps.service.MyFirebaseMessagingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static online.jne.com.jneapps.helper.Utils.LOG_TAG;


public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLinkToLogin;
    private MaterialEditText inputFullName;
    private MaterialEditText inputPhone;
    private MaterialEditText inputPassword;
    private MaterialEditText inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = findViewById(R.id.name);
        inputPhone = findViewById(R.id.hp);
        inputPassword = findViewById(R.id.password);
        inputEmail = findViewById(R.id.email);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);


        btnRegister.setOnClickListener(view -> {
            String name = inputFullName.getText().toString().trim();
            String hp = inputPhone.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                inputFullName.setEnabled(false);
                inputPhone.setEnabled(false);
                inputPassword.setEnabled(false);
                inputEmail.setEnabled(false);
                btnRegister.setEnabled(false);

                registerProcessWithRetrofit(email,password,name,hp);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }
        });


        btnLinkToLogin.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void registerProcessWithRetrofit(final String email, String password, String name, String hp){
        String token = MyFirebaseMessagingService.getToken(getApplicationContext());
        ApiClient.getTiketApiClient().registration(email,password,hp,name,token).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if(userResponse!=null) {
                    Log.d(LOG_TAG, String.valueOf(userResponse.isError()));
                    if (!userResponse.isError()) {
                        User user = userResponse.getUser();
                        Log.d(LOG_TAG, new Gson().toJson(response.body()));
                        Log.d(LOG_TAG, user.getName());
                        Toast.makeText(RegisterActivity.this, "Sukses,Silahkan login", Toast.LENGTH_SHORT).show();


                        Intent x = new Intent(RegisterActivity.this, LoginActivity.class);
                        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(x);
                    } else {
                        Log.d("tes", new Gson().toJson(response.body()));
                        inputFullName.setEnabled(true);
                        inputPhone.setEnabled(true);
                        inputPassword.setEnabled(true);
                        btnRegister.setEnabled(true);
                        inputEmail.setEnabled(true);
                        Log.d(LOG_TAG, String.valueOf(userResponse.isError()));
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

                inputFullName.setEnabled(true);
                inputPhone.setEnabled(true);
                inputPassword.setEnabled(true);
                btnRegister.setEnabled(true);
                inputEmail.setEnabled(true);

                Toast.makeText(RegisterActivity.this,"Failed to register",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
