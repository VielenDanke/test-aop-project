package kz.danke.test.project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSaveRequest {

    @Size(max = 128, min = 3, message = "Size of username is not valid, use from 3 to 128 symbols")
    private String username;
    @Size(max = 128, min = 3, message = "Size of password is not valid, use from 3 to 128 symbols")
    private String password;
    @Size(max = 128, min = 3, message = "Size of full name is not valid, use from 3 to 128 symbols")
    private String fullName;
}
