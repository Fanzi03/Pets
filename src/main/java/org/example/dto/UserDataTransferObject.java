package org.example.dto;

import java.util.List;

public class UserDataTransferObject {
    private String fullName;
    private String email;
    private int age;
    private List<PetDataTransferObject> pets;

    public UserDataTransferObject() {
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public int getAge() {
        return this.age;
    }

    public List<PetDataTransferObject> getPets() {
        return this.pets;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPets(List<PetDataTransferObject> pets) {
        this.pets = pets;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UserDataTransferObject)) return false;
        final UserDataTransferObject other = (UserDataTransferObject) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$fullName = this.getFullName();
        final Object other$fullName = other.getFullName();
        if (this$fullName == null ? other$fullName != null : !this$fullName.equals(other$fullName)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        if (this.getAge() != other.getAge()) return false;
        final Object this$pets = this.getPets();
        final Object other$pets = other.getPets();
        if (this$pets == null ? other$pets != null : !this$pets.equals(other$pets)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UserDataTransferObject;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fullName = this.getFullName();
        result = result * PRIME + ($fullName == null ? 43 : $fullName.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        result = result * PRIME + this.getAge();
        final Object $pets = this.getPets();
        result = result * PRIME + ($pets == null ? 43 : $pets.hashCode());
        return result;
    }

    public String toString() {
        return "UserDTO(fullName=" + this.getFullName() + ", email=" + this.getEmail() + ", age=" + this.getAge() + ", pets=" + this.getPets() + ")";
    }
}
