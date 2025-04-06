package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIclient extends UnicastRemoteObject implements ViewInterface {
    final VirtualController server;
    public RMIclient(VirtualController server) throws RemoteException {
        super();
        this.server = server;
    }

    public void run() throws RemoteException {
        this.server.connect(this);
        this.runCli();
    }

    public void runCli() throws RemoteException {
        Scanner scan=new Scanner(System.in);
        while (true) {
            System.out.print(">  ");
            int command = scan.nextInt();
            //da adattare al comando in ingresso
        }

    }
    public void showUpdate () throws RemoteException {

    }

}
