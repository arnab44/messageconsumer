import com.message.consumer.Consumer;

import java.util.Random;

public class MessageConsumerApplication {

    public static  void  main(String[] args) {

        //String path = args[0];
        String path ="/Users/arnabs/Desktop/sampleData3" ;
        Random random = new Random();
        Consumer consumer = new Consumer(path,random.nextInt());
        consumer.connectServer();
    }
}
