package br.com.prefeituracomunica.Control;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.prefeituracomunica.Model.Parametros;

public class AlternativeDB {
    private static final String TELEFONE = "telefone";
    private static final String SENHA = "senha";
    private static final String TOKEN = "token";
    private static final String ID_USUARIO = "id_usuario";
    private static final String FACEBOOK = "facebook_id";
    private Context context;
    private Parametros parametros;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AlternativeDB(Context context){
        this.context = context;
        this.parametros = new Parametros();
        this.sharedPreferences = context.getSharedPreferences(parametros.getNomeDB(), Context.MODE_PRIVATE);
        this.editor  = sharedPreferences.edit();
    }

    public void setTelefone(String telefone) {
        editor.putString(TELEFONE, telefone);
        editor.commit();
    }
    public void deleteId_usuario(){
        editor.remove(ID_USUARIO);
        editor.commit();
    }
    public void deleteTelefone(){
        editor.remove(TELEFONE);
        editor.commit();
    }
    public void deleteSenha(){
        editor.remove(SENHA);
        editor.commit();
    }
    public void deleteToken(){
        editor.remove(TOKEN);
        editor.commit();
    }

    public void setFacebookId(String facebookId){
        editor.putString(FACEBOOK, facebookId);
        editor.commit();
    }
    public void deleteFacebookId(){
        editor.remove(FACEBOOK);
        editor.commit();
    }
    public String getFacebookId(){
        return this.sharedPreferences.getString(FACEBOOK, null);
    }

    public String getTelefone() {

        return this.sharedPreferences.getString(TELEFONE, null);
    }

    public void setSenha(String senha) {
        editor.putString(SENHA, senha);
        editor.commit();
    }

    public String getSenha() {
        return this.sharedPreferences.getString(SENHA, null);
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return this.sharedPreferences.getString(TOKEN, "");
    }

    public int getIdUsuario() {
        return this.sharedPreferences.getInt(ID_USUARIO, 0);
    }

    public void setIdUsuario(int id_usuario){
        editor.putInt(ID_USUARIO, id_usuario);
        editor.commit();
    }
}
