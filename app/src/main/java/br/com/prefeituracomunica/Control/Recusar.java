package br.com.prefeituracomunica.Control;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Recusar extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String id_agendamento = intent.getStringExtra("id_agendamento");
        String id_notificacao = ""+intent.getStringExtra("id_notificacao");
        int id = Integer.parseInt(id_notificacao);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel(id);

        Intent background = new Intent(context, BackgroundService.class);
        background.putExtra("id_agendamento", id_agendamento);
        background.putExtra("opcao", "recusar");
        context.startService(background);
    }
}

