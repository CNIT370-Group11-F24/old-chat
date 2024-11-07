package com.demo;

import com.demo.constrants.Config;
import com.demo.constrants.Message;
import com.demo.constrants.MessageEvent;
import com.demo.utils.MessageSendUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Client implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    public abstract void handleMessage(String message);

    @Override
    public void run() {
        try{
            Socket client = new Socket("localhost" , Config.PORT);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String line;
                    System.out.println("begain");
                     {
                        try {
                            while (true) {
                                if (((line = in.readLine()) != null)) {
//                                    System.out.println("Received message from client: " + line);
                                    handleMessage(line);
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            try {
                                in.close();
                                out.close();
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }).start();

        }catch(IOException e){
            shutdown();
        }
    }

    public void sendMessage(String message,String username) {
        MessageSendUtil.sendMessage(out, message, username);
    }

    private void sendMessage(Message message) {
        System.out.println("发送消息：" + message);
        out.println(message.toString());
    }


    public void shutdown() {
        done= true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }

        } catch (IOException e){
            //ignore
        }
    }

    public void joinRoom(String username) {
        Message message = new Message();
        message.setEvent(MessageEvent.USER_JOIN);
        message.setUsername(username);
        sendMessage(message);
    }

    class InputHandler implements Runnable{
        @Override
        public void run() {
            try{
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done){
                    String message = inReader.readLine();
                    if(message.equals("")){
                        System.out.println("Please enter a message");
                        continue;
                    }
                    handleMessage(message, out);
                }

            } catch (IOException e) {
                shutdown();
            }
        }
    }

    private void handleMessage(String message, PrintWriter out) {
        if(message.equals("/quit")) {
            shutdown();
        }else{
            MessageSendUtil.sendMessage(out, message, "456");
        }
    }


    public static void main(String[] args) {
        Client client = new Client(){
            @Override
            public void handleMessage(String message) {
                System.out.println("Received message from client: " + message);
            }
        };
        client.run();
        client.joinRoom("123");
        client.sendMessage("test", "123");
    }

}