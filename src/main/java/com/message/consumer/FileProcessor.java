package com.message.consumer;


import com.google.common.base.Stopwatch;
import com.tcpmanager.Message.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

                 //   Stopwatch sw = new Stopwatch();
                   // sw.start();

                   /* FileOutputStream fos = new FileOutputStream(file);

                    fos.write(message.getPayLoad().getBytes());
                    fos.close();*/


                    Files.write(Paths.get(file.toURI()),
                            message.getPayLoad().getBytes("utf-8"),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING);
                  //  sw.stop();
                   // System.out.println(message.getHeader().getSize()+" "+sw.elapsed(TimeUnit.MILLISECONDS));
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
