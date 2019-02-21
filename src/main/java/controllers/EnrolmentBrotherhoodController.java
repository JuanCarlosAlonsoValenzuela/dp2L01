
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import domain.Enrolment;

@Controller
@RequestMapping("/enrolment/brotherhood")
public class EnrolmentBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;


	public EnrolmentBrotherhoodController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		List<Enrolment> enrolments = new ArrayList<Enrolment>();
		enrolments = this.brotherhoodService.getPengingEnrolments();

		result = new ModelAndView("enrolment/brotherhood/list");

		result.addObject("enrolments", enrolments);
		result.addObject("requestURI", "enrolment/brotherhood/list.do");
		return result;
	}

}
