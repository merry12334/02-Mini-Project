package in.ashokit.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.SignUpForm;
import in.ashokit.binding.UnlockForm;
import in.ashokit.entity.UserDtlsEntity;
import in.ashokit.repo.UserDtlsRepo;
import in.ashokit.utils.EmailUtils;
import in.ashokit.utils.PwdUtils;
import jakarta.servlet.http.HttpSession;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserDtlsRepo UserRepo;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private HttpSession session;

	@Override
	public boolean signup(SignUpForm form) {
		
		UserDtlsEntity email = UserRepo.findByEmail(form.getEmail());
		
		if(email!=null) {
			return false;
		}
		//TODO:copy data from binding object to entity object
		UserDtlsEntity entity= new UserDtlsEntity();
		BeanUtils.copyProperties(form, entity);
		
		//TODO:generate password set to the object
		String tempPwd = PwdUtils.generateRandomPwd();
		entity.setPwd(tempPwd);
		//TODO:set account status as unlock
		entity.setAccStatus("LOCKED");
		
		//TODO:insert the record
		UserRepo.save(entity);
		
		//TODO:send email to unlock the account
		String to=form.getEmail();
		String subject="Unlock Your Account";
		StringBuffer body=new StringBuffer("");  
		body.append("<h1>Use below temporary password to unlock your account</h1>");
		body.append("Temporary password : "+tempPwd);
		body.append("<br>");
		body.append("<a href=\"http://localhost:8081/unlock?email=" + to + "\">Click here to unlock your account</a>");

		emailUtils.sendEmail(to, subject, body.toString());
		
		
		return true;
	}
	

	@Override
	public String login(LoginForm form) {
		UserDtlsEntity entity = UserRepo.findByEmail(form.getEmail());
		if(entity==null) {
			return "invalid credential";
		}if("LOCKED".equals(entity.getAccStatus())) {
			return "Your Account is locked.Please unlock your account.";
		}if(!entity.getPwd().equals(form.getPwd())) {
			return "Password is Incorrect.";
		}
		//create session and store user data in session
		session.setAttribute("userId", entity.getUserId());
		
		return "success";
	}

	@Override
	public String unlockAccount(UnlockForm form) {
		UserDtlsEntity entity = UserRepo.findByEmail(form.getEmail());
		if(entity==null) {
			return "Invalid Email";
		}if(!entity.getPwd().equals(form.getTemppwd())) {
			return "Invalid Temporary password";
		}
		//if all okey then execute this part
		entity.setPwd(form.getNewPwd());
		entity.setAccStatus("unlocked");
		UserRepo.save(entity);
        		
		return "Your Account is Unlocked";
	}

	@Override
	public String forgotPwd(String email) {
	    UserDtlsEntity user = UserRepo.findByEmail(email);
	    if (user == null) {
	        return "Invalid email address.";
	    }

	    // For example purpose only: Send actual password (not secure in real apps)
	    String subject = "Your Password";
	    String body = "Your password is: " + user.getPwd();

	    boolean sent = emailUtils.sendEmail(email, subject, body);
	    return sent ? "Password sent to your email" : "Failed to send email";
	}

}
