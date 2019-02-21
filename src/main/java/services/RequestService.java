
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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
	@Autowired
	private Validator			validator;
	@Autowired
	private BrotherhoodService	brotherhoodService;


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
	
	public List<Request> getRequestsByProcessionAndStatus(Procession procession, Status status) {
		return this.requestRepository.getRequestsByProcessionAndStatus(procession, status);
	}

	public Collection<Request> getRequestsByBrotherhoodAndStatus(Brotherhood brotherhood, Status status) {
		return this.requestRepository.getRequestsByBrotherhoodAndStatus(brotherhood, status);
	}

	public Request getRequestByBrotherhoodAndRequestId(Brotherhood brotherhood, Request request) {
		return this.requestRepository.getRequestByBrotherhoodAndRequestId(brotherhood, request);
	}

	public Collection<Request> getRequestApprovedByBrotherhoodAndProcession(Brotherhood brotherhood, Procession procession) {
		return this.requestRepository.getRequestApprovedByBrotherhoodAndProcession(brotherhood, procession);
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

	public boolean canRequest(Member member, int processionId) {
		boolean res = true;

		Procession procession = this.processionService.findOne(processionId);
		List<Request> requests = procession.getRequests();

		if (procession.getIsDraftMode() == true)
			res = false;
		if (res == true)
			for (Request r : requests)
				if (r.getMember().equals(member)) {
					res = false;
					break;
				}

		return res;
	}

	public Request saveRequestWithPreviousChecking(Request request) {
		Request requestSaved;

		if (request.getStatus().equals(Status.APPROVED)) {
			Assert.notNull(request.getColumnNumber());
			Assert.notNull(request.getRowNumber());

			Procession procession = request.getProcession();
			Collection<Request> requests = procession.getRequests();

			Integer col = request.getColumnNumber();
			Integer row = request.getRowNumber();

			Boolean isFree = true;
			for (Request req : requests)
				if (req.getColumnNumber() == col && req.getRowNumber() == row) {
					isFree = false;
					break;
				}

			Boolean respectMax = col <= procession.getColumnNumber() && row <= procession.getRowNumber();

			Assert.isTrue(isFree && respectMax);

		} else if (request.getStatus().equals(Status.REJECTED)) {
			Assert.notNull(request.getReasonDescription());
			Assert.isTrue(!request.getReasonDescription().trim().equals(""));
		}

		requestSaved = this.save(request);
		return requestSaved;
	}
	public Request reconstructRequestDecide(Request request, BindingResult binding) {
		this.brotherhoodService.securityAndBrotherhood();

		Request result = this.requestRepository.findOne(request.getId());

		Request result2 = new Request();

		result2.setId(result.getId());
		result2.setMember(result.getMember());
		result2.setProcession(result.getProcession());
		result2.setVersion(result.getVersion());

		if (request.getStatus().equals(Status.APPROVED)) {
			Integer col = request.getColumnNumber();
			Integer row = request.getRowNumber();

			result2.setColumnNumber(col);
			result2.setRowNumber(row);
			result2.setReasonDescription(null);
			result2.setStatus(request.getStatus());
		} else if (request.getStatus().equals(Status.REJECTED)) {
			result2.setColumnNumber(null);
			result2.setRowNumber(null);
			result2.setReasonDescription(request.getReasonDescription());
			result2.setStatus(request.getStatus());
		} else {
			result2.setColumnNumber(null);
			result2.setRowNumber(null);
			result2.setReasonDescription(null);
			result2.setStatus(request.getStatus());
		}

		this.validator.validate(result, binding);
		return result2;
	}

}
