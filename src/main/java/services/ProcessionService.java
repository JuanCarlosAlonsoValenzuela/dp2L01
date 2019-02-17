
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

import repositories.ProcessionRepository;
import domain.Brotherhood;
import domain.Coach;
import domain.Procession;
import domain.Request;

@Service
@Transactional
public class ProcessionService {

	// Managed repository ------------------------------------------

	@Autowired
	private ProcessionRepository	processionRepository;
	@Autowired
	private BrotherhoodService		brotherhoodService;


	// Simple CRUD methods ------------------------------------------

	public Procession createProcession() {

		//Asegurar que está logueado como Brotherhood
		//Asegurar que la Brotherhood logueada tiene un área
		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));

		final Procession procession = new Procession();

		final List<Coach> coachs = new ArrayList<>();
		procession.setCoachs(coachs);

		procession.setColumnNumber(0);
		procession.setDescription("");
		procession.setIsDraftMode(true);
		procession.setMoment(null);

		final List<Request> requests = new ArrayList<>();
		procession.setRequests(requests);

		procession.setRowNumber(0);

		final String ticker = this.generateTicker();
		procession.setTicker(ticker);

		procession.setTitle("");

		return procession;
	}

	public Procession editProcession(final Procession procession, final int columnNumber, final int rowNumber, final String description, final boolean isDraftMode, final String title, final Date moment) {

		//Security
		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		Assert.isTrue(procession.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getProcessions().contains(procession));

		final List<Procession> processions = loggedBrotherhood.getProcessions();
		processions.remove(procession);

		//procession.setCoachs(coachs);
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

	public void deleteProcession(final Procession procession) {

		//Security
		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		Assert.isTrue(procession.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getProcessions().contains(procession));

		//No debería tener Request porque está en Draft mode
		//Tampoco hay que preocuparse por el finder porque no se pueden buscar procesiones en Draft mode

		final List<Coach> coachs = new ArrayList<>();
		procession.setCoachs(coachs);

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
		final String date2 = LocalDate.now().toString();
		int count = 5;

		//Random String
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final StringBuilder builder = new StringBuilder();

		while (count-- != 0) {
			final int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		final String gen = builder.toString();

		final List<Procession> lc = this.processionRepository.findAll();
		final SimpleDateFormat df_in = new SimpleDateFormat("yyMMdd");
		final SimpleDateFormat df_output = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df_output.parse(date2);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		date1 = df_in.format(date);
		res = res + date1 + "-" + gen;
		for (final Procession c : lc)
			if (c.getTicker() == res)
				return this.generateTicker();
		return res;
	}

	public List<Procession> findAll() {
		return this.processionRepository.findAll();
	}

	public Procession findOne(final int id) {
		return this.processionRepository.findOne(id);
	}

	public Procession save(final Procession procession) {
		return this.processionRepository.save(procession);
	}
	public void delete(final Procession procession) {
		this.processionRepository.delete(procession);
	}

}
