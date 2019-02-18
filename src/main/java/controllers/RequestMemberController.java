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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.MemberService;
import services.RequestService;
import domain.Member;
import domain.Request;

@Controller
@RequestMapping("/request/member/")
public class RequestMemberController extends AbstractController {

	@Autowired
	private RequestService	requestService;
	@Autowired
	private MemberService	memberService;


	// Constructors -----------------------------------------------------------

	public RequestMemberController() {
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
}
