package br.com.prefeituracomunica.Model;

import java.lang.reflect.Field;
import java.util.Map;

public class Notificacao {
    private String title;
    private String body;
    private String img_url;
    private String url_destino;
    private String id_agendamento;

    public Notificacao (Map<String , String> data){
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(this, data.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUrl_destino() {
        return url_destino;
    }

    public void setUrl_destino(String url_destino) {
        this.url_destino = url_destino;
    }

    public String getId_agendamento() {
        return id_agendamento;
    }

    public void setId_agendamento(String id_agendamento) {
        this.id_agendamento = id_agendamento;
    }
}
