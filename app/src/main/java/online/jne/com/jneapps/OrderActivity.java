package online.jne.com.jneapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import online.jne.com.jneapps.connection.ApiClient;
import online.jne.com.jneapps.model.Cabang;
import online.jne.com.jneapps.model.CabangResponse;
import online.jne.com.jneapps.model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    Spinner kantor;
    Spinner berat;

    ArrayList<Cabang> cabangs;
    ArrayList<String> berats = new ArrayList<>();
    EditText ket;
    double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kirim Barang");

        Intent x = getIntent();
        lat = x.getDoubleExtra("lat",0.0);
        lng = x.getDoubleExtra("long",0.0);

        kantor = findViewById(R.id.spin_kantor);
        berat = findViewById(R.id.spin_berat);
        ket = findViewById(R.id.keterangan);
        FancyButton order = findViewById(R.id.btn_order);

        getListKantor();

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cabang cabang = cabangs.get(kantor.getSelectedItemPosition());
                String beratBrg = berats.get(berat.getSelectedItemPosition());
                String kete = ket.getText().toString().trim();

                Intent x = new Intent(getApplicationContext(), MapActivity.class);
                x.putExtra("cabang",cabang);
                x.putExtra("berat",beratBrg);
                x.putExtra("ket",kete);
                x.putExtra("lat",lat);
                x.putExtra("long",lng);
                startActivity(x);
            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private ArrayList<Cabang> getListKantor(){
        ArrayList<Cabang> cabangList = new ArrayList<>();
        ApiClient.getTiketApiClient().getCabang().enqueue(new Callback<CabangResponse>() {
            @Override
            public void onResponse(Call<CabangResponse> call, Response<CabangResponse> response) {
                cabangs = new ArrayList<>();
                cabangs = response.body().getListCabang();
                berats.add("Kecil(1-3KG)");
                berats.add("Sedang(4-7KG)");
                berats.add("Berat(8-10KG)");

                ArrayAdapter<Cabang> adapter =
                        new ArrayAdapter<Cabang>(getApplicationContext(), R.layout.spinner_item, cabangs);
                adapter.setDropDownViewResource( R.layout.spinner_item);
                ArrayAdapter<String> adapter2 =
                        new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_item, berats);
                adapter2.setDropDownViewResource(R.layout.spinner_item);

                kantor.setAdapter(adapter);
                berat.setAdapter(adapter2);

            }

            @Override
            public void onFailure(Call<CabangResponse> call, Throwable t) {

            }
        });
        return cabangList;
    }

}
