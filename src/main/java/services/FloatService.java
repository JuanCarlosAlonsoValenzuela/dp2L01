
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FloatRepository;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

@Service
@Transactional
public class FloatService {

	@Autowired
	private FloatRepository		floatRepository;

	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private ProcessionService	processionService;
	@Autowired
	private Validator			validator;


	public Float reconstruct(Float floatt, BindingResult binding) {
		Float result = new Float();

		if (floatt.getId() == 0) {
			result = floatt;
			this.validator.validate(result, binding);
		} else {
			result = this.floatRepository.findOne(floatt.getId());
			result.setTitle(floatt.getTitle());
			result.setDescription(floatt.getDescription());

			result.setPictures(floatt.getPictures());

			this.validator.validate(result, binding);
		}
		return result;
	}
	public List<Float> showAssignedFloats(Procession procession) {
		List<Float> floatts = new ArrayList<Float>();
		floatts = procession.getFloats();
		return floatts;
	}

	public List<Float> showAllFloats() {
		List<Float> floatts = new ArrayList<Float>();
		floatts = this.floatRepository.findAll();
		return floatts;
	}

	public List<Float> showBrotherhoodFloats() {
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();
		List<Float> floatts = new ArrayList<Float>();
		floatts = bro.getFloats();
		return floatts;
	}

	public List<Float> findAll() {
		return this.floatRepository.findAll();
	}

	public Float findOne(final int id) {
		return this.floatRepository.findOne(id);
	}

	public void remove(final Float floatt) {
		//No se pueden eliminar pasos asignados a procesiones en final mode

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();
		List<Procession> pro = new ArrayList<Procession>();

		pro = this.brotherhoodService.getProcessionsByBrotherhood(bro);
		Assert.isTrue(this.allProcesionsDraftMode(pro));
		for (final Procession p : pro)
			if (p.getFloats().contains(floatt))
				p.getFloats().remove(floatt);
		bro.getFloats().remove(floatt);
		this.floatRepository.delete(floatt);
	}

	public Float save(final Float c) {

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood bro = new Brotherhood();
		Float floattSaved = new Float();
		bro = this.brotherhoodService.loggedBrotherhood();

		Assert.isTrue(!(bro.getArea().equals(null)));

		floattSaved = this.floatRepository.save(c);

		bro.getFloats().add(floattSaved);
		this.brotherhoodService.save(bro);
		return floattSaved;
	}

	public Float create() {
		final Float floatt = new Float();
		final List<String> pictures = new ArrayList<String>();

		floatt.setPictures(pictures);
		floatt.setTitle("");
		floatt.setDescription("");

		return floatt;
	}

	public Boolean allProcesionsDraftMode(final List<Procession> pro) {
		final Boolean res = true;
		for (final Procession p : pro)
			if (p.getIsDraftMode() == false)
				return true;
		return res;
	}

	public void AssingFloatToProcession(final Float floatt, final Procession procession) {
		Assert.isTrue(procession.getIsDraftMode() == true);
		if (!(procession.getFloats().contains(floatt)))
			procession.getFloats().add(floatt);
		this.processionService.save(procession);
	}

	public void UnAssingFloatToProcession(final Float floatt, final Procession procession) {
		Assert.isTrue(procession.getIsDraftMode() == true);
		if (procession.getFloats().contains(floatt))
			procession.getFloats().remove(floatt);
		this.processionService.save(procession);
	}

}
