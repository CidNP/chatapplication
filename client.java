import java.net.*;
import java.io.*;

public class client{

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public client(){
        try{
            System.out.println("Sending request to server");
            socket=new Socket("127.0.0.1",7777); //ip and port
            System.out.println("Connection Successful.");

            
                //Client to Server (read)
            br=new BufferedReader (new InputStreamReader(socket.getInputStream()));
                //Server to Client (write)
            out=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();


        }catch(Exception e){
            //handle exception
        }
    }
    public void startReading(){
        //thread- read garirakhxa ani dinxa
        Runnable r1=()->{

            System.out.println("reader started...");

            try{
                while(true){
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                    System.out.println("Server left the chat");
                    socket.close();
                    break;
                    }
                    System.out.println("Server: "+msg);
                }
            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is Closed");
            }

        };

        new Thread(r1).start();
    }


    public void startWriting(){
        //thread- data user sanga linxa ani client lai pathauxa
        Runnable r2=()->{
            System.out.println("writer started...");

            try{
            //loop until client exits it runs
                while(!socket.isClosed()){

                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }

                System.out.println("Connection is Closed");
                
            }catch(Exception e){
                e.printStackTrace();
            }

        };

        new Thread(r2).start();
    }


    public static void main(String[] args){
        System.out.println("this is client...");
        new client();
    }
}