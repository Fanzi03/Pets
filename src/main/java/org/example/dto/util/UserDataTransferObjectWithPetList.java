package org.example.dto.util;

import java.util.ArrayList;
import java.util.List;

import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserDataTransferObjectWithPetList extends UserDataTransferObject {
    private final List<PetDataTransferObject> pets = new ArrayList<>();
}

