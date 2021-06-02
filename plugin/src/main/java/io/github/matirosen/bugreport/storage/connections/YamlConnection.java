package io.github.matirosen.bugreport.storage.connections;

import io.github.matirosen.bugreport.storage.DataConnection;

import java.sql.Connection;

public class YamlConnection implements DataConnection<Connection> {


    @Override
    public Connection getConnection(){
        return null;
    }

    @Override
    public void connect(){

    }

    @Override
    public void disconnect(){

    }
}
