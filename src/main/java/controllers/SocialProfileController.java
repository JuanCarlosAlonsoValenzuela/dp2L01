
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AdminService;
import services.BrotherhoodService;
import services.MemberService;
import services.SocialProfileService;
import domain.Actor;
import domain.Admin;
import domain.Brotherhood;
import domain.Member;
import domain.SocialProfile;

@Controller
@RequestMapping("/authenticated")
public class SocialProfileController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private AdminService			adminService;
	@Autowired
	private MemberService			memberService;


	//-------------------------------------------------------------------
	//---------------------------LIST BROTHERHOOD------------------------------------
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Brotherhood broherhood = new Brotherhood();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor logguedActor = new Actor();
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();

		final List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();

		if (authorities.get(0).toString().equals("BROTHERHOOD")) {
			broherhood = this.brotherhoodService.loggedBrotherhood();
			socialProfiles = broherhood.getSocialProfiles();
		} else {

			logguedActor = this.actorService.getActorByUsername(userAccount.getUsername());

			socialProfiles = logguedActor.getSocialProfiles();
		}

		result = new ModelAndView("authenticated/showProfile");
		result.addObject("socialProfiles", socialProfiles);
		result.addObject("actor", logguedActor);
		result.addObject("broherhood", broherhood);
		result.addObject("requestURI", "authenticated/showProfile.do");

		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------CREATE BROTHERHOOD------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();
		result = this.createEditModelAndView(socialProfile);

		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------EDIT BROTHERHOOD--------------------------------------
	@RequestMapping(value = "/socialProfile/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int socialProfileId) {

		ModelAndView result;
		SocialProfile socialProfile;

		Actor logged = this.actorService.getActorByUsername(LoginService.getPrincipal().getUsername());

		List<SocialProfile> socialProfiles = logged.getSocialProfiles();

		socialProfile = this.socialProfileService.findOne(socialProfileId);
		Assert.notNull(socialProfile);
		result = this.createEditModelAndView(socialProfile);

		if (!(socialProfiles.contains(socialProfile)))
			result = this.list();
		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------SAVE --------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialProfile socialProfile, BindingResult binding) {
		ModelAndView result;
		Actor logguedActor = this.actorService.getActorByUsername(LoginService.getPrincipal().getUsername());

		socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(socialProfile);
		else
			try {

				SocialProfile saved = this.socialProfileService.save(socialProfile);
				List<SocialProfile> socialProfiles = logguedActor.getSocialProfiles();

				if (socialProfiles.contains(socialProfile)) {
					socialProfiles.remove(saved);
					socialProfiles.add(saved);
				} else
					socialProfiles.add(saved);

				logguedActor.setSocialProfiles(socialProfiles);

				this.actorService.save(logguedActor);

				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
			}
		return result;
	}
	//---------------------------------------------------------------------
	//---------------------------DELETE------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SocialProfile socialProfile, BindingResult binding) {

		ModelAndView result;

		socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);

		try {

			this.socialProfileService.deleteSocialProfile(socialProfile);
			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
		}
		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------EDIT PERSONAL DATA------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editPersonalData() {

		ModelAndView result;

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();

		if (authorities.get(0).toString().equals("ADMIN")) {
			Admin admin = this.adminService.loggedAdmin();
			Assert.notNull(admin);
			result = this.createEditModelAndView(admin);

		} else if (authorities.get(0).toString().equals("BROTHERHOOD")) {
			Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();
			Assert.notNull(brotherhood);
			result = this.createEditModelAndView(brotherhood);
		} else {
			Member member = this.memberService.loggedMember();
			Assert.notNull(member);
			result = this.createEditModelAndView(member);
		}

		if (result == null)
			result = this.list();
		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------SAVE PERSONAL DATA------------------------
	//Admin
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAdmin(Admin admin, BindingResult binding) {
		ModelAndView result;

		admin = this.adminService.reconstruct(admin, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(admin);
		else
			try {
				this.adminService.save(admin);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(admin, "socialProfile.commit.error");
			}
		return result;
	}

	//Member
	@RequestMapping(value = "/editMember", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(Member member, BindingResult binding) {
		ModelAndView result;

		member = this.memberService.reconstruct(member, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(member);
		else
			try {
				this.memberService.save(member);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(member, "socialProfile.commit.error");
			}
		return result;
	}

	//Brotherhood
	@RequestMapping(value = "/editBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(Brotherhood brotherhood, BindingResult binding) {
		ModelAndView result;

		brotherhood = this.brotherhoodService.reconstructBrotherhood(brotherhood, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(brotherhood);
		else
			try {
				this.brotherhoodService.save(brotherhood);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(brotherhood, "socialProfile.commit.error");
			}
		return result;
	}
	//---------------------------------------------------------------------
	//---------------------------CREATEEDITMODELANDVIEW--------------------

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile) {

		ModelAndView result;

		result = this.createEditModelAndView(socialProfile, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/socialProfile/create");
		result.addObject("socialProfile", socialProfile);
		result.addObject("message", messageCode);

		return result;
	}

	//---------------------------------------------------------------------
	//-------------------CREATEEDITMODELANDVIEW ACTOR----------------------

	//Admin
	protected ModelAndView createEditModelAndView(Admin admin) {

		ModelAndView result;

		result = this.createEditModelAndView(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Admin admin, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("admin", admin);
		result.addObject("message", messageCode);

		return result;
	}

	//Brotherhood
	protected ModelAndView createEditModelAndView(Brotherhood brotherhood) {

		ModelAndView result;

		result = this.createEditModelAndView(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Brotherhood brotherhood, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("brotherhood", brotherhood);
		result.addObject("message", messageCode);

		return result;
	}

	//Member
	protected ModelAndView createEditModelAndView(Member member) {

		ModelAndView result;

		result = this.createEditModelAndView(member, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Member member, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("member", member);
		result.addObject("message", messageCode);

		return result;
	}
}
