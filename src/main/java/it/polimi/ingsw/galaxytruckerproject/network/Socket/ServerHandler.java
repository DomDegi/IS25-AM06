package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.ClientMessage;
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

    private VirtualController virtualController;

    private final ExecutorService messageProcesser = Executors.newSingleThreadExecutor();

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

    public void sendMessage(ClientMessage message){

    }
}
