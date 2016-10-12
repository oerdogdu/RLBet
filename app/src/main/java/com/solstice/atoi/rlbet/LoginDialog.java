package com.solstice.atoi.rlbet;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Atoi on 17.09.2016.
 */
public class LoginDialog extends DialogFragment{

    private Button btnLogin;
    private EditText etEmail, etPassword;
    private DBAdapter db;
    private SessionManager session;

    public LoginDialog() {

    }

    public static LoginDialog newInstance() {
        LoginDialog login = new LoginDialog();
        return login;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_dialog, container);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DBAdapter(getActivity());
        etEmail = (EditText) view.findViewById(R.id.txt_email);
        etPassword = (EditText)view.findViewById(R.id.txt_password);
        btnLogin = (Button)view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (!email.isEmpty() && !password.isEmpty())
                    checkLogin(email, password);
                else
                    Toast.makeText(getActivity(), "nope", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLogin(final String email, final String password) {

        String tag_string_req = "req_login";
        String url = AppConfig.URL_LOGIN + "?email="+email+"&password="+password+"";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Log.d("deb", "deb");
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        RegisteredAccount r = new RegisteredAccount(email, password);

                        Intent intent = new Intent(getContext(),
                                MainActivity.class);
                        startActivity(intent);
                        dismiss();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    // HTTP Status Code: 401 Unauthorized
                }
            }
        }) {
        };

        int socketTimeout = 90000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppControler.getInstance().addToRequestQueue(getContext(), strReq, tag_string_req);
    }
}
