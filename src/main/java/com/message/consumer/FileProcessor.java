package com.message.consumer;


import com.tcpmanager.Message.Message;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileProcessor implements Runnable {

    private LocalBuffer lb;
    private String path;
    private static boolean moreMessage = true;

    FileProcessor(LocalBuffer lb, String path) {
        this.lb = lb;
        this.path = path;
    }

      public  void run() {

        processFile();

      }

      public void processFile() {

      //  boolean moreMessage = true;

        while(moreMessage) {
            if (!lb.getQ().isEmpty()) {

                Message message = (Message) lb.poll();
                if(message.getHeader().getSize() < 0) {
                    moreMessage = false;
                    break;
                }
                String fileName = message.getHeader().getFileName();
                String fileFullPath = path + "/" + fileName;
                File file = new File(fileFullPath);

                try {


                    FileOutputStream fos = new FileOutputStream(file);
                 //   FileChannel fileChannel = fos.getChannel();

                    fos.write(message.getPayLoad().getBytes());
//                    fos.flush();
                    fos.close();
//                    FileWriter fileWriter = new FileWriter(file);
                  //  fileWriter.write(message.getPayLoad().getBytes());

//                    BufferedWriter writer = new BufferedWriter(fileWriter);
//                    writer.write(message.getPayLoad());
                    //writer.close();
//                    fileWriter.close();
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }
            }
        }

          System.out.println("Fileprocessor "+ Thread.currentThread().getId() + " finished at "+
                  new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));



      }





}
