package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

public class ApplicationUserUpdateDto {
    private Long id;
    @NotBlank
    @Size(max = 300)
    private String firstName;
    @NotBlank
    @Size(max = 300)
    private String lastName;
    @NotBlank
    @Email
    @Size(max = 300)
    private String email;
    @NotBlank
    private String gender;
    @NotNull
    private Date dateOfBirth;

    public ApplicationUserUpdateDto() {
    }

    public ApplicationUserUpdateDto(Long id, @NotBlank @Size(max = 300) String firstName, @NotBlank @Size(max = 300) String lastName, @NotBlank @Email @Size(max = 300) String email, @NotBlank String gender, @NotNull Date dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
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
}
