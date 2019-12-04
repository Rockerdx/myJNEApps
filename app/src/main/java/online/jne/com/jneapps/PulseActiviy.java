package online.jne.com.jneapps;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.jne.com.jneapps.model.History;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class PulseActiviy extends AppCompatActivity {

    History history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_activiy);
        PulsatorLayout pulsator = findViewById(R.id.pulsator);
        pulsator.start();

        Intent x = getIntent();
        history = x.getParcelableExtra("order");

        new Handler().postDelayed(() -> {
            Intent i = new Intent(PulseActiviy.this,SuccessActivity.class);
            i.putExtra("order", history);
            i.putExtra("selesai", true);
            startActivity(i);

        }, 3000);


    }
}
