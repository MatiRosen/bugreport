package io.github.matirosen.storage.repositories;

import io.github.matirosen.storage.Callback;

import java.util.List;

public interface ObjectRepository<T,I> {
    T load(I id);
    List<T> loadAll();
    void save(T t);
    void delete(I id);

    void loadAsync(I id, Callback<T> callback);
}
