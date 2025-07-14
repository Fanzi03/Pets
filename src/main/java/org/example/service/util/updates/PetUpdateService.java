package org.example.service.util.updates;

public interface PetUpdateService <T>{
    public T update(Long id, T petData);
}
