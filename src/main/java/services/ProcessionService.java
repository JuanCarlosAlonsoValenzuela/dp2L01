
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProcessionRepository;
import utilities.RandomString;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;
import domain.Request;
import forms.FormObjectProcessionFloat;

@Service
@Transactional
public class ProcessionService {

	// Managed repository ------------------------------------------

	@Autowired
	private ProcessionRepository	processionRepository;
	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private Validator				validator;


	// Simple CRUD methods ------------------------------------------

	public Procession create() {

		//Asegurar que está logueado como Brotherhood
		//Asegurar que la Brotherhood logueada tiene un área
		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));

		final Procession procession = new Procession();

		final List<Float> floats = new ArrayList<>();
		procession.setFloats(floats);

		procession.setColumnNumber(0);
		procession.setDescription("");
		procession.setIsDraftMode(true);
		procession.setMoment(null);

		List<Request> requests = new ArrayList<>();
		procession.setRequests(requests);

		procession.setRowNumber(0);

		String ticker = this.generateTicker();
		procession.setTicker(ticker);

		procession.setTitle("");

		return procession;
	}

	public Procession edit(Procession procession, int columnNumber, int rowNumber, String description, boolean isDraftMode, String title, Date moment) {

		//Security
		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		Assert.isTrue(procession.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getProcessions().contains(procession));

		List<Procession> processions = loggedBrotherhood.getProcessions();
		processions.remove(procession);

		//procession.setFloats(floats);
		procession.setColumnNumber(columnNumber);
		procession.setDescription(description);
		procession.setIsDraftMode(isDraftMode);
		procession.setMoment(moment);
		//procession.setRequests(requests);
		procession.setRowNumber(rowNumber);
		//procession.setTicker(ticker);

		procession.setTitle(title);

		final Procession saved = this.save(procession);
		processions.add(saved);
		loggedBrotherhood.setProcessions(processions);

		this.brotherhoodService.save(loggedBrotherhood);

		return saved;
	}

	public void deleteProcession(Procession procession) {

		//Security
		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		Assert.isTrue(procession.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getProcessions().contains(procession));

		//No debería tener Request porque está en Draft mode
		//Tampoco hay que preocuparse por el finder porque no se pueden buscar procesiones en Draft mode

		final List<Float> floats = new ArrayList<>();
		procession.setFloats(floats);

		final List<Procession> processions = loggedBrotherhood.getProcessions();
		processions.remove(procession);
		loggedBrotherhood.setProcessions(processions);
		this.brotherhoodService.save(loggedBrotherhood);

		this.processionRepository.delete(procession);
	}

	//Método auxiliar para generar el ticker-------------------------------
	private String generateTicker() {
		String res = "";
		Date date = null;
		String date1;
		String date2 = LocalDate.now().toString();
		String gen = new RandomString(6).nextString();
		List<Procession> lc = this.processionRepository.findAll();
		SimpleDateFormat df_in = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat df_output = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df_output.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date1 = df_in.format(date);
		res = res + date1 + "-" + gen;
		for (Procession c : lc)
			if (c.getTicker() == res)
				return this.generateTicker();
		return res;
	}

	public List<Procession> findAll() {
		return this.processionRepository.findAll();
	}

	public Procession findOne(int id) {
		return this.processionRepository.findOne(id);
	}

	public Procession save(Procession procession) {
		return this.processionRepository.save(procession);
	}
	public void delete(Procession procession) {
		this.processionRepository.delete(procession);
	}

	public Procession reconstruct(FormObjectProcessionFloat formObjectProcessionCoach, BindingResult binding) {
		Procession result = new Procession();

		result.setTitle(formObjectProcessionCoach.getTitleProcession());
		result.setDescription(formObjectProcessionCoach.getDescriptionProcession());
		result.setMoment(formObjectProcessionCoach.getMoment());
		result.setIsDraftMode(formObjectProcessionCoach.getIsDraftMode());
		result.setRowNumber(formObjectProcessionCoach.getRowNumber());
		result.setColumnNumber(formObjectProcessionCoach.getColumnNumber());

		result.setTicker(this.generateTicker());

		//		this.validator.validate(result, binding);

		return result;
	}

	public Procession saveAssign(Procession procession, domain.Float newFloat) {

		//procession.getFloats().add(newFloat);
		List<domain.Float> floats = new ArrayList<>();
		floats.add(newFloat);
		procession.setFloats(floats);
		Procession saved = new Procession();
		saved = this.processionRepository.save(procession);

		Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();

		brotherhood.getProcessions().add(saved);
		this.brotherhoodService.save(brotherhood);

		return saved;
	}

}
