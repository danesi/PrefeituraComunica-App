package br.com.prefeituracomunica.Control;

import android.content.Context;
import android.util.Log;

public class ConfirmacaoControl {
    private Context context;

    public ConfirmacaoControl(Context context) {
        this.context = context;
    }

    public void confirma(String id_agendamento){
        Log.d("avanco", "To indo confimar: " + id_agendamento);
        AbstractBD pdo = new AbstractBD(context);
        pdo.prepare("update agendamento set status = 'confirmado' where id_agendamento = :id_agendamento");
        pdo.bindValue(":id_agendamento", Integer.parseInt(id_agendamento));
        pdo.executeRemote();
        Log.d("avanco", "PDO = " + pdo.getSql());
    }

    public void recusa(String id_agendamento){
        AbstractBD pdo = new AbstractBD(context);
        pdo.prepare("update agendamento set status = 'recusado' where id_agendamento = :id_agendamento");
        pdo.bindValue(":id_agendamento", Integer.parseInt(id_agendamento));
        pdo.executeRemote();
    }
}
