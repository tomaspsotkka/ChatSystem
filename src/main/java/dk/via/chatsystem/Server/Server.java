package dk.via.chatsystem.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private ArrayList<ConnectionHandler>connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try{
            server = new ServerSocket(8888);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client, this);
                connections.add(handler);
                pool.execute(handler);  //every time we add a new connection, we want to execute the handler,
                                        //which is going to execute the run function :)
            }
        }catch (Exception e){
            shutdown();
        }
    }

    public void broadcast(String message){ // sends the message to every connection (connected client)
        for (ConnectionHandler ch : connections){
            if (ch != null){
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown(){
        try {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections){
                ch.shutdown();
            }
        }catch (IOException e){
            //ignore
        }
    }
}