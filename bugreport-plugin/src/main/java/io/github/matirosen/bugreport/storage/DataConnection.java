package io.github.matirosen.bugreport.storage;

public interface DataConnection<T> {
    T getConnection();
    void connect();
    void disconnect();
}
