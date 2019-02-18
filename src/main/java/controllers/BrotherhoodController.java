
package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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
@RequestMapping("/brotherhood")
public class BrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private CoachService		coachService;


	public BrotherhoodController() {
		super();
	}

	//Lista de todos los coach de esa brotherhood
	@RequestMapping(value = "/coach/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		List<Coach> allCoachs = new ArrayList<Coach>();
		allCoachs = this.coachService.showAllCoachs();

		result = new ModelAndView("brotherhood/coach/list");

		result.addObject("allCoachs", allCoachs);
		result.addObject("requestURI", "brotherhood/coach/list.do");
		return result;
	}

	@RequestMapping(value = "/coach/create", method = RequestMethod.GET)
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

	@RequestMapping(value = "/coach/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final Coach coach, final BindingResult binding) {
		ModelAndView result;
		this.brotherhoodService.loggedAsBrotherhood();

		if (binding.hasErrors())
			result = this.createEditModelAndView(coach);
		else
			try {
				this.coachService.save(coach);
				result = new ModelAndView("redirect:brotherhood/coach/list");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(coach, "message.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/coach/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Coach coach, final BindingResult binding) {
		this.brotherhoodService.loggedAsBrotherhood();
		ModelAndView result;
		Brotherhood brother = new Brotherhood();
		brother = this.brotherhoodService.loggedBrotherhood();
		List<Coach> coachs = new ArrayList<Coach>();

		coachs = this.brotherhoodService.getCoachsByBrotherhood(brother);

		if (!(coachs.contains(coach)))
			return new ModelAndView("redirect:brotherhood/coach/list");

		try {
			this.coachService.remove(coach);
			result = new ModelAndView("redirect:brotherhood/coach/list");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(coach, "message.commit.error");

		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Coach coach) {
		ModelAndView result;

		result = this.createEditModelAndView(coach, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Coach coach, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("brotherhood/coach/create");

		result.addObject("coach", coach);
		result.addObject("message", messageCode);

		return result;
	}

}
