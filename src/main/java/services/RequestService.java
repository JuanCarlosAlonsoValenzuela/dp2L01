
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.RequestRepository;
import domain.Member;
import domain.Procession;
import domain.Request;
import domain.Status;

@Service
@Transactional
public class RequestService {

	// Managed repository ------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;


	//Simple CRUD methods ---------------------------------------------------------------------

	public Request createRequest(Member member, Procession procession) {
		Request res = new Request();

		res.setStatus(Status.PENDING);
		res.setColumnNumber(null);
		res.setRowNumber(null);
		res.setReasonDescription(null);

		res.setMember(member);
		res.setProcession(procession);

		return res;
	}

	// Simple CRUD methods ------------------------------------------

	public Collection<Request> findAll() {
		return this.requestRepository.findAll();
	}

	public Request findOne(int id) {
		return this.requestRepository.findOne(id);
	}

	public Request save(final Request request) {
		return this.requestRepository.save(request);
	}

	public void delete(Request request) {
		this.requestRepository.delete(request);
	}

	// Other methods
	public Collection<Request> getRequestsByMember(Member member) {
		return this.requestRepository.getRequestsByMember(member);
	}
}
