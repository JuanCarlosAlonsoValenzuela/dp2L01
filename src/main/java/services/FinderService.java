
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Service
@Transactional
public class FinderService {

	// Managed repository ------------------------------------------

	@Autowired
	private FinderRepository	finderRepository;

	// Supporting Services -----------------------------------------

	@Autowired
	private MemberService		memberRepository;
	@Autowired
	private ProcessionService	processionService;


	// Simple CRUD methods ------------------------------------------

	public Finder createFinder() {

		Finder finder = new Finder();

		List<Procession> processions = new ArrayList<>();

		Date maxDate = new Date();
		Date minDate = new Date();
		Date lastEdit = new Date();

		lastEdit.setTime(lastEdit.getTime() - 1);

		finder.setKeyWord("");
		finder.setArea("");
		finder.setMaxDate(maxDate);
		finder.setMinDate(minDate);
		finder.setLastEdit(lastEdit);
		finder.setProcessions(processions);

		return finder;

	}

	public List<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder findOne(int id) {
		return this.finderRepository.findOne(id);
	}

	public Finder save(Finder finder) {
		return this.finderRepository.save(finder);
	}
	public void delete(Finder finder) {
		this.finderRepository.delete(finder);
	}

	public void filterProcessionsByFinder() {
		UserAccount userAccount = LoginService.getPrincipal();

		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("MEMBER"));

		Member loggedMember = this.memberRepository.getMemberByUsername(userAccount.getUsername());

		Finder finder = loggedMember.getFinder();

		List<Procession> filter = new ArrayList<>();
		List<Procession> result = this.processionService.findAll();

		//KeyWord
		if (!finder.getKeyWord().equals(null) && !finder.getKeyWord().equals("")) {
			filter = this.finderRepository.getProcessionsByKeyWord(finder.getKeyWord());
			result.retainAll(filter);
		}
		//Area
		if (!finder.getArea().equals(null) && !finder.getArea().equals("")) {
			filter = this.finderRepository.getProcessionsByArea(finder.getArea());
			result.retainAll(filter);
		}
		//Dates
		if (finder.getMinDate() != null && finder.getMaxDate() != null) {
			Assert.isTrue(finder.getMinDate().before(finder.getMaxDate()));
			filter = this.finderRepository.getProcessionsByDate(finder.getId());
			result.removeAll(filter);
		}
		finder.setProcessions(result);
		Finder finderRes = this.finderRepository.save(finder);
		loggedMember.setFinder(finderRes);
		this.memberRepository.save(loggedMember);

	}
}