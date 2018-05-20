import java.net.*;
import java.io.*;

public class ChatServer implements Runnable{

    //variables declaration
    private ChatServerThread clients[]=new ChatServerThread[50];
    private ServerSocket server=null;
    private Thread thread=null;
    private int clientCount=0;

    //constructor initializing connection to port in parameter
    public ChatServer(int port){

        try{

            System.out.println("Binding to port "+port+", please wait  ...");
            server=new ServerSocket(port);
            System.out.println("Server started: "+server);
            start();

        }

        catch(IOException ioe){

            System.out.println("Can not bind to port "+port+": "+ioe.getMessage());

        }

    }

    //always running in this thread, waiting for a connection and automatically accepting it
    public void run(){

        while(thread!=null){

            try{

                System.out.println("Waiting for a client ...");
                addThread(server.accept());

            }

            catch(IOException ioe){

                System.out.println("Server accept error: "+ioe);
                stop();

            }

            catch (InterruptedException e) {

                e.printStackTrace();

            }

        }

    }

    //starts thread
    public void start(){

        if(thread==null){

            thread=new Thread(this);
            thread.start();

        }

    }

    //stops thread
    public void stop(){

        if (thread!=null){

            thread.stop();
            thread=null;

        }

    }

    //returns client position in client array with username in parameter. If not present return -1
    private int findClient(String username){

        for(int i=0; i<clientCount; i++){

            if(clients[i].getUsername().equals(username)){

                return i;

            }

        }

        return -1;

    }

    //sends a message with the sender, to the reciever
    public synchronized void handle(String sender, String reciever, String input){

        if(input.equals(".bye")){

            clients[findClient(sender)].send(".bye");
            remove(sender);

        }

        else{

            clients[findClient(reciever)].send(sender+": "+input);

        }

    }

    //removes client with username in parameters
    public synchronized void remove(String username){

        int pos=findClient(username);

        if(pos>=0){

            ChatServerThread toTerminate=clients[pos];
            System.out.println("Removing client thread "+username+" at "+pos);

            if(pos<clientCount-1){

                for (int i=pos + 1; i<clientCount; i++){

                    clients[i-1]=clients[i];

                }

            }

            clientCount--;

            try{

                toTerminate.close();

            }

            catch(IOException ioe){

                System.out.println("Error closing thread: "+ioe);

            }

            toTerminate.stop();

        }

    }

    //creates new thread when a connection is accepted
    private void addThread(Socket socket) throws IOException, InterruptedException{

        if(clientCount<clients.length){

            System.out.println("Client accepted: "+socket);
            clients[clientCount]=new ChatServerThread(this, socket);

            try{

                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;

            }

            catch(IOException ioe){

                System.out.println("Error opening thread: "+ioe);

            }

        }

        else {

            System.out.println("Client refused: maximum "+clients.length+" reached.");

        }

    }

    //main starting constructor and setting it to wait for connections on port 1337
    public static void main(String[] args){

        ChatServer server=new ChatServer(1337);

    }

}


