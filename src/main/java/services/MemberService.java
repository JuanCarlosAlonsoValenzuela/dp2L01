
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MemberRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Member;

@Service
@Transactional
public class MemberService {

	// Managed repository ------------------------------------------

	@Autowired
	private MemberRepository	memberRepository;


	// Simple CRUD methods ------------------------------------------

	public Member createMember() {

		Member member = new Member();

		return member;
	}

	public List<Member> findAll() {
		return this.memberRepository.findAll();
	}

	public Member findOne(int id) {
		return this.memberRepository.findOne(id);
	}

	public Member save(Member member) {
		return this.memberRepository.save(member);
	}
	public void delete(Member member) {
		this.memberRepository.delete(member);
	}

	// Other methods  ------------------------------------------------

	public Member getMemberByUsername(String username) {
		return this.memberRepository.getMemberByUsername(username);
	}

	// Auxiliar methods
	public Member securityAndMember() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();

		Member loggedMember = this.memberRepository.getMemberByUsername(username);
		List<Authority> authorities = (List<Authority>) loggedMember.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("MEMBER"));

		return loggedMember;
	}

}
