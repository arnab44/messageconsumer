package com.message.consumer;

import com.tcpmanager.BrokerClient.BrokerClient;
import com.tcpmanager.Message.Message;

import java.io.*;
import java.net.Socket;

public class Consumer {

    String path = null;
    int consumerID;
    private BrokerClient brokerClient;
    private ObjectOutputStream os;
    private static final String BROKER_IP = "127.0.0.1";
    private static final int BROKER_PORT = 6000;
    public Consumer(String path, int consumerID) {
        this.path = path;
        this.consumerID = consumerID;
        this.brokerClient = BrokerClient.getBrokerClient();
        this.brokerClient.setListenPort(BROKER_PORT);
        this.brokerClient.setListenUrl(BROKER_IP);
    }

    public void connectServer() {
        try {
            Socket socket = new Socket(BROKER_IP, BROKER_PORT);
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            os.writeUTF("hello");
            os.flush();
            receiveMessage(is);

        }
        catch(IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public void receiveMessage(ObjectInputStream is) {

        boolean moreMessage = true;
        while(moreMessage) {
            try {
                Message message = (Message)is.readObject();
                if(message.getHeader().getSize() < 0) {
                    moreMessage = false;
                    break;
                }
                storeMessage(message);
            }
            catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }

    }

    public void storeMessage(Message message) {
        String fileName = message.getHeader().getFileName();
        String fileFullPath = path+"/"+fileName;
        File file = new File(fileFullPath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(message.getPayLoad());
            writer.close();
        }
        catch(IOException ex) {
            System.out.println(ex.toString());
        }
    }
}
