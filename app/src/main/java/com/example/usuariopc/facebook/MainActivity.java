package com.example.usuariopc.facebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        // Establecer las devoluciones de llamada
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);


        // Registrar las devoluciones de llamada
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        /*info.setText(
                                "User ID: "
                                        + loginResult.getAccessToken().
                                        getUserId()
                                        + "n" +
                                        "Auth Token: "
                                        + loginResult.getAccessToken().
                                        getToken()
                        );
                        makeToast(loginResult.getAccessToken().
                                toString());*/
                        Profile profile = Profile.getCurrentProfile();
                        datos(profile);
                        accessTokenTracker = new AccessTokenTracker() {
                            @Override
                            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                            }
                        };
                        profileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                datos(currentProfile);
                            }
                        };
                        accessTokenTracker.startTracking();
                        profileTracker.startTracking();
                        loginButton.setReadPermissions("user_friends");
                        loginButton.setReadPermissions("public_profile");

                    }


                    @Override
                    public void onCancel() {
                        String cancelMessage = "Login Cancelado.";
                        info.setText(cancelMessage);
                        makeToast(cancelMessage);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        String errorMessage = "Login error.";
                        info.setText(errorMessage);
                        makeToast(errorMessage);
                    }
                }
        );

        // Inicializar el Texview
        info = (TextView) findViewById(R.id.info);

        if(isLoggedIn())
         makeToast("User ID: "
                    + AccessToken.getCurrentAccessToken().getUserId());
    }

    /**
     * Comprueba si el usuario ha iniciado sesión en Facebook y el
     token de acceso está activo
     * @return
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (accessToken != null) && (!accessToken.isExpired());
    }

    /**
     * datos de interés en el gestor de devolución de llamada
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data)
        ;
    }

    /**
     * creamos los toast
     * @param text
     */
    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //los logs de 'instalar' y 'aplicación activa' App Eventos.
        AppEventsLogger.activateApp(this);
        Profile profile = Profile.getCurrentProfile();
        datos(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs de'app desactivada' App Eventos.
        AppEventsLogger.deactivateApp(this);
    }
    private void datos(Profile perfil){
        if (perfil!= null){
            String nombre =perfil.getFirstName();
            info.setText(nombre);
        }
    }
}