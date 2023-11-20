package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserPasswordDto {
    @Size(min = 8, max = 300)
    @NotBlank
    @Pattern(regexp = (".*[!@#$%^&*]"), message = "The password must contain a special character")
    String password;

    public String getPassword() {
        return password;
    }

}
