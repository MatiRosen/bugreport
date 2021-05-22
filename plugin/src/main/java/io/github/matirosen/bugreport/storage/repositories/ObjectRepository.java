package io.github.matirosen.bugreport.storage.repositories;

import io.github.matirosen.bugreport.storage.Callback;

import java.util.List;

public interface ObjectRepository<T,I> {
    T load(I id);
    List<T> loadAll();
    void save(T t);
    void delete(I id);
    void start();

    void saveAsync(T id, Callback<T> callback);
    void loadAsync(I id, Callback<T> callback);
}
