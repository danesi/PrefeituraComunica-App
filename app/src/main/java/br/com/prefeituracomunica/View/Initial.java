package br.com.prefeituracomunica.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.prefeituracomunica.R;

public class Initial extends AppCompatActivity {

    Button adm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        adm = (Button) findViewById(R.id.adm);

        adm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Initial.this, Login.class));
            }
        });
    }
}
