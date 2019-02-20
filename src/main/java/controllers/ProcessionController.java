
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ProcessionService;
import domain.Brotherhood;
import domain.Procession;

@Controller
@RequestMapping("/procession/brotherhood")
public class ProcessionController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;
	@Autowired
	private BrotherhoodService	brotherhoodService;


	public ProcessionController() {
		super();
	}

	//-------------------------------------------------------------------
	//---------------------------LIST------------------------------------

	//Listar Processions
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		List<Procession> processions;

		this.brotherhoodService.loggedAsBrotherhood();

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Boolean hasArea = !(loggedBrotherhood.getArea() == null);

		processions = loggedBrotherhood.getProcessions();

		result = new ModelAndView("procession/brotherhood/list");
		result.addObject("processions", processions);
		result.addObject("requestURI", "procession/brotherhood/list.do");
		result.addObject("hasArea", hasArea);

		return result;
	}

	//-------------------------------------------------------------------
	//---------------------------CREATE----------------------------------

	//CREATE PROCESSION
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createProcession() {
		ModelAndView result;
		Procession procession;

		procession = this.processionService.create();
		result = this.createEditModelAndView(procession);

		return result;
	}

	//SAVE PROCESSION
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Procession procession, BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {
				this.processionService.save(procession);
				result = new ModelAndView("redirect:/procession/brotherhood/list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(procession, "brotherhood.commit.error");
			}
		return result;
	}

	//MODEL AND VIEW PROCESSION
	protected ModelAndView createEditModelAndView(Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Procession procession, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("procession/brotherhood/create");
		result.addObject("procession", procession);
		result.addObject("message", messageCode);

		return result;
	}

	//-------------------------------------------------------------------
	//---------------------------EDIT------------------------------------

	//Edit Procession
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int processionId) {
		ModelAndView result;
		Procession procession;

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		procession = this.processionService.findOne(processionId);
		Assert.notNull(procession);
		Assert.isTrue(procession.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getProcessions().contains(procession));

		result = this.createEditModelAndView1(procession);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveProcession(@Valid Procession procession, BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {
				this.processionService.save(procession);
				result = new ModelAndView("redirect:/procession/brotherhood/list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(procession, "procession.commit.error");
			}
		return result;
	}

	//MODEL AND VIEW WARRANTY
	protected ModelAndView createEditModelAndView1(Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView1(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView1(Procession procession, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("procession/brotherhood/edit");
		result.addObject("procession", procession);
		result.addObject("processionId", procession.getId());
		result.addObject("message", messageCode);

		return result;
	}

	//-------------------------------------------------------------------
	//---------------------------DELETE----------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Procession procession, BindingResult binding) {

		ModelAndView result;

		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		Assert.isTrue(procession.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getProcessions().contains(procession));

		try {
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:/procession/brotherhood/list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView1(procession, "procession.commit.error");
		}
		return result;
	}

}
