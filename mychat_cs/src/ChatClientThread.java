import java.net.*;
import java.io.*;

public class ChatClientThread extends Thread{

    //variables declaration
    private Socket socket=null;
    private ChatClient client=null;
    private DataInputStream streamIn=null;

    //constructor
    public ChatClientThread(ChatClient _client, Socket _socket){

        client=_client;
        socket=_socket;
        open();
        start();

    }


    //Method open to start streaming in
    public void open(){

        try{

            streamIn=new DataInputStream(socket.getInputStream());

        }

        catch(IOException ioe){

            System.out.println("Error getting input stream: "+ioe);
            client.stop();

        }

    }

    //Method close to close the connection
    public void close(){

        try{

            if(streamIn!=null){

                streamIn.close();

            }

        }

        catch(IOException ioe){

            System.out.println("Error closing input stream: "+ioe);

        }

    }

    //Overriding method run() to be called by method start()
    @Override
    public void run(){

        //Infinite cycle handling the input stream
        while(true){

            try{

                client.handle(streamIn.readUTF());

            }

            catch(IOException ioe){

                System.out.println("Listening error: "+ioe.getMessage());
                client.stop();

            }

        }

    }

}