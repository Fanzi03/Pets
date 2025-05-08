package org.example.dto;

import org.example.entity.Gender;

public class PetDataTransferObject {
    private String name;
    private String type;
    private Gender gender;
    private int age;
    private String fullName;

    public PetDataTransferObject() {
    }


    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public Gender getGender() {
        return this.gender;
    }

    public int getAge() {
        return this.age;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PetDataTransferObject)) return false;
        final PetDataTransferObject other = (PetDataTransferObject) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$gender = this.getGender();
        final Object other$gender = other.getGender();
        if (this$gender == null ? other$gender != null : !this$gender.equals(other$gender)) return false;
        if (this.getAge() != other.getAge()) return false;
        final Object this$fullName = this.getFullName();
        final Object other$fullName = other.getFullName();
        if (this$fullName == null ? other$fullName != null : !this$fullName.equals(other$fullName)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PetDataTransferObject;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $gender = this.getGender();
        result = result * PRIME + ($gender == null ? 43 : $gender.hashCode());
        result = result * PRIME + this.getAge();
        final Object $fullName = this.getFullName();
        result = result * PRIME + ($fullName == null ? 43 : $fullName.hashCode());
        return result;
    }

    public String toString() {
        return "PetDataTransferObject(name=" + this.getName() + ", type=" + this.getType() + ", gender=" + this.getGender() + ", age=" + this.getAge() + ", fullName=" + this.getFullName() + ")";
    }
}
