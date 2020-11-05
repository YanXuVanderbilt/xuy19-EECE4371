package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {


    Button button;
    TextInputEditText username_input;
    TextInputEditText password_input;
    public static EmailClient client;

    public MainActivity() throws IOException {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new EmailClient();
                } catch (IOException e) {
                    Log.i("debug", "exception in creating new client");
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            login();
                        } catch (IOException e) {
                            Log.i("debug", "exception in creating new client");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });




    }

    public void login() throws IOException {


        String name = username_input.getText().toString();
        String pass = password_input.getText().toString();

        if (name.isEmpty()) name = "a";
        if (pass.isEmpty()) pass = "a";

        client.userName = name;
        client.outToServer.writeBytes(client.Android_login(name, pass) + "\n");
        String login_ack = client.serverBufferedReader.readLine();
        if (EmailClient.DEBUG) System.out.println(login_ack);
        EmailProtocolMessage login_msg = new EmailProtocolMessage(login_ack);
        if (!EmailClient.login_ack(login_ack)) {
            System.out.println("No response from server. Login failed.\n");
            return;
        }
        if (!EmailClient.authenticate(login_msg)) {
            System.out.println("Authentication failed");
        } else {
            client.token = login_msg.getParam(EmailProtocolMessage.TOKEN_KEY);
            openInbox();
        }

    }



    public void openInbox() {
        Intent intent = new Intent(this, Inbox.class);
        startActivity(intent);
    }
}