package br.com.prefeituracomunica.Model;

public class Parametros {
    private String nomeDB = "prefcomu";
    private String userDB = "prefcomu";
    private String urlMestre = "https://prefeituracomunica.markeyvip.com/";
    private String senhaDB = "gabCd&fnx^t5bKhH";
    private String uriRestDB = "/Controle/remoteDB.php";
    private String dbAdress = "localhost";

    public String getDbAdress() {
        return dbAdress;
    }

    public void setDbAdress(String dbAdress) {
        this.dbAdress = dbAdress;
    }

    public String getNomeDB() {
        return nomeDB;
    }

    public void setNomeDB(String nomeDB) {
        this.nomeDB = nomeDB;
    }

    public String getUserDB() {
        return userDB;
    }

    public void setUserDB(String userDB) {
        this.userDB = userDB;
    }

    public String getUrlMestre() {
        return urlMestre;
    }

    public void setUrlMestre(String urlMestre) {
        this.urlMestre = urlMestre;
    }

    public String getSenhaDB() {
        return senhaDB;
    }

    public void setSenhaDB(String senhaDB) {
        this.senhaDB = senhaDB;
    }

    public String getUriRestDB() {
        return uriRestDB;
    }

    public void setUriRestDB(String uriRestDB) {
        this.uriRestDB = uriRestDB;
    }
}
