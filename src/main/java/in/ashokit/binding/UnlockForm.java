package in.ashokit.binding;

import lombok.Data;

@Data
public class UnlockForm {
	
	private String email;
	
  private String temppwd;
  
  private String newPwd;
  
  private String confPwd;

}
