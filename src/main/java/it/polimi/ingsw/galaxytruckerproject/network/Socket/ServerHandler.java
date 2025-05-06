package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.ClientMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.ServerMessage;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

public class ServerHandler implements Runnable {

    private final Socket server;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    private DisplayableView view;

    private ClientController clientController;

    private VirtualController virtualController;

    private final ExecutorService messageProcesser = Executors.newSingleThreadExecutor();

    public boolean isOn = true;

    public boolean isReady = false;

    public ServerHandler(Socket server) throws IOException {
        this.server = server;
    }

    public void setView(DisplayableView view) {
        this.view = view;
    }

    public void setVirtualController(VirtualController virtualController) {
        this.virtualController = virtualController;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void run() {
        ExecutorService connectionExecutor = Executors.newSingleThreadExecutor();
        try {
            CompletableFuture<?> connectionFuture = CompletableFuture.runAsync(() -> {
                try {
                    this.output = new ObjectOutputStream(server.getOutputStream());
                    this.output.flush();
                    this.input = new ObjectInputStream(server.getInputStream());
                    //this.waitSetup();
                    //Thread messageReceiver = new Thread(this::receiveMessages, "message receiver");
                    //messageReceiver.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Input and output streams could not be created");
                    throw new CompletionException(e);
                }
            });
            connectionFuture.get(3,TimeUnit.SECONDS);

            this.waitSetup();
            Thread messageReceiver = new Thread(this::receiveMessages, "message receiver");
            messageReceiver.start();

        } catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println("Connection timed out");
            try{
                server.close();
            } catch (IOException e1) {
                e.printStackTrace();
                System.out.println(e1.getMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("server handler could not run");
        }
    }

    public synchronized void sendClientMessage(ClientMessage clientMessage){
        try{
            //output.reset();
            output.writeObject(clientMessage);
        } catch (IOException e){
            e.printStackTrace();
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
                e.printStackTrace();
                return;
            }
            synchronized (this){
                messageProcesser.submit(() -> processMessage(serverMessage));
            }
        }
    }

    public void processMessage(ServerMessage serverMessage){
            try {
                serverMessage.processMessage(this);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not process message from server");
            }
    }

    public void stopReceivingMessages(){
        this.isOn = false;
        try{
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not close server");
        }
    }

    public void waitSetup() {
        this.isReady = true;
        while(this.view == null || this.virtualController == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("waiting for setup");
            }
        }
    }
    public DisplayableView getView() {
        return this.view;
    }

    public ClientController getClientController() {
        return this.clientController;
    }

    public boolean isReady(){
        return this.isReady;
    }
}
