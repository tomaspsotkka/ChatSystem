package dk.via.chatsystem.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class InputHandler implements Runnable{

    private Client c;
    private boolean done;

    public InputHandler(Client c){
        this.c=c;
    }

    @Override
    public void run() {
        try{
            BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
            while(!done){
                String message = inReader.readLine();
                if (message.equals("/quit")){
                    c.getOut().println(message); //actually sending the message to server
                    inReader.close();
                    c.shutdown();
                }else {
                    c.getOut().println(message);  // didn't know how to access in and out fields from client class so
                                                  // so I created getters in client class
                }
            }
        }catch (IOException e){
            c.shutdown();
        }
    }
}
