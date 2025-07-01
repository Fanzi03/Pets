package org.example.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CacheKeyGenerator { 

    static String USER_PREFIX = "user:";
    static String PET_PREFIX = "pet:";

    public static String userKey(Long id){
        if(id ==null) throw new IllegalArgumentException("User id cannot be null");
        return USER_PREFIX + id;
    }

    public static String petKey(Long id){
        if(id ==null) throw new IllegalArgumentException("Pet id cannot be null");
        return PET_PREFIX + id;
    }
    
    public static String userEmailKey(String email){
        if(email ==null || email.isEmpty()) {
            throw new IllegalArgumentException(
                "Email cannot be null or empty"
            );
        }

        return "user:email:" + email.toLowerCase();
    } 

    public static String usersPageKey(int page, int size){
        return "users:page:" + page + ":size:" + size;
    }

    public static String petsPageKey(int page, int size){
        return "pets:page:" + page + ":size:" + size;
    }

    public static String allPetsPagePattern(){
        return "pets:page:*";
    }
    public static String allUsersPagePattern(){
        return "users:page:*";
    }
    public static String allUsersPattern(){
        return USER_PREFIX + "*"; 
    }

    public static String allPetsPattern(){
        return PET_PREFIX + "*";
    }

    public static String allUserEmailPattern(){
        return "user:email:*";
    }

    public static String userPetsKey(Long userId){
        if(userId ==null) throw new IllegalArgumentException("User id cannot be null");
        return USER_PREFIX + userId + ":pets";
    }

    public static String petUserKey(Long petId){
        if(petId ==null) throw new IllegalArgumentException("Pet id cannot be null");
        return PET_PREFIX + petId + ":user";
    }


    public static String createKey(String prefix, Object... parts){
        if(prefix ==null || prefix.isEmpty()) throw new IllegalArgumentException("Prefix cannot be null");
        if(parts ==null || parts.length == 0) {
            return prefix.endsWith(":") ? prefix.substring(0, prefix.length() -1) : prefix;
        }
        StringBuilder sb = new StringBuilder(prefix);
        for(Object part:parts){
            if(part != null){
                sb.append(part.toString()).append(":");
            }
        }

        if(sb.length() > 0 && sb.charAt(sb.length() -1) == ':'){
            sb.setLength(sb.length() -1);
        }
        return sb.toString();
    }
}