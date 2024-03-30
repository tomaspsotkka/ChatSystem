package dk.via.chatsystem.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {
        try {
            client = new Socket("localhost",8888);
            out = new PrintWriter(client.getOutputStream(), true); //I set atoFlush to true, so we don't have to
                                                                            //flush manually every time we want to send message to the server
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler(this);
            Thread t = new Thread(inputHandler);
            t.start(); //run() doesn't start the thread, so we have to do it manually

            String inMessage;
            while ((inMessage = in.readLine()) != null){
                System.out.println(inMessage);
            }
        }catch (IOException e){
            shutdown();
        }
    }

    public void shutdown(){ // closes everything that is currently running
        done = true;
        try {
            in.close();
            out.close();
            if (!client.isClosed()){
                client.close();
            }
        }catch (IOException e){
            //ignore
        }
    }

    public BufferedReader getIn() { //returns BufferedReader object
        return in;
    }

    public PrintWriter getOut() { //returns PrintWriter object
        return out;
    }
}
