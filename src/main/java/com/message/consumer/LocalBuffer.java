package com.message.consumer;


import com.google.inject.Singleton;
import com.tcpmanager.Message.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Queue;

@Getter
@Setter
@Builder
@Singleton
public class LocalBuffer {

    Queue<Message> q;
    Long size ;
    private static final Long MAX_SIZE = Long.valueOf(2L*1024L*1024L*1024L);

    public LocalBuffer(Queue q, Long size){
        this.q = q;
        this.size = size ;
    }

    public boolean push(Message m) {
        // System.out.println((size + m.getHeader().getSize()) + " max "+ MAX_SIZE);
        if((size + m.getHeader().getSize()) > MAX_SIZE) {
            System.out.println("Exceeding queue size");
            return  false;
        }
        try{
            this.getQ().add(m);
            size = size +  m.getHeader().getSize();
            return true;
        }
        catch (Exception ex) {
            System.out.println("Error while push into queue "+ ex.toString());
            return false;
        }
    }

    public  Message poll(){
        Message m =  this.getQ().poll();
        return  m;
    }

}
