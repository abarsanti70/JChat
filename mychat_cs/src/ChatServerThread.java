import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread{

    //variables declaration
    private ChatServer server;
    private Socket socket=null;
    private String username=null;
    private String password=null;
    private String reciever=null;
    private final String usr="admin";
    private final String psw="password";
    private DataInputStream streamIn=null;
    private DataOutputStream streamOut=null;

    //constructor
    public ChatServerThread(ChatServer _server, Socket _socket) throws IOException, InterruptedException{

        super();
        server=_server;
        socket=_socket;
        open();
        //reads username and password that are sent through socket
        username=streamIn.readUTF();
        password=streamIn.readUTF();

        //verifies if username & password are incorrect
        if(!username.equals(usr) || !password.equals(psw)){

            streamOut.writeUTF("Wrong username or password. Closing in 5 seconds...");
            close();

        }

        //if they are not doesn't close connection
        else{

            streamOut.writeUTF("Logged in.");

        }

    }

    //writes the message in the parameter
    public void send(String msg){

        try{

            streamOut.writeUTF(msg);
            streamOut.flush();

        }

        catch(IOException ioe){

            System.out.println(username+" ERROR sending: "+ioe.getMessage());
            server.remove(username);
            stop();

        }

    }

    //returns current username
    public String getUsername(){

        return username;

    }


    //method always running for thread
    public void run(){

        System.out.println("Server Thread "+username+" running.");

        while(true){

            try{

                //handles a message (from, to, message)
                reciever=streamIn.readUTF();
                server.handle(username, reciever, streamIn.readUTF());

            }

            catch(IOException ioe){

                System.out.println(username+" ERROR reading: "+ioe.getMessage());
                server.remove(username);
                stop();

            }

        }

    }

    //opens connection
    public void open() throws IOException{

        streamIn=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    }

    //closes connection
    public void close() throws IOException{

        if(socket!=null){

            socket.close();

        }

        if(streamIn!=null){

            streamIn.close();

        }

        if(streamOut!=null){

            streamOut.close();

        }

    }

}