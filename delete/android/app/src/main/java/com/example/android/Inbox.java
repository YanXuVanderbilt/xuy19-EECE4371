package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Inbox extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<String> mEmails = new ArrayList<>();
    public static EmailClient client = MainActivity.client;
    Button refresh;
    Button newEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        recyclerView = findViewById(R.id.email);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        refresh = findViewById(R.id.refresh);
        newEmail = findViewById(R.id.newEmail);
        update();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                });
            }
        });

        newEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transition();
            }
        });


        mAdapter = new MyAdapter(mEmails);
        recyclerView.setAdapter(mAdapter);
    }

    public void transition () {
        Intent intent = new Intent(this, NewEmail.class);
        startActivity(intent);

    }

    public String[] parseEmailString(String response) {
        EmailProtocolMessage msg = new EmailProtocolMessage(response);
        String emails_str = msg.getParam(EmailProtocolMessage.EMAILS_KEY);
        String[] emails = emails_str.split("ZZZ");
        return emails;
    }

    public void update() {
        client = MainActivity.client;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    client.outToServer.writeBytes(client.retrieve() + "\n");
                    String responseFromServer = client.serverBufferedReader.readLine();
                    String[] emails = parseEmailString(responseFromServer);
                    Log.i("debug", String.valueOf(emails.length));
                    mEmails = new ArrayList<String>(Arrays.asList(emails)); //new ArrayList is only needed if you absolutely need an ArrayList
                    mAdapter.reset(mEmails);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}

