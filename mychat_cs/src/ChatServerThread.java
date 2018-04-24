import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread{

    //variables declaration
    private ChatServer server =null;
    private Socket socket=null;
    private String username=null;
    private String password=null;
    private String reciever=null;
    //private final String usr="Patrick";
    //private final String psw="password";
    private DataInputStream streamIn=null;
    private DataOutputStream streamOut=null;

    //constructor
    public ChatServerThread(ChatServer _server, Socket _socket) throws IOException, InterruptedException{

        super();
        server=_server;
        socket=_socket;
        open();
        username=streamIn.readUTF();
        password=streamIn.readUTF();

        if(username.equals("Patrick") && password.equals("password")){

            streamOut.writeBoolean(true);
            streamOut.flush();

        }

        else{

            streamOut.writeBoolean(false);
            streamOut.flush();
            close();

        }

    }

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

    public String getUsername(){

        return username;

    }

    public void run(){

        System.out.println("Server Thread "+username+" running.");

        while(true){

            try{

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

    public void open() throws IOException{

        streamIn=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    }

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