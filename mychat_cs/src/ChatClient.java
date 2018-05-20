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
    private ChatClientThread client=null;

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

            System.out.println("Good bye. Press RETURN to exit ...");
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

        if (thread==null){

            client=new ChatClientThread(this, socket);
            thread=new Thread(this);
            thread.start();

        }

    }

    //Method stop() interrupts the connection and closes threads
    public void stop(){

        if (thread!=null){

            thread.stop();
            thread=null;

        }

        try{

            if(T!=null){

                T.close();

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
        client.stop();

    }

    public static void main(String args[]) throws IOException{

        //input standards
        BufferedReader T=new BufferedReader(new InputStreamReader(System.in));

        //creating ChatClient object
        ChatClient client=null;

        //login format
        System.out.print("username: ");
        String usr=T.readLine();
        System.out.print("password: ");
        String psw=T.readLine();

        //initializing ChatClient object with the ip of the server (right now set to localhost - loopback address 127.0.0.1),
        // port on which server is waiting for a connection (in this case port 1337), username and password.
        client=new ChatClient("127.0.0.1", 1337, usr, psw);

    }

}