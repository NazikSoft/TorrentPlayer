package com.naziksoft.videoplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.naziksoft.videoplayer.consts.Const;
import com.naziksoft.videoplayer.R;
import com.rey.material.widget.ProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    // UI elements (Butterknife lib).
    @BindView(R.id.editTextEmail)
    com.rey.material.widget.EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    com.rey.material.widget.EditText editTextPassword;
    @BindView(R.id.buttonSignIn)
    com.rey.material.widget.Button bSignIn;
    @BindView(R.id.buttonRegister)
    com.rey.material.widget.Button bRegister;
    @BindView(R.id.bSignInGoogle)
    SignInButton bSignInGoogle;
    @BindView(R.id.progressView)
    ProgressView progressView;

    //  auth elements
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient mGoogleApiClient;
    private String userEmail = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // init UI
        ButterKnife.bind(this);

        // add OnClickListeners
        bRegister.setOnClickListener(this);
        bSignIn.setOnClickListener(this);
        bSignInGoogle.setOnClickListener(this);

        initGoogleApiClient();

        // init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // set authStateListener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    Log.d(Const.TAG, "onAuthStateChanger check sign in:" + user.getUid());
                else
                    Log.d(Const.TAG, "onAuthStateChanger check sign out");

                if (user != null)
                    userEmail = user.getEmail();
            }
        };
    }

    private void initGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    // create new user account
    private void createNewAccount(String email, String password) {
        Log.d(Const.TAG, "createNewAccount: " + email);
        if (!checkValidateInputFills())
            return;

        showProgress();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(Const.TAG, "createUserWithEmail.onComplete: " + task.isSuccessful());
                hideProgress();
                if (!task.isSuccessful())
                    Toast.makeText(AuthActivity.this, R.string.user_with_email_exist, Toast.LENGTH_SHORT).show();
                else {
                    hideProgress();
                    Intent intent = new Intent();
                    intent.putExtra(Const.EXTRA_USER_EMAIL, userEmail);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    // sign in with email and password
    private void signInWithEmail(String email, String password) {
        Log.d(Const.TAG, "signInWithEmail: " + email);
        if (!checkValidateInputFills())
            return;

        showProgress();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(Const.TAG, "signInWithEmail.onComplete: " + task.isSuccessful());
                hideProgress();
                if (!task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, R.string.error_auth, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Const.EXTRA_USER_EMAIL, userEmail);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    // send intent for google account
    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Const.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Const.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(Const.TAG, "Google Sign In failed.");
            }
        }
    }

    // sign in with Google account
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(Const.TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgress();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(Const.TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        hideProgress();

                        // If sign in fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Log.w(Const.TAG, "signInWithCredential", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(Const.EXTRA_USER_EMAIL, userEmail);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    // on buttons click
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                createNewAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                break;
            case R.id.buttonSignIn:
                signInWithEmail(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                break;
            case R.id.bSignInGoogle:
                signInWithGoogle();
                break;
        }
    }

    private void showProgress() {
        progressView.start();
    }

    private void hideProgress() {
        progressView.stop();
    }

    // method for check validate input password and email
    private boolean checkValidateInputFills() {
        boolean result = true;
        String email = editTextEmail.getText().toString();

        if (TextUtils.isEmpty(email) || !email.contains("@")) {
            editTextEmail.setError(getResources().getString(R.string.mail_err));
            result = false;
        } else editTextEmail.setError(null);

        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getResources().getString(R.string.pass_err_empty));
            result = false;
        } else if (password.length() < 6) {
            editTextPassword.setError(getResources().getString(R.string.pass_err_short));
            result = false;
        } else editTextPassword.setError(null);

        return result;
    }

    //  add listener when activity is starting
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    // remove listener when activity was stopped
    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(Const.TAG, "onConnectionFailed:" + connectionResult);
        Snackbar.make(bSignIn, R.string.play_services_error, Snackbar.LENGTH_SHORT).show();
    }
}
