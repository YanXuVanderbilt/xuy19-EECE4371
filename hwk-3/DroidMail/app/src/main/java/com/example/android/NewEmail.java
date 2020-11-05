package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class NewEmail extends AppCompatActivity {

    Button send;
    TextInputEditText recipient;
    TextInputEditText text;

    public static EmailClient client = MainActivity.client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_email);

        send = findViewById(R.id.Send);
        recipient = findViewById(R.id.to);
        text = findViewById(R.id.body);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        send();
                    }
                });
            }
        });
    }

    public void send() {
        final String to = recipient.getText().toString();
        final String email = text.getText().toString();
        final boolean[] success = {false};
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    success[0] = compose(to, email);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void back() {
        Intent intent = new Intent(this, Inbox.class);
        startActivity(intent);
    }

    public boolean compose(String r, String e) throws IOException {
        String email = "to>" + r + ";from>" + client.userName + ";body>" + e;
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.SENDEMAIL_COMMAND);
        msg.putParam(EmailProtocolMessage.EMAIL_KEY, email);
        msg.putParam(EmailProtocolMessage.TOKEN_KEY, client.token);
        //in.close();
        String m = msg.toString();
        client.outToServer.writeBytes(m + "\n");
        String responseFromServer = client.serverBufferedReader.readLine();
        msg = new EmailProtocolMessage(responseFromServer);
        if (!EmailClient.authenticate(msg)) {
            System.out.println("Authentication failed");
            return false;
        } else {
            back();
            return true;
        }
    }
}