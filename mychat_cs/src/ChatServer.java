import java.net.*;
import java.io.*;

public class ChatServer implements Runnable{

    private ChatServerThread clients[]=new ChatServerThread[50];
    private ServerSocket server=null;
    private Thread thread=null;
    private int clientCount=0;

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

    public void start(){

        if(thread==null){

            thread=new Thread(this);
            thread.start();

        }

    }

    public void stop(){

        if (thread!=null){

            thread.stop();
            thread=null;

        }

    }

    private int findClient(String username){

        for(int i=0; i<clientCount; i++){

            if(clients[i].getUsername().equals(username)){

                return i;

            }

        }

        return -1;

    }

    public synchronized void handle(String sender, String reciever, String input){

        if(input.equals(".bye")){

            clients[findClient(sender)].send(".bye");
            remove(sender);

        }

        else{

            clients[findClient(reciever)].send(sender+": "+input);

        }

    }

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

    public static void main(String[] args){

        ChatServer server=new ChatServer(1337);

    }

}


