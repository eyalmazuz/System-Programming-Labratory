package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class BidiConnections<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler> connections;
    private int idCounter;

    public BidiConnections() {
        this.connections=new ConcurrentHashMap<>();
        this.idCounter=0;
    }

    public Integer addConnection (ConnectionHandler connectionHandler)
    {
        this.idCounter++;
        this.connections.putIfAbsent(idCounter,connectionHandler);
        return this.idCounter;
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
