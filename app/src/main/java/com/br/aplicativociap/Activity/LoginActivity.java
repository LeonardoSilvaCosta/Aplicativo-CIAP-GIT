package com.br.aplicativociap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.aplicativociap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private BootstrapEditText edtEmail, edtSenha;
    private BootstrapButton btnLogin, btnCancelar, btnCadastrar;
    private TextView txtRecuperar;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    private Dialog dialog;
    private BootstrapButton btnCancelarAlerta, btnEnviarEmail;
    private BootstrapEditText edtEnviarEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtSenha = (BootstrapEditText) findViewById(R.id.edtSenha);
        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);
        btnCancelar = (BootstrapButton) findViewById(R.id.btnCancelar);
        btnCadastrar = (BootstrapButton) findViewById(R.id.btnCadastrar);
        txtRecuperar = (TextView) findViewById(R.id.txtRecuperar);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                efetuarLogin(edtEmail.getText().toString(), edtSenha.getText().toString());
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirCadastro();

            }
        });

        txtRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirDialog();

            }
        });
    }
    private void efetuarLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

                            abrirMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    private void abrirCadastro(){
        Intent intent = new Intent(LoginActivity.this, AtividadeCadastro.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            abrirMainActivity();
        }

    }

    private void abrirMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void abrirDialog(){
        dialog = new Dialog(LoginActivity.this);

        dialog.setContentView(R.layout.alerta_recuperar_senha);

        btnCancelarAlerta = (BootstrapButton) dialog.findViewById(R.id.btnCancelarAlerta);
        btnEnviarEmail = (BootstrapButton) dialog.findViewById(R.id.btnEnviarEmail);
        edtEnviarEmail = (BootstrapEditText) dialog.findViewById(R.id.edteEnviarEmail);

        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.sendPasswordResetEmail(edtEnviarEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");
                                    Toast.makeText(LoginActivity.this, "Verifique a sua caixa de e-mail", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                dialog.dismiss();

            }
        });

        btnCancelarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }
}
