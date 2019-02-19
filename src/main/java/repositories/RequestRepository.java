
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Member;
import domain.Request;
import domain.Status;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.member = ?1")
	public Collection<Request> getRequestsByMember(Member member);

	@Query("select r from Request r where r.member = ?1 and r.status = ?2")
	public Collection<Request> getRequestsByMemberAndStatus(Member member, Status status);

}
