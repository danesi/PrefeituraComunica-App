package br.com.prefeituracomunica.Control;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class TokenControl {
    private Context activity;

    public TokenControl(Context activity) {
        this.activity = activity;
    }

    public void updateToken(){
        AlternativeDB adb = new AlternativeDB(activity);
        if(adb.getIdUsuario()>0) {
            AbstractBD pdo = new AbstractBD(activity);
            pdo.prepare("update usuario set token = :token where id_usuario = :id_usuario");
            pdo.bindValue(":token" , adb.getToken());
            pdo.bindValue(":id_usuario" , adb.getIdUsuario());
            if(adb.getToken() == ""){
                getActualToken();
            }else {
                pdo.executeRemote();
            }
            Log.d("avanco", pdo.getSql());
        }
    }

    public void getActualToken(){
        final AlternativeDB adb = new AlternativeDB(activity);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        AbstractBD pdo = new AbstractBD(activity);
                        pdo.prepare("update usuario set token = :token where id_usuario = :id_usuario");
                        pdo.bindValue(":token" , token);
                        pdo.bindValue(":id_usuario" , adb.getIdUsuario());
                        pdo.executeRemote();
                        adb.setToken(token);
                        Log.d("avanco", pdo.getSql());
                    }
                });
    }

}
