package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.ClientMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.ServerMessage;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

public class ServerHandler implements Runnable {

    private final Socket server;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    private DisplayableView view;

    private VirtualController virtualController;

    private final ExecutorService messageProcesser = Executors.newSingleThreadExecutor();

    private final Queue<ServerMessage> receivedMessages = new PriorityQueue<>();

    public boolean isOn = true;

    public ServerHandler(Socket server, DisplayableView view, VirtualController virtualController) throws IOException {
        this.server = server;
        this.view = view;
        this.virtualController = virtualController;
    }

    @Override
    public void run() {
        ExecutorService connectionExecutor = Executors.newSingleThreadExecutor();
        try {
            CompletableFuture<?> connectionFuture = CompletableFuture.runAsync(() -> {
                try {
                    this.input = new ObjectInputStream(server.getInputStream());
                    this.output = new ObjectOutputStream(server.getOutputStream());
                    Thread messageReceiver = new Thread(this::receiveMessages, "message receiver");
                    messageReceiver.start();
                } catch (IOException e) {
                    System.out.println("Input and output streams could not be created");
                    throw new CompletionException(e);
                }
            });
            connectionFuture.get(3,TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Connection timed out");
            try{
                server.close();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("server handler could not run");
        }
    }

    public void sendClientMessage(ClientMessage clientMessage){
        try{
            output.reset();
            output.writeObject(clientMessage);
        } catch (IOException e){
            System.out.println("Could not send message to server");
        }
    }

    public void receiveMessages(){
        System.out.println("Receiving messages from server");
        while(isOn){
            ServerMessage serverMessage;
            try{
                serverMessage = (ServerMessage) input.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Could not read object from server, it wasn't a ServerMessage");
                return;
            } catch (IOException e) {
                System.out.println("Error handling inputStream from server");
                return;
            }
            synchronized (this){
                receivedMessages.add(serverMessage);
                messageProcesser.submit(() -> processMessage(receivedMessages));
            }
        }
    }

    public void processMessage(Queue<ServerMessage> queue){
        while(!queue.isEmpty()){
            ServerMessage serverMessage = queue.poll();
            try {
                serverMessage.processMessage(this);
            } catch (Exception e) {
                System.out.println("Could not process message from server");
            }
        }
    }

    public void stopReceivingMessages(){
        this.isOn = false;
        try{
            server.close();
        } catch (IOException e) {
            System.out.println("Could not close server");
        }
    }
}
