package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//TODO: replace this class with a correct ApplicationUser Entity implementation
@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    @Size(max = 300)
    private String firstName;
    @NotBlank
    @Size(max = 300)
    private String lastName;
    @Size(max = 300)
    private String email;
    @Size(min = 8, max = 300)
    @NotBlank
    private String password;
    @NotBlank
    private String gender;
    @NotNull
    private Date dateOfBirth;
    @NotNull
    private Boolean admin;
    private Boolean locked;
    private int loginFails;
    private boolean passwordReset;

    public ApplicationUser() {
    }

    public ApplicationUser(@NotBlank @Email @Size(max = 300) String email,
        @Size(min = 8, max = 300) @NotBlank String password, Boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public ApplicationUser(Long id, @NotBlank @Size(max = 300) String firstName,
                           @NotBlank @Size(max = 300) String lastName, @NotBlank @Email @Size(max = 300) String email,
                           @Size(min = 8, max = 300) @NotBlank String password, @NotBlank String gender,
                           @NotNull Date dateOfBirth, @NotNull Boolean admin, Boolean locked, int loginFails, boolean passwordReset) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.admin = admin;
        this.locked = locked;
        this.loginFails = loginFails;
        this.passwordReset = passwordReset;
    }

    public ApplicationUser(Long id, @NotBlank @Size(max = 300) String firstName, @NotBlank @Size(max = 300) String lastName, @NotBlank @Email @Size(max = 300) String email, @NotBlank String gender, @NotNull Date dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public ApplicationUser(@Size(min = 8, max = 300) @NotBlank String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public int getLoginFails() {
        return loginFails;
    }

    public void setLoginFails(int loginFails) {
        this.loginFails = loginFails;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", gender='" + gender + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", admin=" + admin +
            ", locked=" + locked +
            ", loginFails=" + loginFails +
            ", passwordReset=" + passwordReset +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser that = (ApplicationUser) o;
        return loginFails == that.loginFails
            && passwordReset == that.passwordReset
            && Objects.equals(firstName, that.firstName)
            && Objects.equals(lastName, that.lastName)
            && Objects.equals(email, that.email)
            && Objects.equals(password, that.password)
            && Objects.equals(gender, that.gender)
            && Objects.equals(dateOfBirth, that.dateOfBirth)
            && Objects.equals(admin, that.admin)
            && Objects.equals(locked, that.locked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, gender, dateOfBirth, admin, locked, loginFails, passwordReset);
    }
}
