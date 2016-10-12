package com.solstice.atoi.rlbet;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


public class LoginScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String DB_PATH;
    private String DB_NAME = "googleaccounts.db";
    private static final int RC_SIGN_IN = 9001;
    private static final String REGDIALOG = "register_dialog";
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private ImageButton facebookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   WindowManager.LayoutParams.FLAG_FULLSCREEN); //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("GoogleSign", 0); // 0 - for private mode
        try {
            String destPath = "/data/data/" + getPackageName()
                    + "/databases/googleaccounts";
            File file = new File(destPath);
            File path = new File("/data/data/" + getPackageName()
                    + "/databases/");
            if (!file.exists()) {
                path.mkdirs();
                CopyDB(getBaseContext().getAssets().open("googleaccounts.db"),
                        new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        facebookBtn = (ImageButton)findViewById(R.id.imgBtnFacebook);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFbLogin();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void onFbLogin()
    {
        callbackManager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","user_photos","public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result"+jsonresult);

                                                String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_firstname = json.getString("first_name");
                                                String str_lastname = json.getString("last_name");
                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("FaceSign", 0);
                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putInt("facebooksigned", 1);
                                                FacebookAccount f = new FacebookAccount(str_firstname, str_lastname, str_email);
                                                DBAdapter db = new DBAdapter(getBaseContext());
                                                db.open();
                                                db.createFacebook(f);
                                                db.close();
                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                startActivity(intent);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnGoogle:
                SharedPreferences prefs = getSharedPreferences("GoogleSign", MODE_PRIVATE);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.imgBtnFacebook:
                LoginManager.getInstance().logInWithReadPermissions(this,
                        Arrays.asList("public_profile", "user_friends"));
                break;
            case R.id.tvRegister:
                showEditDialog();
                break;
            case R.id.tvLogin:
                showLoginDialog();
                break;
        }
    }

    public void showLoginDialog() {
        FragmentManager fm = getSupportFragmentManager();
        LoginDialog ld = LoginDialog.newInstance();
        ld.show(fm, "login_dialog");
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RegisterDialog rd = RegisterDialog.newInstance();
        rd.show(fm, REGDIALOG);
    }


    public void CopyDB(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        // Copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int signal = result.getStatus().getStatusCode();
            Log.d("sig", signal+"");
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            DBAdapter db = new DBAdapter(this);
            db.open();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            GoogleAccount g = new GoogleAccount(personName, email, personPhotoUrl);
            db.create(g);
            db.close();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("GoogleSign", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("signed", 1);
            editor.commit();
            Log.e("Infos", "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("info", personName+","+email+","+personPhotoUrl);
            startActivity(intent);


        } else {
            // Signed out, show unauthenticated UI.
            //Toast.makeText(getBaseContext(), "Failed to log in", Toast.LENGTH_LONG).show();
        }
    }
}
