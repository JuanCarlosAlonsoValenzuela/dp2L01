
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AreaRepository;
import domain.Area;
import domain.Brotherhood;

@Service
@Transactional
public class AreaService {

	// Managed repository ------------------------------------------

	@Autowired
	private AreaRepository	areaRepository;


	// Simple CRUD methods ------------------------------------------

	public Area createArea() {

		Area area = new Area();

		return area;
	}

	public List<Area> findAll() {
		return this.areaRepository.findAll();
	}

	public Area findOne(int id) {
		return this.areaRepository.findOne(id);
	}

	public Area save(Area area) {
		return this.areaRepository.save(area);
	}
	public void delete(Area area) {
		this.areaRepository.delete(area);
	}

	// Other methods  ------------------------------------------------

	public Boolean hasArea(Brotherhood brotherhood) {
		try {
			Assert.notNull(brotherhood.getArea());
			return true;
		} catch (Throwable oops) {
			return false;
		}
	}
}
