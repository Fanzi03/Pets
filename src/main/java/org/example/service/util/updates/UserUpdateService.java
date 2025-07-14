package org.example.service.util.updates;

public interface UserUpdateService <T> {
    public T update(T userData, Long id);
}
