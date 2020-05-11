package com.br.aplicativociap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.aplicativociap.R;
import com.br.aplicativociap.entidades.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AtividadeCadastro extends AppCompatActivity {

    private BootstrapEditText edtNome, edtEmail, edtSenha, edtConfirmarSenha;
    private BootstrapButton btnCadastrar, btnCancelar;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_cadastro);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = new User();

        edtNome = (BootstrapEditText) findViewById(R.id.edtNome);
        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtSenha = (BootstrapEditText) findViewById(R.id.edtSenha);
        edtConfirmarSenha = (BootstrapEditText) findViewById(R.id.edtConfirmarSenha);

        btnCadastrar = (BootstrapButton) findViewById(R.id.btnCadastrar);
        btnCancelar = (BootstrapButton) findViewById(R.id.btnCancelar);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(edtSenha.getText().toString().equals(edtConfirmarSenha.getText().toString())){
                  user.setEmail(edtEmail.getText().toString());
                  user.setNome(edtNome.getText().toString());
                  user.setSenha(edtSenha.getText().toString());

                  criarConta(user);

              }else{
                  Toast.makeText(AtividadeCadastro.this, "As senhas não correspondem", Toast.LENGTH_SHORT).show();
              }





            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirTelaLogin();

            }
        });

    }
    private void criarConta(final User user){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(AtividadeCadastro.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();

                            inserirUsuarioDataBase(user);

                            abrirMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AtividadeCadastro.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void abrirMainActivity(){
        Intent intent = new Intent(AtividadeCadastro.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    private void inserirUsuarioDataBase(User user){

        myRef = database.getReference("usuários");

        String key = myRef.child("usuários").push().getKey();

        user.setKeyUser(key);

        myRef.child(key).setValue(user);
    }

    private void abrirTelaLogin(){
        Intent intent = new Intent(AtividadeCadastro.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
