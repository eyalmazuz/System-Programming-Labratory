package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler> connections;
    private int idCounter;

    public ConnectionsImpl() {
        this.connections=new ConcurrentHashMap<>();
        this.idCounter=0;
    }

    public Integer addConnection (ConnectionHandler connectionHandler)
    {
        this.idCounter++;
        this.connections.putIfAbsent(idCounter,connectionHandler);
        return this.idCounter;
    }

    public boolean isQueueEmpty(int connectionId){
        if (connections.get(connectionId) instanceof NonBlockingConnectionHandler)
               return  ((NonBlockingConnectionHandler)connections.get(connectionId)).isQueueEmpty();
        return true;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        try {
            connections.get(connectionId).send(msg);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void broadcast(T msg) {
        for (Integer clientId:connections.keySet())
            send(clientId,msg);
    }

    @Override
    public void disconnect(int connectionId) {
        ConnectionHandler handler=connections.remove(connectionId);
        try{
            handler.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
