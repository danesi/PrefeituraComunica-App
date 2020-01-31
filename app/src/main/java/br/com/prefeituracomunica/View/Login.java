package br.com.prefeituracomunica.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import br.com.prefeituracomunica.Control.AlternativeDB;
import br.com.prefeituracomunica.Control.TokenControl;
import br.com.prefeituracomunica.Control.UsuarioPDO;
import br.com.prefeituracomunica.Model.Usuario;
import br.com.prefeituracomunica.R;

public class Login extends AppCompatActivity {

    ProgressBar minhaBarrinha;
    Button entrar, cadastrar, face, btRecuperar;
    EditText edTelefone, edSenha;
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginManager.getInstance().logOut();
        minhaBarrinha = findViewById(R.id.carregando);
        entrar = (Button) findViewById(R.id.btEntar);
        cadastrar = (Button) findViewById(R.id.btCadastrar);
        btRecuperar = (Button) findViewById(R.id.btRecuperar);
        edTelefone = (EditText) findViewById(R.id.telefone);
        edSenha = (EditText) findViewById(R.id.senha);
        loginButton = findViewById(R.id.login_button);

        btRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, RecuperaSenha.class));
                finish();
            }
        });

        TokenControl tkc = new TokenControl(this);
        tkc.getActualToken();
        final AlternativeDB adb = new AlternativeDB(this);
        cadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this , VizualizadorWeb.class);
                i.putExtra("url" , "/Tela/registroUsuario.php?token="+adb.getToken());
                startActivity(i);
                finish();
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edTelefone.getText().toString().equals("")) {
                    minhaBarrinha.setVisibility(View.VISIBLE);
                    Usuario usuario = new Usuario();
                    usuario.setUsuario(edTelefone.getText().toString());
                    usuario.setSenha(edSenha.getText().toString());
                    UsuarioPDO usuarioPDO = new UsuarioPDO(Login.this);
                    usuarioPDO.loginRemoto(usuario, Login.this);
                } else {
                    edTelefone.setError("O telefone não pode ser vazio!");
                }
            }
        });
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        face = findViewById(R.id.face);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minhaBarrinha.setVisibility(View.VISIBLE);
                loginButton.callOnClick();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null) {
            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };
    private void loadUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    String nome = first_name + " " + last_name;
                    Usuario us= new Usuario();
                    us.setUsuario(email);
                    us.setFacebook_id(id);
                    us.setFoto(image_url);
                    us.setNome(nome);
                    UsuarioPDO updo = new UsuarioPDO(Login.this);
                    updo.loginFace(us);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle paremetros = new Bundle();
        paremetros.putString("fields", "first_name, last_name, email, id, picture");
        request.setParameters(paremetros);
        request.executeAsync();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        minhaBarrinha.setVisibility(View.GONE);
    }

    public void stopBarrinha() {
        minhaBarrinha.setVisibility(View.GONE);
    }
}
