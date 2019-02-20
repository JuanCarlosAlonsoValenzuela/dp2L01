/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.MemberService;
import services.RequestService;
import domain.Member;
import domain.Request;
import domain.Status;

@Controller
@RequestMapping("/request/brotherhood/")
public class RequestBrotherhoodController extends AbstractController {

	@Autowired
	private RequestService		requestService;
	@Autowired
	private MemberService		memberService;
	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public RequestBrotherhoodController() {
		super();
	}

	// List ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView requestsList() {
		ModelAndView result;

		Member loggedMember = this.memberService.securityAndMember();
		Collection<Request> requests = this.requestService.getRequestsByMember(loggedMember);

		result = new ModelAndView("member/requests");

		result.addObject("requests", requests);
		result.addObject("requestURI", "request/member/list.do");

		return result;
	}

	@RequestMapping(value = "/filter", method = RequestMethod.POST, params = "refresh")
	public ModelAndView requestsFilter(@Valid String fselect) {
		ModelAndView result;

		if (fselect.equals("ALL"))
			result = new ModelAndView("redirect:list.do");
		else {

			Status status = Status.APPROVED;
			if (fselect.equals("PENDING"))
				status = Status.PENDING;
			else if (fselect.equals("REJECTED"))
				status = Status.REJECTED;

			Member loggedMember = this.memberService.securityAndMember();
			Collection<Request> requests = this.requestService.getRequestsByMemberAndStatus(loggedMember, status);

			result = new ModelAndView("member/requests");

			result.addObject("requests", requests);
			result.addObject("requestURI", "request/member/filter.do");
		}

		return result;
	}
}
