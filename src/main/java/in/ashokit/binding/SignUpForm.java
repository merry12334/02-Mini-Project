package in.ashokit.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpForm {
	
	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email")
	private String email;
   
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Enter a valid 10-digit phone number")
    private String phno;
}
