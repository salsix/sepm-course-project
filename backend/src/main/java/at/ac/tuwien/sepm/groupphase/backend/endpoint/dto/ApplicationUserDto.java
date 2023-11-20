package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.*;
import java.sql.Date;

public class ApplicationUserDto {
    private Long id;
    @NotBlank
    @Size(max = 300)
    private String firstName;
    @NotBlank
    @Size(max = 300)
    private String lastName;
    @NotBlank
    @Pattern(regexp = ("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
        "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])" +
        "|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:" +
        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"),
        message = "has not a valid format")
    @Size(max = 300)
    private String email;
    @Size(min = 8, max = 300)
    @NotBlank
    @Pattern(regexp = (".*[!@#$%^&*]"), message = "The password must contain a special character")
    private String password;
    @NotBlank
    private String gender;
    @NotNull
    private Date dateOfBirth;
    @NotNull(message = "User Role can not be empty")
    private Boolean admin;
    private Boolean locked;
    private int loginFails;
    private boolean passwordReset;


    public ApplicationUserDto() {
    }

    public ApplicationUserDto(Long id,
                              @NotBlank @Size(max = 300) String firstName,
                              @NotBlank @Size(max = 300) String lastName,
                              @NotBlank @Email @Size(max = 300) String email,
                              @Size(min = 8, max = 300)
                              @NotBlank @Pattern(regexp = (".*[!@#$%^&*]"),
                                  message = "The password must contain a special character")
                                  String password, @NotBlank String gender,
                              @NotNull Date dateOfBirth,
                              @NotNull Boolean admin,
                              Boolean locked,
                              int loginFails,
                              boolean passwordReset) {
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

    @Override
    public String toString() {
        return "ApplicationUserDto{" +
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
}
