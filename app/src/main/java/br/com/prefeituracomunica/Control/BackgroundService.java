package br.com.prefeituracomunica.Control;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundService extends Service {
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private String id_agendamento;
    private String opcao;
    private ConfirmacaoControl confirmacaoControl;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            if(opcao.equals("confirmar")) {
                confirmacaoControl = new ConfirmacaoControl(context);
                confirmacaoControl.confirma(id_agendamento);
            } else {
                confirmacaoControl = new ConfirmacaoControl(context);
                confirmacaoControl.recusa(id_agendamento);
            }
            stopSelf();
            onDestroy();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id_agendamento = intent.getStringExtra("id_agendamento");
        opcao = intent.getStringExtra("opcao");
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }
}
