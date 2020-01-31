package br.com.prefeituracomunica.Control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

import br.com.prefeituracomunica.Model.Usuario;
import br.com.prefeituracomunica.View.Login;
import br.com.prefeituracomunica.View.MainActivity;
import br.com.prefeituracomunica.View.VizualizadorWeb;

public class UsuarioPDO {
    private Context activity;

    public UsuarioPDO(Context activity) {
        this.activity = activity;
    }



    public Usuario getStoredUser() {
        AlternativeDB adb = new AlternativeDB(activity);
        Usuario us = new Usuario();
        us.setUsuario(adb.getTelefone());
        us.setSenha(adb.getSenha());
        us.setToken(adb.getToken());
        us.setFacebook_id(adb.getFacebookId());
        return us;
    }

    public void loginFace(final Usuario us){
        AbstractBD pdo = new AbstractBD(activity){
            @Override
            public void success(JSONArray jsonArray) {
                super.success(jsonArray);
                if(jsonArray.length()>0){
                    try {
                        Usuario usuario = new Usuario(jsonArray.getJSONObject(0));
                        AlternativeDB ater = new AlternativeDB(activity);
                        ater.setIdUsuario(usuario.getId_usuario());
                        ater.setFacebookId(us.getFacebook_id());
                        TokenControl tcn = new TokenControl(activity);
                        tcn.updateToken();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(activity , VizualizadorWeb.class);
                    i.putExtra("url" , "/Controle/usuarioControle.php?function=loginAppFace&facebook_id="+us.getFacebook_id());
                    activity.startActivity(i);
                }else{
                    AbstractBD pdo = new AbstractBD(activity){
                        @Override
                        public void success(JSONArray jsonArray) {
                            super.success(jsonArray);
                            if(jsonArray.length()>0){
                                loginFace(us);
                            }else{
                                Log.d("avanco", "Falha geral insert face");
                            }
                        }
                    };
                    pdo.prepare("insert into usuario values (default , :nome , '' , '' , :email , '' , '' , :foto , :token , default , 1 , 1 , 0 , 1, 0 , :face_id , 1 , 0)");
                    pdo.bindValue(":nome" , us.getNome());
                    pdo.bindValue(":email" , us.getUsuario());
                    pdo.bindValue(":foto" , us.getFoto());
                    pdo.bindValue(":token" , us.getToken());
                    pdo.bindValue(":face_id" , us.getFacebook_id());
                    pdo.executeRemote();
                }
            }
        };
        pdo.prepare("select * from usuario where facebook_id = :facebook_id");
        pdo.bindValue(":facebook_id" , us.getFacebook_id());
        pdo.executeRemote();
    }

    public void login() {
        final Usuario us = this.getStoredUser();
        if(us.getFacebook_id()!=null){
            TokenControl tk = new TokenControl(activity);
            tk.updateToken();
            Intent i = new Intent(activity , VizualizadorWeb.class);
            i.putExtra("url" , "/Controle/usuarioControle.php?function=loginAppFace&facebook_id="+us.getFacebook_id());
            activity.startActivity(i);
            ((Activity)activity).finish();
        }else {
            AbstractBD pdo = new AbstractBD(activity) {
                @Override
                public void success(JSONArray jsonArray) {
                    super.success(jsonArray);
                    if (jsonArray.length() > 0) {
                        TokenControl tk = new TokenControl(activity);
                        tk.updateToken();
                        Intent i = new Intent(activity, VizualizadorWeb.class);
                        i.putExtra("url", "/Controle/usuarioControle.php?function=login&us=" + us.getUsuario() +
                                "&ps=" + us.getSenha());
                        TokenControl tokenControl = new TokenControl(activity);
                        tokenControl.updateToken();
                        activity.startActivity(i);
                        ((Activity)activity).finish();

                    } else {
                        AlternativeDB alt = new AlternativeDB(activity);
                        alt.deleteId_usuario();
                        alt.deleteSenha();
                        alt.deleteTelefone();
                        Toast.makeText(activity, "Usuário ou senha Errados!", Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity, Login.class));
                        ((Activity)activity).finish();
                    }
                }

                @Override
                public void responseError(VolleyError response) {
                    super.responseError(response);
                }
            };
            pdo.prepare("select * from usuario where (telefone = :telefone or email = :email) and senha = :senha;");
            pdo.bindValue(":telefone", us.getUsuario());
            pdo.bindValue(":email", us.getUsuario());
            pdo.bindValue(":senha", MainActivity.md5(us.getSenha()));
            pdo.executeRemote();
        }
    }

    public void loginRemoto(final Usuario us, final Login login) {
        AbstractBD pdo = new AbstractBD(activity) {
            @Override
            public void success(JSONArray jsonArray) {
                super.success(jsonArray);
                if (jsonArray.length() > 0) {
                    Usuario usuario = null;
                    try {
                        usuario = new Usuario(jsonArray.getJSONObject(0));
                        AlternativeDB adb = new AlternativeDB(activity);
                        adb.setIdUsuario(usuario.getId_usuario());
                        adb.setSenha(us.getSenha());
                        adb.setTelefone(us.getUsuario());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(activity, VizualizadorWeb.class);
                    i.putExtra("url", "/Controle/usuarioControle.php?function=login&us=" + us.getUsuario() +
                            "&ps=" + us.getSenha());
                    TokenControl tokenControl = new TokenControl(activity);
                    tokenControl.updateToken();
                    activity.startActivity(i);
                    ((Activity)activity).finish();
                } else {
                    login.stopBarrinha();
                    Toast.makeText(activity, "Usuário ou senha Errados!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        pdo.prepare("select * from usuario where (telefone = :telefone or email = :email) and senha = :senha;");
        pdo.bindValue(":telefone", us.getUsuario());
        pdo.bindValue(":email", us.getUsuario());
        pdo.bindValue(":senha", us.getMd5Ps());
        pdo.executeRemote();
    }

}
