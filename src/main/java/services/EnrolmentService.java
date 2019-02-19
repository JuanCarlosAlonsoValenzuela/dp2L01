
package services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.EnrolmentRepository;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.StatusEnrolment;

@Service
@Transactional
public class EnrolmentService {

	@Autowired
	private EnrolmentRepository	enrolmentRepository;

	@Autowired
	private MemberService		memberService;


	public List<Enrolment> findAll() {
		return this.enrolmentRepository.findAll();
	}

	public Enrolment findOne(Integer enrolmentId) {
		return this.enrolmentRepository.findOne(enrolmentId);
	}

	public Enrolment save(Enrolment e) {
		return this.enrolmentRepository.save(e);
	}

	public void delete(Enrolment e) {
		this.enrolmentRepository.delete(e);
	}

	public Enrolment create() {
		Enrolment e = new Enrolment();

		Date creationMoment = new Date();
		creationMoment.setTime(creationMoment.getTime() - 1);
		String position = null;
		StatusEnrolment statusEnrolment = StatusEnrolment.PENDING;
		Date dropOutDate = null;
		Member member = null;
		Brotherhood brotherhood = null;

		e.setCreationMoment(creationMoment);
		e.setPosition(position);
		e.setStatusEnrolment(statusEnrolment);
		e.setDropOutDate(dropOutDate);
		e.setMember(member);
		e.setBrotherhood(brotherhood);

		return e;

	}

	public Enrolment createEnrolment(Enrolment enrolment) {
		Member loggedMember = this.memberService.loggedMember();

		Enrolment enrolmentSaved = this.enrolmentRepository.save(enrolment);

		List<Enrolment> enrolments = loggedMember.getEnrolments();
		if (enrolments.contains(enrolment))
			enrolments.remove(enrolment);
		enrolments.add(enrolmentSaved);
		loggedMember.setEnrolments(enrolments);
		this.memberService.save(loggedMember);

		return enrolment;

	}

	public List<Enrolment> getEnrolmentsPerMember(Member m) {
		return m.getEnrolments();
	}

}
