package org.example.cache;

import java.util.List;

import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserCacheService implements UserService, CacheCheckService{

    @Qualifier("userServiceImpl")
    UserService userService;
    
    @Getter
    CacheService cacheService;
    
    @Override
    public Page<UserDataTransferObject> getUsers(Pageable pageable) {
        String cacheKey = CacheKeyGenerator.usersPageKey(pageable.getPageNumber(), pageable.getPageSize());
        return getFromCacheOrCompute(cacheKey, Page.class, () -> userService.getUsers(pageable), 3600); 
    }

    @Override
    public List<PetDataTransferObject> getUserPets(Long id) {
        String cacheKey = CacheKeyGenerator.userPetsKey(id);
        return getFromCacheOrCompute(cacheKey, List.class, () -> userService.getUserPets(id), 3600);
    }

    @Override
    public UserDataTransferObject findUserById(Long id) {
        String cacheKey = CacheKeyGenerator.userKey(id);
        return getFromCacheOrCompute(cacheKey, UserDataTransferObject.class, 
            () -> userService.findUserById(id), 3600);
    }

    @Override
    public UserDataTransferObject findUserByEmail(String email) {
        return getFromCacheOrCompute(CacheKeyGenerator.userEmailKey(email), 
            UserDataTransferObject.class, () -> userService.findUserByEmail(email));
    }

    @Override
    public UserDataTransferObject findUserByUserName(String userName){
        return getFromCacheOrCompute(CacheKeyGenerator.userNameKey(userName), UserDataTransferObject.class,
             () -> userService.findUserByUserName(userName));
    }

    @Override
    public void deleteById(Long id) {
        userService.deleteById(id);
        String cacheKey = CacheKeyGenerator.userKey(id);
        cacheService.evict(cacheKey);
        cacheService.evict(CacheKeyGenerator.userPetsKey(id));
        cacheService.evictedByPattern(CacheKeyGenerator.allUserEmailPattern());
        cacheService.evictedByPattern(CacheKeyGenerator.allUsersPattern());
        cacheService.evictedByPattern(CacheKeyGenerator.allUsersPagePattern());
        cacheService.evictedByPattern(CacheKeyGenerator.allUserNamePattern());
    }

    @Override
    public UserDataTransferObject update(UserDataTransferObject userDataTransferObject, Long id) {
        UserDataTransferObject updatedUser = userService.update(userDataTransferObject, id);
        String cacheKey = CacheKeyGenerator.userKey(id);
        cacheService.put(cacheKey, updatedUser, 3600);
        if((!userDataTransferObject.getEmail().equals(updatedUser.getEmail())) 
            && (updatedUser.getEmail() != null && (!updatedUser.getEmail().isEmpty())))
                cacheService.evictedByPattern(CacheKeyGenerator.allUserEmailPattern());
        return updatedUser;
    }

    @Override
    public UserDataTransferObject createUser(UserDataTransferObject userDataTransferObject) {
        UserDataTransferObject createdUser = userService.createUser(userDataTransferObject);
        cacheService.evictedByPattern(CacheKeyGenerator.allUsersPagePattern());
        return createdUser;
    } 
}
