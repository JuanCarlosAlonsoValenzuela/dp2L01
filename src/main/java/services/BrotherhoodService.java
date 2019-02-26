
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Box;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Float;
import domain.Member;
import domain.Procession;
import domain.SocialProfile;
import domain.StatusEnrolment;

@Service
@Transactional
public class BrotherhoodService {

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private Validator				validator;


	public List<Brotherhood> findAll() {
		return this.brotherhoodRepository.findAll();
	}

	public Brotherhood save(final Brotherhood h) {
		return this.brotherhoodRepository.save(h);
	}

	public void delete(final Brotherhood h) {
		this.brotherhoodRepository.delete(h);
	}

	public Brotherhood findOne(final int id) {
		return this.brotherhoodRepository.findOne(id);
	}

	public List<Float> getFloatsByBrotherhood(final Brotherhood b) {
		return this.brotherhoodRepository.getFloatsByBrotherhood(b.getId());
	}

	public List<Procession> getProcessionsByBrotherhood(final Brotherhood b) {
		return this.brotherhoodRepository.getProcessionsByBrotherhood(b.getId());
	}

	public List<Procession> getProcessionsByBrotherhoodFinal(final Brotherhood b) {
		return this.brotherhoodRepository.getProcessionsByBrotherhoodFinal(b.getId());
	}

	public Brotherhood create() {
		final Brotherhood bro = new Brotherhood();

		final List<String> pictures = new ArrayList<String>();
		final List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		final List<Enrolment> enrolments = new ArrayList<Enrolment>();
		final List<Box> boxes = new ArrayList<Box>();
		final List<Procession> processions = new ArrayList<Procession>();
		final List<Float> floats = new ArrayList<Float>();

		final UserAccount userAccount = new UserAccount();
		final List<Authority> authorities = new ArrayList<Authority>();
		userAccount.setUsername("");
		userAccount.setPassword("");

		final Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		final Date establishmentDate = new Date();
		establishmentDate.setTime(establishmentDate.getTime() - 1000);

		bro.setName("");
		bro.setSurname("");
		bro.setEstablishmentDate(establishmentDate);
		bro.setPictures(pictures);
		bro.setPolarity(0);
		bro.setHasSpam(false);
		bro.setPhoto(null);
		bro.setPhoneNumber("");
		bro.setAddress("");
		bro.setArea(null);
		bro.setBoxes(boxes);
		bro.setEmail("");
		bro.setEnrolments(enrolments);
		bro.setMiddleName("");
		bro.setSocialProfiles(socialProfiles);
		bro.setTitle("");
		bro.setUserAccount(userAccount);
		bro.setProcessions(processions);
		bro.setFloats(floats);

		return bro;
	}

	public Brotherhood saveCreate(final Brotherhood bro) {

		final List<Box> boxes = new ArrayList<>();
		final Box box1 = this.boxService.createSystem();
		box1.setName("Spam");
		final Box saved1 = this.boxService.saveSystem(box1);
		boxes.add(saved1);

		final Box box2 = this.boxService.createSystem();
		box2.setName("Trash");
		final Box saved2 = this.boxService.saveSystem(box2);
		boxes.add(saved2);

		final Box box3 = this.boxService.createSystem();
		box3.setName("Sent messages");
		final Box saved3 = this.boxService.saveSystem(box3);
		boxes.add(saved3);

		final Box box4 = this.boxService.createSystem();
		box4.setName("Notifications");
		final Box saved4 = this.boxService.saveSystem(box4);
		boxes.add(saved4);

		final Box box5 = this.boxService.createSystem();
		box5.setName("Received messages");
		final Box saved5 = this.boxService.saveSystem(box5);
		boxes.add(saved5);

		bro.setBoxes(boxes);

		return this.brotherhoodRepository.save(bro);
	}

	public void loggedAsBrotherhood() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("BROTHERHOOD"));
	}

	public Brotherhood loggedBrotherhood() {
		Brotherhood brother = new Brotherhood();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		brother = this.brotherhoodRepository.getBrotherhoodByUsername(userAccount.getUsername());
		return brother;
	}

	public Brotherhood securityAndBrotherhood() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();

		Brotherhood loggedBrotherhood = this.brotherhoodRepository.getBrotherhoodByUsername(username);
		List<Authority> authorities = (List<Authority>) loggedBrotherhood.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("BROTHERHOOD"));

		return loggedBrotherhood;
	}

	public Boolean hasArea(Brotherhood brotherhood) {
		try {
			Assert.notNull(brotherhood.getArea());
			return true;
		} catch (Throwable oops) {
			return false;
		}
	}

	public Brotherhood reconstructArea(Brotherhood brotherhood, BindingResult binding) {
		Brotherhood result;

		if (brotherhood.getId() == 0)
			result = brotherhood;
		else {
			result = this.brotherhoodRepository.findOne(brotherhood.getId());

			result.setArea(brotherhood.getArea());

			this.validator.validate(result, binding);
		}

		return result;
	}

	public Brotherhood updateBrotherhood(Brotherhood brotherhood) {
		this.loggedAsBrotherhood();
		Assert.isTrue(brotherhood.getId() != 0);
		return this.brotherhoodRepository.save(brotherhood);
	}

	public List<Member> getMembersOfBrotherhood() {
		Brotherhood bro = new Brotherhood();
		bro = this.loggedBrotherhood();
		List<Member> members = new ArrayList<Member>();
		List<Enrolment> enrolmentsBro = bro.getEnrolments();
		for (Enrolment e : enrolmentsBro)
			if (e.getStatusEnrolment() == StatusEnrolment.ACCEPTED)
				members.add(e.getMember());
		return members;
	}

	public Enrolment getEnrolment(Member m) {
		Enrolment en = null;
		Brotherhood bro = this.loggedBrotherhood();
		List<Enrolment> broEn = bro.getEnrolments();
		List<Enrolment> memEn = m.getEnrolments();
		broEn.retainAll(memEn);
		for (Enrolment e : broEn)
			if (e.getStatusEnrolment() == StatusEnrolment.ACCEPTED) {
				en = e;
				break;
			}
		return en;
	}

	public List<Enrolment> getPengingEnrolments() {
		Brotherhood bro = this.loggedBrotherhood();
		List<Enrolment> enrolments = bro.getEnrolments();
		Assert.notNull(bro);
		List<Enrolment> res = new ArrayList<Enrolment>();
		for (Enrolment e : enrolments)
			if (e.getStatusEnrolment() == StatusEnrolment.PENDING)
				res.add(e);
		return res;
	}

	public List<Member> getMembersOfBrotherhood(Brotherhood bro) {

		List<Member> members = new ArrayList<Member>();
		List<Enrolment> enrolmentsBro = bro.getEnrolments();
		for (Enrolment e : enrolmentsBro)
			if (e.getStatusEnrolment() == StatusEnrolment.ACCEPTED)
				members.add(e.getMember());
		return members;
	}
}
