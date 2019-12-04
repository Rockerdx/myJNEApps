package online.jne.com.jneapps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import online.jne.com.jneapps.adapter.OrderAdapter;
import online.jne.com.jneapps.connection.ApiClient;
import online.jne.com.jneapps.helper.SessionManager;
import online.jne.com.jneapps.model.History;
import online.jne.com.jneapps.model.HistoryResponse;
import online.jne.com.jneapps.model.Order;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HIstoryActivity extends AppCompatActivity {

    List<History> orders = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");

        getOrder();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getOrder(){
        SessionManager sessionManager = new SessionManager(HIstoryActivity.this);
        String iduser = sessionManager.getId();

        ApiClient.getTiketApiClient().getHistory(iduser).enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                orders = new ArrayList<>();
                orders = response.body().getListHistory();
                if(orders!=null && orders.size()>0) {
                    OrderAdapter adapter = new OrderAdapter(getApplicationContext(), orders);
                    RecyclerView rv = findViewById(R.id.rv_history);
                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv.setAdapter(adapter);
                }
                else {
                    Toast.makeText(HIstoryActivity.this,"History Kosong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {

            }
        });


    }


}
