package dk.via.chatsystem.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;
    private Server server;

    public ConnectionHandler(Socket client, Server server){
        this.client=client;
        this.server=server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println("Enter a nickname: ");
            nickname = in.readLine();
            System.out.println(nickname + " connected.");
            server.broadcast(nickname + " joined the chat.");
            String message;
            while ((message = in.readLine()) != null){
                if (message.startsWith("/nick ")){
                    String[] messageSplit = message.split(" ",2);
                    if (messageSplit.length == 2){
                        server.broadcast(nickname + " changed nickname to " + messageSplit[1]);
                        System.out.println(nickname + " changed nickname to " + messageSplit[1]);
                        nickname = messageSplit[1];
                        out.println("Successfully changed nickname to "+ nickname); //to change nickname you have to
                                                                                    //enter "/nick <<newNickname>>"
                    } else {
                        out.println("No nickname provided.");
                    }
                } else if (message.startsWith("/quit")){
                    server.broadcast(nickname + " left the chat.");
                    shutdown();
                } else {
                    server.broadcast(nickname + ": " + message);
                }
            }
        }catch (IOException e){
            shutdown();
        }
    }
    public void sendMessage(String message){
        out.println(message);
    }

    public void shutdown() {
        try {
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        }catch (IOException e){
            //ignore
        }
    }
}
