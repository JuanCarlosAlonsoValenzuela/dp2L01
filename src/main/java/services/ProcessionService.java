
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ProcessionRepository;
import domain.Procession;

@Service
@Transactional
public class ProcessionService {

	// Managed repository ------------------------------------------

	@Autowired
	private ProcessionRepository	processionRepository;


	// Simple CRUD methods ------------------------------------------

	public Procession createProcession() {

		Procession procession = new Procession();

		return procession;
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

}
