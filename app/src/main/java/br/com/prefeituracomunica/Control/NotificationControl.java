package br.com.prefeituracomunica.Control;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import br.com.prefeituracomunica.Model.Notificacao;
import br.com.prefeituracomunica.R;
import br.com.prefeituracomunica.View.MainActivity;

public class NotificationControl extends FirebaseMessagingService {

    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null){
            enviarNotificacao(remoteMessage);
        }

        if (remoteMessage.getNotification() != null) {
            enviarNotificacao(remoteMessage);
        }
    }

    private void enviarNotificacao(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        //TODO Enviar data para construtor
        Notificacao notificacao = new Notificacao(data);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "markey";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Gerais",
                    NotificationManager.IMPORTANCE_MAX
            );

            channel.setDescription("Canal geral de notificações");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }

        AlternativeDB alternativeDB = new AlternativeDB(this);
        String telefone = alternativeDB.getTelefone();
        if(telefone != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("url", notificacao.getUrl_destino());
            pendingIntent = PendingIntent.getActivity(this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        int SUMMARY_ID = 0;
        String GROUP_KEY_WORK_EMAIL = "markey.markeyvip.group";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alert);
        int id_notification = 0;
        if (!notificacao.getImg_url().equals("")) {
            if(notificacao.getId_agendamento().equals("")) {
                id_notification = (int) (Math.random() * 10000);
                Notification notification =
                        new NotificationCompat.Builder(NotificationControl.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_alert)
                                .setContentTitle(notificacao.getTitle())
                                .setContentText(notificacao.getBody())
                                .setGroup(GROUP_KEY_WORK_EMAIL)
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getImage(notificacao.getImg_url())))
                                .setColor(Color.rgb(0, 0, 205))
                                .setContentIntent(pendingIntent)
                                .setLargeIcon(bitmap)
                                .setAutoCancel(true)
                                .build();

                manager.notify( id_notification, notification);
            }
            else {
                Log.d("avanco", "enviarNotificacion: else segundo if");
                id_notification = (int) (Math.random() * 10000);

                Intent intentConfirma = new Intent(NotificationControl.this, Confirmar.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra("id_notificacao", id_notification+"")
                        .putExtra("id_agendamento", notificacao.getId_agendamento());

                PendingIntent confirma = PendingIntent.getBroadcast(this,
                        id_notification,
                        intentConfirma,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                Intent intentRecusa = new Intent(NotificationControl.this, Recusar.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra("id_agendamento", notificacao.getId_agendamento())
                        .putExtra("id_notificacao", id_notification+"");
                PendingIntent recusa = PendingIntent.getBroadcast(this,
                        id_notification,
                        intentRecusa,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                Notification notification =
                        new NotificationCompat.Builder(NotificationControl.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_alert)
                                .setContentTitle(notificacao.getTitle())
                                .setContentText(notificacao.getBody())
                                .setGroup(GROUP_KEY_WORK_EMAIL)
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getImage(notificacao.getImg_url())))
                                .setColor(Color.rgb(0, 0, 205))
                                .setContentIntent(PendingIntent.getActivity(this,
                                        id_notification,
                                        new Intent(this, MainActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                .putExtra("url", notificacao.getUrl_destino()),
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                ))
                                .setLargeIcon(bitmap)
                                .setAutoCancel(true)
                                .addAction(R.drawable.ic_confirmar, "Confirmar", confirma)
                                .addAction(R.drawable.ic_recusa, "Recusar", recusa)
                                .build();

                manager.notify(id_notification, notification);
            }
        } else {
            if(!notificacao.getId_agendamento().equals("")) {
                Log.d("avanco", "enviarNotificacion: tem id sem imagem");
                id_notification = (int) (Math.random() * 10000);
                Intent intentConfirma = new Intent(NotificationControl.this, Confirmar.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra("id_notificacao", id_notification+"")
                        .putExtra("id_agendamento", notificacao.getId_agendamento());

                PendingIntent confirma = PendingIntent.getBroadcast(this,
                        id_notification,
                        intentConfirma,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                Intent intentRecusa = new Intent(NotificationControl.this, Recusar.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra("id_agendamento", notificacao.getId_agendamento())
                        .putExtra("id_notificacao", id_notification+"");

                PendingIntent recusa = PendingIntent.getBroadcast(this,
                        id_notification,
                        intentRecusa,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                Notification notification =
                        new NotificationCompat.Builder(NotificationControl.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_alert)
                                .setContentTitle(notificacao.getTitle())
                                .setContentText(notificacao.getBody())
                                .setGroup(GROUP_KEY_WORK_EMAIL)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(notificacao.getBody()))
                                .setColor(Color.rgb(0, 0, 205))
                                .setContentIntent(PendingIntent.getActivity(this,
                                        id_notification,
                                        new Intent(this, MainActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                .putExtra("url", notificacao.getUrl_destino()),
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                ))
                                .addAction(R.drawable.ic_confirmar, "Confirmar", confirma)
                                .addAction(R.drawable.ic_recusa, "Recusar", recusa)
                                .setLargeIcon(bitmap)
                                .setAutoCancel(true)
                                .build();
                manager.notify(id_notification, notification);
            } else {
                Log.d("avanco", "enviarNotificacion: " + notificacao.getUrl_destino());
                id_notification = (int) (Math.random() * 10000);
                Notification notification =
                        new NotificationCompat.Builder(NotificationControl.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_alert)
                                .setContentTitle(notificacao.getTitle())
                                .setContentText(notificacao.getBody())
                                .setGroup(GROUP_KEY_WORK_EMAIL)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(notificacao.getBody()))
                                .setColor(Color.rgb(0, 0, 205))
                                .setContentIntent(
                                        PendingIntent.getActivity(this,
                                                id_notification,
                                                new Intent(NotificationControl.this, MainActivity.class)
                                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .putExtra("url", notificacao.getUrl_destino()),
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        ))
                                .setLargeIcon(bitmap)
                                .setAutoCancel(true)
                                .build();
                manager.notify(id_notification, notification);
            }
        }

        if (Build.VERSION.SDK_INT > 23) {
            Log.d("avanco", "enviarNotificacion: já n sei mais");

            if(notificacao.getId_agendamento().equals("")) {
                Log.d("avanco", "enviarNotificacion: veio aqui");

                Notification summaryNotification =
                        new NotificationCompat.Builder(NotificationControl.this, CHANNEL_ID)
                                .setAutoCancel(true)
                                .setContentTitle(notificacao.getTitle())
                                .setContentText(notificacao.getBody())
                                .setSmallIcon(R.drawable.ic_alert)
                                .setStyle(new NotificationCompat.InboxStyle())
                                .setGroup(GROUP_KEY_WORK_EMAIL)
                                .setColor(Color.rgb(0, 0, 205))
                                .setGroupSummary(true)
                                .build();
                manager.notify(SUMMARY_ID, summaryNotification);
            }
        }

    }

    @Override
    public void onNewToken(String token) {
        FirebaseMessaging.getInstance().subscribeToTopic("dispositivos");
        AlternativeDB alternativeDB = new AlternativeDB(this);
        alternativeDB.setToken(token);
        Log.d("avanco", "onNewToken: "+ token);

        if(alternativeDB.getIdUsuario() != 0) {
            AbstractBD pdo = new AbstractBD(this){
                @Override
                public void responseError(VolleyError response) {
                    super.responseError(response);
                    Log.d("avanco", "Erro ao atulizar o token");
                }
            };
            pdo.prepare("update usuario set token = :token where id_usuario = :id_usuario;");
            pdo.bindValue(":token", token);
            pdo.bindValue(":id_usuario", alternativeDB.getIdUsuario());
            pdo.executeRemote();
        }
    }

    public static Bitmap getImage(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
