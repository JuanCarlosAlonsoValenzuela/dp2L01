/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

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

import services.AreaService;
import services.BrotherhoodService;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping("/area")
public class AreaController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private AreaService			areaService;


	// Constructors -----------------------------------------------------------

	public AreaController() {
		super();
	}

	// Action-1 ---------------------------------------------------------------		

	@RequestMapping(value = "/brotherhood/showArea", method = RequestMethod.GET)
	public ModelAndView showArea() {
		ModelAndView result;
		Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();
		Area a = brotherhood.getArea();
		List<Area> area = new ArrayList<Area>();
		area.add(a);
		Boolean hasArea = false;
		try {
			Assert.notNull(brotherhood.getArea());
			hasArea = true;
		} catch (Throwable oops) {

		}

		result = new ModelAndView("area/brotherhood/showArea");
		result.addObject("area", area);
		result.addObject("hasArea", hasArea);

		return result;
	}

	@RequestMapping(value = "/brotherhood/selectArea", method = RequestMethod.GET)
	public ModelAndView selectArea() {
		ModelAndView result;
		Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();
		List<Area> areas = this.areaService.findAll();

		result = new ModelAndView("area/brotherhood/selectArea");
		result.addObject("brotherhood", brotherhood);
		result.addObject("areas", areas);

		return result;
	}

	@RequestMapping(value = "/brotherhood/showPictures", method = RequestMethod.GET)
	public ModelAndView showPictures(@RequestParam int areaId) {
		ModelAndView result;
		Area area = this.areaService.findOne(areaId);

		result = new ModelAndView("area/brotherhood/showPictures");
		result.addObject("area", area);
		result.addObject("pictures", area.getPictures());
		result.addObject("requestURI", "area/brotherhood/showPictures.do");

		return result;
	}

	@RequestMapping(value = "/brotherhood/selectArea", method = RequestMethod.POST, params = "edit")
	public ModelAndView selectArea(Brotherhood brotherhood, BindingResult binding) {
		ModelAndView result;
		Brotherhood bro;
		bro = this.brotherhoodService.reconstructArea(brotherhood, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewB(bro);
		else
			try {
				this.brotherhoodService.save(bro);
				result = new ModelAndView("redirect:showArea.do");

			} catch (Throwable oops) {
				result = this.createEditModelAndViewB(bro, "area.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndView(Area area) {
		ModelAndView result;

		result = this.createEditModelAndView(area, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Area area, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("area/admin/edit");
		result.addObject("area", area);

		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndViewB(Brotherhood brotherhood) {
		ModelAndView result;

		result = this.createEditModelAndViewB(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewB(Brotherhood brotherhood, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("area/brotherhood/selectArea");
		result.addObject("brotherhood", brotherhood);

		result.addObject("message", messageCode);

		return result;
	}

}
