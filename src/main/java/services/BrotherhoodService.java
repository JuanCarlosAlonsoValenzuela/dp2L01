
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Box;
import domain.Brotherhood;
import domain.Coach;
import domain.Enrolment;
import domain.Procession;
import domain.SocialProfile;

@Service
@Transactional
public class BrotherhoodService {

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	@Autowired
	private BoxService				boxService;


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

	public List<Coach> getCoachsByBrotherhood(final Brotherhood b) {
		return this.brotherhoodRepository.getCoachsByBrotherhood(b.getId());
	}

	public List<Procession> getProcessionsByBrotherhood(final Brotherhood b) {
		return this.brotherhoodRepository.getProcessionsByBrotherhood(b.getId());
	}

	public Brotherhood create() {
		final Brotherhood bro = new Brotherhood();

		final List<String> pictures = new ArrayList<String>();
		final List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		final List<Enrolment> enrolments = new ArrayList<Enrolment>();
		final List<Box> boxes = new ArrayList<Box>();
		final List<Procession> processions = new ArrayList<Procession>();
		final List<Coach> coachs = new ArrayList<Coach>();

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
		bro.setCoachs(coachs);

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
		brother = this.brotherhoodRepository.getBrotherhoodByUserName(userAccount.getUsername());
		return brother;
	}

}
