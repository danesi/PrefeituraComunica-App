package br.com.prefeituracomunica.Model;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario {
    private int id_usuario;
    private String usuario;
    private String senha;
    private String token;
    private String foto;
    private String facebook_id;
    private String nome;

    public Usuario() {
    }

    public Usuario(JSONObject json) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            try {
                switch (fields[j].getGenericType().toString()) {
                    case "int":
                        fields[j].set(this, json.getInt(fields[j].getName()));
                        break;
                    case "class java.lang.String":
                        fields[j].set(this, json.getString(fields[j].getName()));
                        break;
                    case "class java.lang.Double":
                        fields[j].set(this, json.getDouble(fields[j].getName()));
                        break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Usuario(Cursor c) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            try {
                switch (fields[j].getGenericType().toString()) {
                    case "int":
                        fields[j].set(this, c.getInt(c.getColumnIndex(fields[j].getName())));
                        break;
                    case "class java.lang.String":
                        fields[j].set(this, c.getString(c.getColumnIndex(fields[j].getName())));
                        break;
                    case "class java.lang.Double":
                        fields[j].set(this, c.getDouble(c.getColumnIndex(fields[j].getName())));
                        break;
                }
//                fields[j].set(this, c.(fields[j].getName()));
            } catch (Exception e) {
                Log.d("avanco", e.getMessage());
                Log.d("avanco", "Deu merda no construtor la");
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMd5Ps() {
        String s = this.getSenha();
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


    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }
}
