package com.demo;

import com.demo.constrants.Config;
import com.demo.constrants.LoginForm;
import com.demo.constrants.Message;
import com.demo.constrants.MessageEvent;
import com.demo.utils.MessageSendUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server  implements Runnable{

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Server() {
        connections= new ArrayList<>();
        done = false;
    }


    @Override
    public void run() {
        try{
            server = new ServerSocket(Config.PORT);
            while (!done) {
                pool = Executors.newCachedThreadPool();
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);

            }

        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
        }


    }

    public void broadcast(String message){
        for(ConnectionHandler ch: connections){
            if(ch != null){
                ch.sendMessage(message);
            }
        }
    }

    public void sendMessage(Message message, String username){
        for(ConnectionHandler ch: connections){
            if(!isEmpty(ch.username)&& ch.username.equals(username)){
                ch.sendMessage(message.toString());
                // return;
            }
        }
        System.out.println("用户未在线，发送失败");
    }

    private boolean isEmpty(String username) {
        return username == null || username.trim().isEmpty();
    }

    public void shutdown () {
        try {
            done = true;
            if (!server.isClosed()) {
                server.close();
            }
            for(ConnectionHandler ch: connections){
                ch.shutdown();
            }
        }catch (IOException e) {
            //ignore
        }
    }


    class ConnectionHandler implements Runnable{
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;
        private String username;


        public ConnectionHandler (Socket client) {
            this.client=client;
        }

        @Override
        public void run() {
            try {
                out=new PrintWriter(client.getOutputStream(),true);
                in=new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message;
                while((message = in.readLine()) != null){
                    handleMessageFromClient(message);
                }
            }catch (IOException e){
                shutdown();
            }

        }
        public void sendMessage(String message){
            out.println(message);

        }

        private void handleMessageFromClient(String msg) {
            Message message = MessageSendUtil.readMessage(msg);
            System.out.println("message: " + msg);
            if (message != null) {
                switch (message.getEvent()) {
                    case MessageEvent.USER_JOIN:
                        String username1 = message.getUsername();
                        this.username = username1;
                        System.out.println("用户上线了，名称：" + username1);
                        break;
                    case MessageEvent.USER_MESSAGE:
                        Server.this.sendMessage(message, message.getUsername());
                        // broadcast(message.getContent());
                        break;
                    case MessageEvent.USER_LOGIN:
                        LoginForm loginForm = MessageSendUtil.readLoginMessage(message.getContent());
                        username = loginForm.getUsername();
                        break;
                }
            }
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            }catch (IOException e) {
                //ignore
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

}
