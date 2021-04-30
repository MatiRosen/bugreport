package io.github.matirosen.storage;

public interface DataConnection<T> {
    T getConnection();
    void disconnect();
}
