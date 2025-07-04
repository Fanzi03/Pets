package org.example.service.util.updates;

import org.example.dto.UserDataTransferObject;

public interface UserUpdateService {
    public UserDataTransferObject update(UserDataTransferObject userDataTransferObject, Long id);
}
