package io.github.matirosen.bugreport.storage;

public interface Callback<T> {
    void call(T t);
}

