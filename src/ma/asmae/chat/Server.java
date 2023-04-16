package ma.asmae.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
public class Server extends Thread{
    List<Conversation> conversations=new ArrayList<>();
    private List<String> clients=new ArrayList<>();

    public static void main(String[] args) {
        new Server().start();
    }

    @Override
    public void run() {
        System.out.println("The server is started using port 1234");
        try {
            ServerSocket ss=new ServerSocket(1234);
            System.out.println("Waiting connection");
            while(true){
                Socket socket=ss.accept();
                System.out.println("connecting...");
                Conversation conversation=new Conversation(socket);
                conversations.add(conversation);
                System.out.println("conver");
                conversation.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class Conversation extends Thread{
        private Socket socket;
        private String clientName;
        private int count=0;

        public  Conversation(Socket s){
            this.socket=s;
        }


        @Override
        public void run() {
            try {
                InputStream is=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                OutputStream os=socket.getOutputStream();
                PrintWriter pw=new PrintWriter(os,true);
                String ip=socket.getRemoteSocketAddress().toString();

                String request;

                while ((request= br.readLine())!=null){
                    if(count==0){
                        this.clientName=request;
                        clients.add(clientName);
                        System.out.println("New Client connection => "+request+" IP = "+ ip);
                        System.out.println("Server : Welcome, Your ID is => "+request);

                        String result = clients.stream()
                                .map(n -> String.valueOf(n))
                                .collect(Collectors.joining("-", "-", "-"));

                        broadcastMessage(result,new Conversation(new Socket()),clients);
                        count++;
                    }else{
                        List<String> clientsTo=new ArrayList<>();
                        String message;
                        if((request.contains("=>"))){
                            String[] items=request.split("=>");
                            System.out.println("item0 "+items[0]+" item1 "+items[1]);
                            String client=items[0];
                            message=items[1];
                            if(client.equals("All")){
                                clientsTo=clients;
                            }else{
                                clientsTo.add(client);
                            }
                            broadcastMessage(message, this, clientsTo);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message,Conversation from,List<String> clients){
        try {
            for(Conversation conversation:conversations){
                if(conversation!=from && clients.contains(conversation.clientName)){
                    Socket socket = conversation.socket;
                    OutputStream os=socket.getOutputStream();
                    PrintWriter pw=new PrintWriter(os,true);
                    System.out.println("From "+from.clientName);
                    pw.println(from.clientName+"=>"+message);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
