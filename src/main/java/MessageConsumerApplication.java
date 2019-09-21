import com.message.consumer.Consumer;

import java.util.Random;


public class MessageConsumerApplication {

    public static  void  main(String[] args) {
        System.out.println("heap memory allocated" + Runtime.getRuntime().maxMemory()/1024/1024 + "MB");
        //String path = args[0];

        String path ="/home/ec2-user//sampleData3" ;
        Random random = new Random();

        Consumer consumer = new Consumer(path,random.nextInt(), null);
        consumer.connectServer();
    }
}
