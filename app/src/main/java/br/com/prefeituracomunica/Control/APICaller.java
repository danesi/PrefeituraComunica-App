package br.com.prefeituracomunica.Control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import br.com.prefeituracomunica.Model.Parametros;
import br.com.prefeituracomunica.View.NetError;

public class APICaller {

    private Parametros parametros;
    private Context activity;
    private String apiURL;
    private String function;
    private List<String> paramName;
    private List<String> param;



    public APICaller(Context activity) {
        this.parametros = new Parametros();
        this.apiURL = this.parametros.getUrlMestre();
        this.activity = activity;
        this.paramName = new ArrayList<>();
        this.param = new ArrayList<>();
    }

    public void setApiUrl(String api){
        this.apiURL = this.parametros.getUrlMestre()+"/"+api;
    }

    public Parametros getParametros() {
        return parametros;
    }

    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }

    public Context getActivity() {
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }


    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<String> getParamName() {
        return paramName;
    }

    public List<String> getParam() {
        return param;
    }

    public void addParam(String name , String value) {
        this.paramName.add(name);
        this.param.add(value);
    }


    public void success(String response) {

    }

    public void responseError(VolleyError response) {
        Log.d("avanco", "VolleyError");
        activity.startActivity(new Intent(activity , NetError.class));
    }

    public void executeRemote() {
            Log.d("avanco", "1");
            Thread t = new Thread() {
                @Override
                public void run() {
                    Log.d("avanco", "2");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("avanco", response);
                            success(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            responseError(error);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parametros = new Hashtable<String, String>();
                            parametros.put("token_acesso","c-m35jf7k2gh34x6c2j45h7v3k56");
                            for (int i = 0; paramName.size()>i ; i++) {
                                Log.d("avanco", paramName.get(i) +"-----"+ param.get(i));
                                parametros.put(paramName.get(i) , param.get(i));
                            }
                            return parametros;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(stringRequest);
                }
            };
            t.start();
    }

}
