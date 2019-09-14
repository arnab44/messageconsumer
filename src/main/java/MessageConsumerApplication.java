import com.message.consumer.Consumer;
import com.message.consumer.LocalBuffer;
import com.tcpmanager.Message.Message;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageConsumerApplication {

    public static  void  main(String[] args) {
        System.out.println("heap memory" + Runtime.getRuntime().maxMemory()/1024/1024 + "MB");
        //String path = args[0];
        String path ="/Users/arnabs/Desktop/sampleData3" ;
        Random random = new Random();

        Consumer consumer = new Consumer(path,random.nextInt(), null);
        consumer.connectServer();
    }
}
