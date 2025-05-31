package in.ashokit.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.binding.DashBoardResponse;
import in.ashokit.binding.EnquiryForm;
import in.ashokit.binding.EnquirySearchCriteria;
import in.ashokit.entity.CourseEntity;
import in.ashokit.entity.EnqStatusEntity;
import in.ashokit.entity.StudentEnqEntity;
import in.ashokit.entity.UserDtlsEntity;
import in.ashokit.repo.CourseRepo;
import in.ashokit.repo.EnqStatusRepo;
import in.ashokit.repo.StudentEnqRepo;
import in.ashokit.repo.UserDtlsRepo;
import jakarta.servlet.http.HttpSession;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private EnqStatusRepo statusRepo;

	@Autowired
	private CourseRepo courseRepo;

	@Autowired
	private StudentEnqRepo repo;

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private HttpSession session;

	@Override
	public List<String> getCourseName() {
		List<String> collect = courseRepo.findAll().stream().map(CourseEntity::getCourseName)
				.collect(Collectors.toList());
		return collect;
	}

	@Override
	public List<String> getEnqStatus() {
		List<String> collect = statusRepo.findAll().stream().map(EnqStatusEntity::getStatusName)
				.collect(Collectors.toList());
		return collect;
	}

	@Override
	public DashBoardResponse getDashBoardData(Integer userId) {

		DashBoardResponse response = new DashBoardResponse();

		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);

		if (findById.isPresent()) {
			UserDtlsEntity userentity = findById.get();
			List<StudentEnqEntity> enquiries = userentity.getEnquiries();
			Integer total = enquiries.size();

			Integer enrolled = (int) enquiries.stream()
					.filter(e -> e.getEnquiryStatus() != null && "ENROLLED".equalsIgnoreCase(e.getEnquiryStatus()))
					.count();

			Integer lost = (int) enquiries.stream()
					.filter(e -> e.getEnquiryStatus() != null && "LOST".equalsIgnoreCase(e.getEnquiryStatus())).count();

			response.setTotalEnq(total);
			response.setEnrolled(enrolled);
			response.setLost(lost);
		}

		return response;
	}

	@Override
	public boolean upsertEnquiry(EnquiryForm form) {
		StudentEnqEntity entity = new StudentEnqEntity();

		BeanUtils.copyProperties(form, entity);

		entity.setCreatedDate(LocalDate.now());
		entity.setUpdatedDate(LocalDate.now());
		entity.setPhno(Long.parseLong(form.getPhno()));

		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> userOpt = userDtlsRepo.findById(userId);

		if (userOpt.isPresent()) {
			entity.setUser(userOpt.get());
			repo.save(entity);
			return true;
		}

		return false;
	}

	@Override
	public List<StudentEnqEntity> getEnquiries() {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		if (findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			return enquiries;
		}
		return null;

	}

	@Override
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria criteria, Integer userId) {
	    Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
	    if (findById.isPresent()) {
	        UserDtlsEntity userDtlsEntity = findById.get();
	        List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();

	        if (criteria.getCourseName() != null &&
	            !criteria.getCourseName().trim().equalsIgnoreCase("-Select-") &&
	            !criteria.getCourseName().trim().isEmpty()) {

	            enquiries = enquiries.stream()
	                .filter(e -> e.getCourseName() != null &&
	                             e.getCourseName().trim().equalsIgnoreCase(criteria.getCourseName().trim()))
	                .collect(Collectors.toList());
	        }

	        if (criteria.getEnquiryStatus() != null &&
	            !criteria.getEnquiryStatus().trim().equalsIgnoreCase("-Select-") &&
	            !criteria.getEnquiryStatus().trim().isEmpty()) {

	            enquiries = enquiries.stream()
	                .filter(e -> e.getEnquiryStatus() != null &&
	                             e.getEnquiryStatus().trim().equalsIgnoreCase(criteria.getEnquiryStatus().trim()))
	                .collect(Collectors.toList());
	        }

	        if (criteria.getClassMode() != null &&
	            !criteria.getClassMode().trim().equalsIgnoreCase("-Select-") &&
	            !criteria.getClassMode().trim().isEmpty()) {

	            enquiries = enquiries.stream()
	                .filter(e -> e.getClassMode() != null &&
	                             e.getClassMode().trim().equalsIgnoreCase(criteria.getClassMode().trim()))
	                .collect(Collectors.toList());
	        }

	        return enquiries;
	    }

	    return Collections.emptyList();
	}



	@Override
	public EnquiryForm getEnquiry(Integer enqId) {
		// TODO Auto-generated method stub
		return null;
	}

}
