package com.message.consumer;

import com.tcpmanager.BrokerClient.BrokerClient;
import com.tcpmanager.Message.Message;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Consumer {

    String path = null;
    int consumerID;
    private BrokerClient brokerClient;
    private ObjectOutputStream os;
    private List<LocalBuffer> lb;
  //  private List<Thread> fp;
    private static final int NUM_FILEPROCESSORS = 10;
    public Consumer(String path, int consumerID, LocalBuffer lb) {
        this.path = path;
        this.consumerID = consumerID;
        this.lb = new ArrayList<LocalBuffer>();
        for (int i = 0; i < LocalBuffer.numberOfBuffers; i++){
            this.lb.add(new LocalBuffer( new ConcurrentLinkedQueue<Message>(), 0L));
        }
        this.brokerClient = BrokerClient.getBrokerClient();
        this.brokerClient.setListenPort(BROKER_PORT);
        this.brokerClient.setListenUrl(BROKER_IP);


        for(int i = 0 ; i< NUM_FILEPROCESSORS; i++) {
            Thread fileProcessor = new Thread(new FileProcessor(this.lb.get(i % LocalBuffer.numberOfBuffers), path));
        //    fp.add(fileProcessor);
            fileProcessor.start();
        }
    }
    private static final String BROKER_IP = "35.175.127.44";
    private static final int BROKER_PORT = 4000;

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
        int counter = 0;
        while(moreMessage) {
            try {
                Message message = (Message)is.readObject();
                if(counter==0) {
                    System.out.println("Consumer started at " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                }
               // System.out.println("------");
               // System.out.println(message.getHeader().getFileName());

                lb.get(counter % LocalBuffer.numberOfBuffers).push(message);
                //storeMessage(message);
                counter = counter + 1;
            }
            catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
        System.out.println("Consumer finished at " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));

    }

}
