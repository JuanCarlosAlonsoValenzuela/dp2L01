
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.BrotherhoodRepository;
import domain.Brotherhood;
import domain.Coach;
import domain.Procession;

@Service
@Transactional
public class BrotherhoodService {

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;


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

}
