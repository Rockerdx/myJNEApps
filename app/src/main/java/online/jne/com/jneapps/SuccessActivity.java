package online.jne.com.jneapps;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import online.jne.com.jneapps.adapter.ResiAdapter;
import online.jne.com.jneapps.connection.ApiClient;
import online.jne.com.jneapps.helper.Utils;
import online.jne.com.jneapps.model.DetailResponse;
import online.jne.com.jneapps.model.Driver;
import online.jne.com.jneapps.model.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessActivity extends AppCompatActivity {

    LinearLayout selesaiBtn;
    TextView orderNo,cabang,tanggal,berat;
    TextView kendaraan,namaDriver,hpDriver,BKDriver;
    ImageView fotoDriver;
    ConstraintLayout layoutDriver;
    ConstraintLayout layoutKosong;
    RecyclerView list_resi;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Orderan");
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));



        orderNo = findViewById(R.id.orderan);
        cabang = findViewById(R.id.cabang);
        tanggal = findViewById(R.id.tanggal);
        berat = findViewById(R.id.berat);
        kendaraan = findViewById(R.id.kendaraan);
        namaDriver = findViewById(R.id.namaDriver);
        hpDriver = findViewById(R.id.hpDriver);
        BKDriver = findViewById(R.id.BKDriver);
        fotoDriver = findViewById(R.id.fotoDriver);
        selesaiBtn = findViewById(R.id.btn_confirm);
        layoutDriver = findViewById(R.id.driverLayout);
        layoutKosong = findViewById(R.id.layoutKosong);
        list_resi = findViewById(R.id.list_resi);
        status = findViewById(R.id.status);


        Intent x = getIntent();
        if(x.getBooleanExtra("selesai",false)){
            selesaiBtn.setVisibility(View.VISIBLE);
        }
        else {
            selesaiBtn.setVisibility(View.GONE);
        }

        History history = new History();
        history = x.getParcelableExtra("order");
        orderNo.setText("Order no : " +history.getKodeOrder());
        cabang.setText(history.getCabang());
        tanggal.setText(Utils.convertDate(history.getUploadTime()));
        berat.setText(history.getKisaranBerat());

        switch (history.getStatus()){
            case "0":
                status.setText("Sistem akan memproses pesanan kamu,mohon tunggu notifikasi berikutnya");
                break;
            case "1":
                status.setText("Driver sedang menuju ke tempat kamu,siapkan paket kamu");
                break;
            case "2":
                status.setText("Orderan telah selesai di proses");
                break;
            case "3":
                status.setText("Orderan di batalkan karna tidak ada driver atau masalah tertentu");
                break;
        }

        selesaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getApplicationContext(), MainActivity.class);
                x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(x);
            }
        });

        if(history.getStatus().equals("1") || history.getStatus().equals("2")){
            ApiClient.getTiketApiClient().getDetail(history.getKodeOrder()).enqueue(new Callback<DetailResponse>() {
                @Override
                public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {

                    layoutDriver.setVisibility(View.VISIBLE);
                    layoutKosong.setVisibility(View.GONE);
                    if(response.body().getResi()!=null && response.body().getResi().size()>0) {
                        ResiAdapter adapter = new ResiAdapter(getApplicationContext(), response.body().getResi());
                        list_resi.setAdapter(adapter);
                        list_resi.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        list_resi.setVisibility(View.VISIBLE);
                    }
                    else {
                        list_resi.setVisibility(View.GONE);
                    }

                    Driver driver = response.body().getDriver();
                    Picasso.get().load(driver.getFilename()).into(fotoDriver);
                    Log.d("tes",driver.getFilename());
                    namaDriver.setText(driver.getNamaDriver());
                    kendaraan.setText(driver.getMerekMotor());
                    BKDriver.setText(driver.getPlatNomor());
                }

                @Override
                public void onFailure(Call<DetailResponse> call, Throwable t) {

                }
            });
        }
        else {
            list_resi.setVisibility(View.GONE);

            layoutDriver.setVisibility(View.GONE);
            layoutKosong.setVisibility(View.VISIBLE);
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
