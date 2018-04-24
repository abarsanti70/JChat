import java.net.*;
import java.io.*;

public class ChatClient implements Runnable{

    //variables declaration
    private Socket socket=null;
    private Thread thread=null;
    private String username=null;
    private String password=null;
    private String reciever=null;
    private BufferedReader T=null;
    private DataOutputStream streamOut=null;
    private DataInputStream streamIn=null;
    private ChatClientThread client=null;
    private boolean sl=false;

    //constructor for initializing the connection and verifying username and password
    public ChatClient(String serverName, int serverPort, String username, String password){

        System.out.println("Establishing connection. Please wait ...");

        try{

            socket=new Socket(serverName, serverPort);
            this.username=username;
            this.password=password;
            System.out.println("Connected: "+socket);
            start();
            streamOut.writeUTF(username);
            streamOut.writeUTF(password);
            streamOut.flush();
            
            boolean a=streamIn.readBoolean();
            System.out.println(a);
            
            if(!a){
                
                System.out.println("stopping");
                stop();
                
            }

        }

        catch(UnknownHostException uhe){

            System.out.println("Host unknown: "+uhe.getMessage());

        }


        catch(IOException ioe){

            System.out.println("Unexpected exception: "+ioe.getMessage());

        }

    }

    //Overriding method run() to be called by method start()
    @Override
    public void run(){

        //while the thread has been created stream to the sever everything typed by keyboard
        while(thread!=null){

            try{

                reciever=username;
                streamOut.writeUTF(reciever);
                streamOut.writeUTF(T.readLine());
                streamOut.flush();

            }

            catch(IOException ioe){

                System.out.println("Sending error: " + ioe.getMessage());
                stop();

            }

        }

    }

    //Method handle verifies if a message is ".bye" in order to disconnect the client from the server
    public void handle(String msg){

        if(msg.equals(".bye")){

            stop();

        }

        else{

            System.out.println(msg);

        }

    }

    //Method start() creates a new Thread and a ChatClientThread object
    public void start() throws IOException{

        T=new BufferedReader(new InputStreamReader(System.in));
        streamOut=new DataOutputStream(socket.getOutputStream());
        streamIn=new DataInputStream(socket.getInputStream());

        if (thread==null){

            client=new ChatClientThread(this, socket);
            thread=new Thread(this);
            thread.start();

        }

    }

    //Method stop() interrupts the connection and closes threads
    public void stop(){
        
        sl=true;

        if (thread!=null){

            thread.stop();
            thread=null;
            
            System.out.println("Thread stopped and null");

        }

        try{

            if(T!=null){

                T=null;

            }

            if (streamOut!=null){

                streamOut.close();

            }

            if(socket!=null){

                socket.close();

            }

        }

        catch(IOException ioe){

            System.out.println("Error closing ...");

        }

        client.close();
        
        System.out.println("ChatClientThread closed");
        
        client.stop();
        
        System.out.println("Thread ChatClientThread Stopped");

    }
    
    public boolean isOpen(){
        
        boolean a=false;
        
        if(T!=null){
            
            a=true;
            System.out.println("ChatClient is open");
            
        }
        
        System.out.println("Returning "+a);
        
        return a;
        
    }
    
    public boolean stopListening(){
        
        System.out.println("Returning "+sl);
        return sl;
        
    }

}