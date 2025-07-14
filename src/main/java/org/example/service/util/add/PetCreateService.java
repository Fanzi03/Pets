package org.example.service.util.add;

public interface PetCreateService <T>{
    public T add(T petData);
    public T addRandomPet();
}
