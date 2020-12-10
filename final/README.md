# Gradient server using TCP

This project simulates a distributed machine learning system. The project is broken into two parts: server and client. 

## Server

The server is responsible for 1) sending the idling clients the current weights of the model 2) receiving gradient updates from clients and incorporates those updates into its own model. 

## Client

The client is responsible for receiving gradient updates from the server, computing a gradient update using gradient descent calculations, and then sending the update back to the server.

## Running

To run this project, run server.py first to start up the server. Enter an ip address such as 127.0.0.1 and a port number such as 1234. Then run client.py as many times as you would like depending on how many clients you want. Enter in the same ip and port to connect to the server.

## Expected Results

After the client begins to run, everything is automatic. Check Server's output to see the training results. One thing to notice is that sometimes when a single client gets connected to server too long it might block the TCP connection. Soif you want to use multiple clients, you should try to connect at least 2 clients to the server within 30s. 
