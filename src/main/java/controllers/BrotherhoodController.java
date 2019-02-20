
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.CoachService;
import domain.Brotherhood;
import domain.Coach;

@Controller
@RequestMapping("/coach/brotherhood")
public class BrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private CoachService		coachService;


	//Lista de todos los coach de esa brotherhood
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Boolean hasArea = !(loggedBrotherhood.getArea() == null);

		List<Coach> allCoachs = new ArrayList<Coach>();
		allCoachs = this.coachService.showBrotherhoodCoachs();

		result = new ModelAndView("coach/brotherhood/list");

		result.addObject("allCoachs", allCoachs);
		result.addObject("requestURI", "coach/brotherhood/list.do");
		result.addObject("hasArea", hasArea);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();

		Assert.isTrue(bro.getArea() != null);
		ModelAndView result;
		this.brotherhoodService.loggedAsBrotherhood();
		Coach coach = new Coach();

		coach = this.coachService.create();

		result = this.createEditModelAndView(coach);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(Coach coach, BindingResult binding) {
		ModelAndView result;
		this.brotherhoodService.loggedAsBrotherhood();

		coach = this.coachService.reconstruct(coach, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(coach);
		else
			try {
				this.coachService.save(coach);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(coach, "message.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Coach coach, BindingResult binding) {
		this.brotherhoodService.loggedAsBrotherhood();
		ModelAndView result;
		Brotherhood brother = new Brotherhood();
		brother = this.brotherhoodService.loggedBrotherhood();
		List<Coach> coachs = new ArrayList<Coach>();

		coachs = this.brotherhoodService.getCoachsByBrotherhood(brother);

		if (!(coachs.contains(coach)))
			return new ModelAndView("redirect:list.do");

		try {
			this.coachService.remove(coach);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(coach, "message.commit.error");

		}
		return result;
	}

	protected ModelAndView createEditModelAndView(Coach coach) {
		ModelAndView result;

		result = this.createEditModelAndView(coach, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Coach coach, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("coach/brotherhood/create");

		result.addObject("coach", coach);
		result.addObject("message", messageCode);

		return result;
	}

}
