package in.ashokit.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "AIT_STUDENT_ENQUIRIES")
public class StudentEnqEntity {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "ENQUIRY_ID")
	    private Integer enquiryId;

	    @Column(name = "STUDENT_NAME", length = 100)
	    private String studentName;

	    @Column(name = "PHNO")
	    private Long phno;

	    @Column(name = "CLASS_MODE", length = 50)
	    private String classMode;

	    @Column(name = "COURSE_NAME", length = 100)
	    private String courseName;

	    @Column(name = "ENQUIRY_STATUS", length = 50)
	    private String enquiryStatus;

	    @Column(name = "CREATED_DATE")
	    private LocalDate createdDate;

	    @Column(name = "UPDATED_DATE")
	    private LocalDate updatedDate;

	    // Foreign key mapping to AIT_USER_DTLS.user_id
	    @ManyToOne
	    @JoinColumn(name = "USER_ID", referencedColumnName = "user_id")
	    private UserDtlsEntity user;


}
