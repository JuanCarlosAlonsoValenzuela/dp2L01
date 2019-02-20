
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CoachRepository;
import domain.Brotherhood;
import domain.Coach;
import domain.Procession;

@Service
@Transactional
public class CoachService {

	@Autowired
	private CoachRepository		coachRepository;

	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private ProcessionService	processionService;
	@Autowired
	private Validator			validator;


	public Coach reconstruct(Coach coach, BindingResult binding) {
		Coach result = new Coach();

		if (coach.getId() == 0) {
			result = coach;
			this.validator.validate(result, binding);
		} else {
			result = this.coachRepository.findOne(coach.getId());
			result.setTitle(coach.getTitle());
			result.setDescription(coach.getDescription());

			result.setPictures(coach.getPictures());

			this.validator.validate(result, binding);
		}
		return result;
	}
	public List<Coach> showAssignedCoachs(Procession procession) {
		List<Coach> coachs = new ArrayList<Coach>();
		coachs = procession.getCoachs();
		return coachs;
	}

	public List<Coach> showAllCoachs() {
		List<Coach> coachs = new ArrayList<Coach>();
		coachs = this.coachRepository.findAll();
		return coachs;
	}

	public List<Coach> showBrotherhoodCoachs() {
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();
		List<Coach> coachs = new ArrayList<Coach>();
		coachs = bro.getCoachs();
		return coachs;
	}

	public List<Coach> findAll() {
		return this.coachRepository.findAll();
	}

	public Coach findOne(final int id) {
		return this.coachRepository.findOne(id);
	}

	public void remove(final Coach coach) {
		//No se pueden eliminar pasos asignados a procesiones en final mode

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();
		List<Procession> pro = new ArrayList<Procession>();

		pro = this.brotherhoodService.getProcessionsByBrotherhood(bro);
		Assert.isTrue(this.allProcesionsDraftMode(pro));
		for (final Procession p : pro)
			if (p.getCoachs().contains(coach))
				p.getCoachs().remove(coach);
		bro.getCoachs().remove(coach);
		this.coachRepository.delete(coach);
	}

	public Coach save(final Coach c) {

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood bro = new Brotherhood();
		Coach coachSaved = new Coach();
		bro = this.brotherhoodService.loggedBrotherhood();

		Assert.isTrue(!(bro.getArea().equals(null)));

		coachSaved = this.coachRepository.save(c);

		bro.getCoachs().add(coachSaved);
		this.brotherhoodService.save(bro);
		return coachSaved;
	}

	public Coach create() {
		final Coach coach = new Coach();
		final List<String> pictures = new ArrayList<String>();

		coach.setPictures(pictures);
		coach.setTitle("");
		coach.setDescription("");

		return coach;
	}

	public Boolean allProcesionsDraftMode(final List<Procession> pro) {
		final Boolean res = true;
		for (final Procession p : pro)
			if (p.getIsDraftMode() == false)
				return true;
		return res;
	}

	public void AssingCoachToProcession(final Coach coach, final Procession procession) {
		Assert.isTrue(procession.getIsDraftMode() == true);
		if (!(procession.getCoachs().contains(coach)))
			procession.getCoachs().add(coach);
		this.processionService.save(procession);
	}

	public void UnAssingCoachToProcession(final Coach coach, final Procession procession) {
		Assert.isTrue(procession.getIsDraftMode() == true);
		if (procession.getCoachs().contains(coach))
			procession.getCoachs().remove(coach);
		this.processionService.save(procession);
	}

}
