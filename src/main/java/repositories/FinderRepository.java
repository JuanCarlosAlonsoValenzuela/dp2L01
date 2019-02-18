
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Procession;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select p from Procession p where p.isDraftMode='FALSE'")
	public List<Procession> getPublushedProcessions();

	@Query("select p from Procession p where p.title like ?1 or p.description like ?1")
	public List<Procession> getProcessionsByKeyWord(String keyWord);

	@Query("select p from Brotherhood b join b.area a join b.processions p where a.name like ?1")
	public List<Procession> getProcessionsByArea(String area);

	@Query("select p from Procession p where (p.moment) between (select a.minDate from Finder a where a.id = ?1) and (select b.maxDate from Finder b where b.id = ?1) order by p.moment")
	public List<Procession> getProcessionsByDate(int id);
}
