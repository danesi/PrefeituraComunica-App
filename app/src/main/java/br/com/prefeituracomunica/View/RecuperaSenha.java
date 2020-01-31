package br.com.prefeituracomunica.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import br.com.prefeituracomunica.Control.APICaller;
import br.com.prefeituracomunica.R;

public class RecuperaSenha extends AppCompatActivity {

    EditText email;
    Button recuperar, voltar;
    ProgressBar minhaBarrinha;
    AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_senha);

        email = (EditText) findViewById(R.id.email);
        recuperar = (Button) findViewById(R.id.btRecuperar);
        voltar = (Button) findViewById(R.id.btVoltar);
        minhaBarrinha = findViewById(R.id.carregando);

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().equals("")){
                    minhaBarrinha.setVisibility(View.VISIBLE);
                    APICaller apc = new APICaller(RecuperaSenha.this){
                        @Override
                        public void success(String response) {
                            super.success(response);
                            if(response.equals("true")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RecuperaSenha.this);
                                builder.setTitle("Recuperação de senha");
                                builder.setMessage("Por favor, verifique sua caixa de e-mail, para trocar sua senha. Caso não achar o e-mail, verifique sua caixa de spam :)");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        minhaBarrinha.setVisibility(View.GONE);
                                        startActivity(new Intent(RecuperaSenha.this, Login.class));
                                        finish();
                                    }
                                });
                                alerta = builder.create();
                                alerta.show();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RecuperaSenha.this);
                                builder.setTitle("Erro no E-mail");
                                builder.setMessage("Este E-mail não foi encontrado no nosso sistema!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        minhaBarrinha.setVisibility(View.GONE);
                                    }
                                });
                                alerta = builder.create();
                                alerta.show();
                            }
                        }

                        @Override
                        public void responseError(VolleyError response) {
                            super.responseError(response);
                            startActivity(new Intent(RecuperaSenha.this , NetError.class));
                            finish();
                        }
                    };
                    apc.setApiUrl("Controle/usuarioControle.php?function=recuperaSenhaAPI");
                    apc.addParam("usuario" , email.getText().toString());
                    apc.executeRemote();
                } else {
                    email.setError("O email não pode ser vazio!");
                }
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecuperaSenha.this, Login.class));
                finish();
            }
        });
    }
}
