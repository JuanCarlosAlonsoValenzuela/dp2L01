
package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ConfigurationService;
import services.FinderService;
import services.MemberService;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Controller
@RequestMapping("/finder/member/")
public class FinderMemberController extends AbstractController {

	@Autowired
	private FinderService			finderService;
	@Autowired
	private MemberService			memberService;
	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public FinderMemberController() {
		super();
	}

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView processionsList() {
		ModelAndView result;

		UserAccount userAccount = LoginService.getPrincipal();
		Member member = this.memberService.getMemberByUsername(userAccount.getUsername());

		Finder finder = member.getFinder();

		//Current Date
		Date currentDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		Integer currentDay = calendar.get(Calendar.DATE);
		Integer currentMonth = calendar.get(Calendar.MONTH);
		Integer currentYear = calendar.get(Calendar.YEAR);
		Integer currentHour = calendar.get(Calendar.HOUR);

		//LastEdit Finder
		Date lasEdit = finder.getLastEdit();
		calendar.setTime(lasEdit);
		Integer lastEditDay = calendar.get(Calendar.DATE);
		Integer lastEditMonth = calendar.get(Calendar.MONTH);
		Integer lastEditYear = calendar.get(Calendar.YEAR);
		Integer lastEditHour = calendar.get(Calendar.HOUR);

		Integer time = this.configurationService.getConfiguration().getTimeFinder();

		List<Procession> processions = new ArrayList<>();
		List<Procession> finderProcessions = finder.getProcessions();

		if (currentDay.equals(lastEditDay) && currentMonth.equals(lastEditMonth) && currentYear.equals(lastEditYear) && lastEditHour < (currentHour + time)) {
			Integer numFinderResult = this.configurationService.getConfiguration().getFinderResult();

			if (finderProcessions.size() > numFinderResult)
				for (int i = 0; i < numFinderResult; i++)
					processions.add(finderProcessions.get(i));
			else
				processions = finderProcessions;
		}

		result = new ModelAndView("member/finderResult");

		result.addObject("processions", processions);

		return result;
	}
}
