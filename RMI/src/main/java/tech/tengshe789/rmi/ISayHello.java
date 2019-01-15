package tech.tengshe789.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISayHello extends Remote {
    public String satHello(String name) throws RemoteException;
}
