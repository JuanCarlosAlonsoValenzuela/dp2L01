
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Brotherhood;
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
	@Autowired
	private MemberService		memberService;
	@Autowired
	private ProcessionService	processionService;


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

	public Collection<Request> getRequestsByMemberAndStatus(Member member, Status status) {
		return this.requestRepository.getRequestsByMemberAndStatus(member, status);
	}

	public Collection<Request> getRequestsByBrotherhood(Brotherhood brotherhood) {
		return this.requestRepository.getRequestsByBrotherhood(brotherhood);
	}

	public Collection<Request> getRequestsByBrotherhoodAndStatus(Brotherhood brotherhood, Status status) {
		return this.requestRepository.getRequestsByBrotherhoodAndStatus(brotherhood, status);
	}

	public List<Request> getRequestsByProcessionAndStatus(Procession procession, Status status) {
		return this.requestRepository.getRequestsByProcessionAndStatus(procession, status);
	}

	public void deleteRequestAsMember(Member member, int requestId) {
		Request request = this.findOne(requestId);

		Assert.isTrue(this.getRequestsByMember(member).contains(request));
		Assert.isTrue(request.getStatus().equals(Status.PENDING));

		Procession procession = request.getProcession();
		List<Request> requests = procession.getRequests();
		requests.remove(request);
		procession.setRequests(requests);
		this.processionService.save(procession);

		List<Request> requests2 = member.getRequests();
		requests2.remove(request);
		member.setRequests(requests2);
		this.memberService.save(member);

		this.delete(request);

	}

	public void createRequestAsMember(Member member, int processionId) {
		Procession procession = this.processionService.findOne(processionId);
		List<Request> requests = procession.getRequests();

		Assert.isTrue(procession.getIsDraftMode() == false);
		for (Request r : requests)
			Assert.isTrue(!r.getMember().equals(member));

		Request newRequest = this.createRequest(member, procession);
		Request saveRequest = this.save(newRequest);

		List<Request> requests2 = member.getRequests();
		requests2.add(saveRequest);
		member.setRequests(requests2);
		this.memberService.save(member);

		List<Request> requests3 = procession.getRequests();
		requests3.add(saveRequest);
		procession.setRequests(requests3);
		this.processionService.save(procession);
	}
}
