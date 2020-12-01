import ast
import socket
import threading
import sys

import torch
import numpy as np

from network import network

try:
    import cPickle as pickle
except:
    import pickle

NN = network()

d_W1 = NN.W1 * 0
d_W2 = NN.W2 * 0

#Wait for incoming data from server
#.decode is used to turn the message in bytes to a string
def receive(socket, signal):
    global NN
    global d_W1
    global d_W2
    lst1 = d_W1.tolist()
    lst2 = d_W2.tolist()
    lst = [lst1, lst2]
    msg = pickle.dumps(lst)
    while signal:
        try:
            #rint("recving")
            data = socket.recv(10000)
            #if data == "": continue
            #data = data.decode()
            #print(data)
            weights = pickle.loads(data)
            #print(weights)
            #print(weights)
            W1 = weights[0]
            W2 = weights[1]
            X = weights[2]
            y = weights[3]
            t_W1 = torch.FloatTensor(W1)
            t_W2 = torch.FloatTensor(W2)
            X = torch.FloatTensor(X)
            y = torch.FloatTensor(y)
            NN.W1 = t_W1
            NN.W2 = t_W2
            o = NN.forward(X)
            #NN.z = torch.matmul(X, NN.W1)  # 3 X 3 ".dot" does not broadcast in PyTorch
            #NN.z2 = NN.sigmoid(NN.z)  # activation function
            #NN.z3 = NN.matmul(NN.z2, NN.W2)
            #o = NN.sigmoid(NN.z3)  # final activation function
            o_error = y - o  # error in output
            o_delta = o_error * NN.sigmoidPrime(o)  # derivative of sig to error
            z2_error = torch.matmul(o_delta, torch.t(NN.W2))
            z2_delta = z2_error * NN.sigmoidPrime(NN.z2)
            d_W1 = torch.matmul(torch.t(X), z2_delta)
            d_W2 = torch.matmul(torch.t(NN.z2), o_delta)
            lst1 = d_W1.tolist()
            lst2 = d_W2.tolist()
            lst = [lst1, lst2]
            print(lst)
            msg = pickle.dumps(lst)
            #print(msg)
            sock.sendall(msg)
            #sock.sendall(str.encode(data))
        except:
            print("exception")
            #while True:
            #    sock.sendall(msg)
            #print("You have been disconnected from the server")
            #signal = False
            #break

#Get host and port
host = input("Host: ")
port = int(input("Port: "))

#Attempt connection to server
try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((host, port))
except:
    print("Could not make a connection to the server")
    input("Press enter to quit")
    sys.exit(0)

#Create new thread to wait for data
receiveThread = threading.Thread(target = receive, args = (sock, True))
receiveThread.start()

#Send data to server
#str.encode is used to turn the string message into bytes so it can be sent across the network
#while True:
    #message = input()
    #message = str([[1,1,1],[1,1,1]])
lst1 = d_W1.tolist()
lst2 = d_W2.tolist()
lst = [lst1, lst2]
message = pickle.dumps(lst)
#print(message)
sock.sendall(message)
