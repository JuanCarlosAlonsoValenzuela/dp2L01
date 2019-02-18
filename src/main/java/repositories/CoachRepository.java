
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Integer> {

}
