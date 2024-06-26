import java.net.*;
import java.io.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    //declare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    //constructor
    public client() {
        try{
            System.out.println("Sending request to server");
            socket=new Socket("127.0.0.1",7777); //ip and port
            System.out.println("Connection Successful.");
                //Client to Server (read)
            br=new BufferedReader (new InputStreamReader(socket.getInputStream()));
                //Server to Client (write)
            out=new PrintWriter(socket.getOutputStream());


            CreateGUI();
            handleEvents();
            startReading();
            //startWriting();


        }catch(Exception e){
            //handle exception
        }
    }


    private void handleEvents(){

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key Released!!!!"+ e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("You have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }


            

        });
    }

    private void CreateGUI(){
        //gui code
        this.setTitle("Client Messenger");
        this.setSize(600,700); //width, height
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
           //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("logo-client.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER); //heading center haldeko
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20)); //top,left,bottom,right
        messageArea.setEditable(false); //message edit garna mildaina
        messageInput.setHorizontalAlignment(SwingConstants.CENTER); //textfield center


        //frame layout set doing
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

    }

    //start reading [Method]
    public void startReading(){
        //thread- read garirakhxa ani dinxa
        Runnable r1=()->{

            System.out.println("reader started...");

            try{
                while(true){
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                    System.out.println("Server left the chat");
                    JOptionPane.showMessageDialog(this,"Server Left the chat");
                    socket.close();
                    messageInput.setEnabled(false);
                    break;
                    }
                    //System.out.println("Server: "+msg);
                    messageArea.append("Server: "+msg+"\n");
                }
            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is Closed");
            }

        };

        new Thread(r1).start();
    }

    //start writing send [Method]
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