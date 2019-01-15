package tech.tengshe789.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SayHelloImpl extends UnicastRemoteObject implements ISayHello{

    protected SayHelloImpl() throws RemoteException {
    }

    public String satHello(String name) throws RemoteException {
        return "hello," + name;
    }
}
