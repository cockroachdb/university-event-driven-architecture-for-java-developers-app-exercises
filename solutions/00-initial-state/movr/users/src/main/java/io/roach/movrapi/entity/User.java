package io.roach.movrapi.entity;

import javax.persistence.*;
import java.util.List;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * Hibernate entity for the User Table
 */

@Entity
@Table(name = "users")
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class User {

    @Id
    private String email;
    private String lastName;
    private String firstName;
    // Phone numbers are represented as plain strings and are not
    // being validated either by the front end or the back end.
    // JPA/Hibernate cannot handle string arrays directly, so we 
    // define them as a UserType using Hibernate Types
    @Type(type = "string-array")
    @Column(columnDefinition = "text[]")
    private String[] phoneNumbers;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
