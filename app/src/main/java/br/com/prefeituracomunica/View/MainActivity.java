package br.com.prefeituracomunica.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.prefeituracomunica.Control.AlternativeDB;
import br.com.prefeituracomunica.Control.UsuarioPDO;
import br.com.prefeituracomunica.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("avanco", "Olá");
        AlternativeDB adb = new AlternativeDB(this);
        if(adb.getIdUsuario()>0){
           UsuarioPDO updo = new UsuarioPDO(this);
           updo.login();
        }else{
            startActivity(new Intent(MainActivity.this , Login.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
