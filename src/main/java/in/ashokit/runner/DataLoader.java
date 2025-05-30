package in.ashokit.runner;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.ashokit.entity.CourseEntity;
import in.ashokit.entity.EnqStatusEntity;
import in.ashokit.repo.CourseRepo;
import in.ashokit.repo.EnqStatusRepo;

@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo StatusRepo;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
	courseRepo.deleteAll();
	StatusRepo.deleteAll();
	
	    CourseEntity c1=new  CourseEntity();
	    c1.setCourseName("java");
	    
	    CourseEntity c2=new  CourseEntity();
	    c2.setCourseName("Python");
	    
	    
	    CourseEntity c3=new  CourseEntity();
	    c3.setCourseName(".Net");
	    
	    
	    CourseEntity c4=new  CourseEntity();
	    c4.setCourseName("Angular");
	    
	    
	    CourseEntity c5=new  CourseEntity();
	    c5.setCourseName("SpringBoot");
	   
	    courseRepo.saveAll(Arrays.asList(c1,c2,c3,c4,c5));
	    
	    EnqStatusEntity e1= new EnqStatusEntity();
	    e1.setStatusName("new");
	    
	    EnqStatusEntity e2= new EnqStatusEntity();
	    e2.setStatusName("Lost");
	    
	    EnqStatusEntity e3= new EnqStatusEntity();
	    e3.setStatusName("enrolled");
	   
	    StatusRepo.saveAll(Arrays.asList(e1,e2,e3));
	
	}

}
