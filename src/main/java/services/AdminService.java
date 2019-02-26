
package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Admin;
import domain.Area;
import domain.Box;
import domain.Brotherhood;
import domain.Member;
import domain.Message;
import domain.Position;
import domain.Procession;
import domain.SocialProfile;

@Service
@Transactional
public class AdminService {

	@Autowired
	private AdminRepository		adminRepository;

	@Autowired
	private MessageService		messageService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BoxService			boxService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private Validator			validator;


	// 1. Create user accounts for new administrators.
	public void loggedAsAdmin() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("ADMIN"));
	}

	public Admin createAdmin() {
		//SE DECLARA EL ADMIN
		final Admin admin = new Admin();

		//SE CREAN LAS LISTAS VACÍAS
		final List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		final List<Box> boxes = new ArrayList<Box>();

		//SE AÑADE EL USERNAME Y EL PASSWORD
		final UserAccount userAccountAdmin = new UserAccount();
		userAccountAdmin.setUsername("");
		userAccountAdmin.setPassword("");

		//SE AÑADEN LOS ATRIBUTOS
		admin.setName("");
		admin.setMiddleName("");
		admin.setSurname("");
		admin.setPhoto("");
		admin.setEmail("");
		admin.setPhoneNumber("");
		admin.setAddress("");
		admin.setSocialProfiles(socialProfiles);
		admin.setBoxes(boxes);
		//SPAM A FALSE
		admin.setHasSpam(false);
		admin.setPolarity(0);

		final List<Authority> authorities = new ArrayList<Authority>();

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);
		userAccountAdmin.setAuthorities(authorities);
		userAccountAdmin.setIsNotLocked(true);
		admin.setUserAccount(userAccountAdmin);

		return admin;
	}

	public Admin createAdmin(final String name, final String middleName, final String surname, final String photo, final String email, final String phoneNumber, final String address, final String userName, final String password) {

		final Admin admin = new Admin();

		final List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		final List<Box> boxes = new ArrayList<Box>();

		final UserAccount userAccountAdmin = new UserAccount();

		userAccountAdmin.setUsername(userName);
		userAccountAdmin.setPassword(password);

		final Box spamBox = new Box();
		final List<Message> messages1 = new ArrayList<>();
		spamBox.setIsSystem(true);
		spamBox.setMessages(messages1);
		spamBox.setName("Spam");

		final Box trashBox = new Box();
		final List<Message> messages2 = new ArrayList<>();
		trashBox.setIsSystem(true);
		trashBox.setMessages(messages2);
		trashBox.setName("Trash");

		final Box sentBox = new Box();
		final List<Message> messages3 = new ArrayList<>();
		sentBox.setIsSystem(true);
		sentBox.setMessages(messages3);
		sentBox.setName("Sent messages");

		final Box receivedBox = new Box();
		final List<Message> messages4 = new ArrayList<>();
		receivedBox.setIsSystem(true);
		receivedBox.setMessages(messages4);
		receivedBox.setName("Received messages");

		boxes.add(receivedBox);
		boxes.add(sentBox);
		boxes.add(spamBox);
		boxes.add(trashBox);

		admin.setName(name);
		admin.setMiddleName(middleName);
		admin.setSurname(surname);
		admin.setPhoto(photo);
		admin.setEmail(email);
		admin.setPhoneNumber(phoneNumber);
		admin.setAddress(address);
		admin.setSocialProfiles(socialProfiles);
		admin.setBoxes(boxes);
		admin.setHasSpam(false);

		final List<Authority> authorities = new ArrayList<Authority>();

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);
		userAccountAdmin.setAuthorities(authorities);
		userAccountAdmin.setIsNotLocked(true);
		admin.setUserAccount(userAccountAdmin);

		return admin;
	}

	public Admin saveCreate(final Admin admin) {	//Tenemos un listBox vacía
		this.loggedAsAdmin();
		final List<Box> boxes = new ArrayList<>();

		//Boxes
		final Box box1 = this.boxService.createSystem();
		box1.setName("Spam");
		final Box saved1 = this.boxService.saveSystem(box1);
		boxes.add(saved1);

		final Box box2 = this.boxService.createSystem();
		box2.setName("Trash");
		final Box saved2 = this.boxService.saveSystem(box2);
		boxes.add(saved2);

		final Box box3 = this.boxService.createSystem();
		box3.setName("Sent messages");
		final Box saved3 = this.boxService.saveSystem(box3);
		boxes.add(saved3);

		final Box box4 = this.boxService.createSystem();
		box4.setName("Received messages");
		final Box saved4 = this.boxService.saveSystem(box4);
		boxes.add(saved4);

		admin.setBoxes(boxes);

		return this.adminRepository.save(admin);
	}

	public Admin save(final Admin admin) {
		return this.adminRepository.save(admin);
	}

	// SERVICIO 1
	/*
	 * public Map<String, Double[]> computeStatistics() {
	 * this.loggedAsAdmin();
	 * 
	 * final Map<String, Double[]> result;
	 * Double[] calculations1;
	 * Double[] calculations2;
	 * Double[] calculations3;
	 * Double[] calculations4;
	 * Double[] calculations5;
	 * Double[] calculations6;
	 * 
	 * calculations1 = this.adminRepository.fixUpTaskPerUser();
	 * calculations2 = this.adminRepository.applicationPerFixUpTask();
	 * calculations3 = this.adminRepository.maxPricePerFixUpTask();
	 * calculations4 = this.adminRepository.priceOferredPerApplication();
	 * calculations5 = this.adminRepository.numberComplaintsPerFixUpTask();
	 * calculations6 = this.adminRepository.notesPerReferee();
	 * 
	 * result = new HashMap<String, Double[]>();
	 * result.put("fixUpTaskPerUser", calculations1);
	 * result.put("applicationPerFixUpTask", calculations2);
	 * result.put("maxPricePerFixUpTask", calculations3);
	 * result.put("priceOferredPerApplication", calculations4);
	 * result.put("numberComplaintsPerFixUpTask", calculations5);
	 * result.put("notesPerReferee", calculations6);
	 * 
	 * return result;
	 * }
	 * 
	 * 
	 * 
	 * // SERVICIO 2
	 * public Map<String, Double> computeStatisticsRatios() {
	 * this.loggedAsAdmin();
	 * 
	 * Double ratioPendingApplications, ratioAcceptedApplications, ratioRejectedApplications, ratioPendingElapsedApplications;
	 * Double fixUpTaskWithComplain;
	 * final Map<String, Double> result;
	 * 
	 * ratioPendingApplications = this.adminRepository.ratioPendingApplications();
	 * ratioAcceptedApplications = this.adminRepository.ratioAcceptedApplications();
	 * ratioRejectedApplications = this.adminRepository.ratioRejectedApplications();
	 * ratioPendingElapsedApplications = this.adminRepository.ratioPendingElapsedApplications();
	 * fixUpTaskWithComplain = this.adminRepository.fixUpTaskWithComplain();
	 * 
	 * result = new HashMap<String, Double>();
	 * result.put("ratioPendingApplications", ratioPendingApplications);
	 * result.put("ratioAcceptedApplications", ratioAcceptedApplications);
	 * result.put("ratioRejectedApplications", ratioRejectedApplications);
	 * result.put("ratioPendingElapsedApplications", ratioPendingElapsedApplications);
	 * result.put("fixUpTaskWithComplain", fixUpTaskWithComplain);
	 * 
	 * return result;
	 * }
	 */

	public void broadcastMessage(Message message) {
		this.loggedAsAdmin();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor admin = this.actorService.getActorByUsername(userAccount.getUsername());

		List<Actor> actors = new ArrayList<Actor>();
		actors = this.actorService.findAll();

		for (Actor a : actors)
			if (!(a.equals(admin))) {
				message.setReceiver(a);
				this.messageService.sendMessageBroadcasted(message);
			}

	}

	public void banSuspiciousActor(final Actor a) {
		this.loggedAsAdmin();

		Assert.isTrue(a.getHasSpam());

		a.getUserAccount().setIsNotLocked(false);
		this.actorService.save(a);
	}

	public void unBanSuspiciousActor(final Actor a) {
		this.loggedAsAdmin();

		a.getUserAccount().setIsNotLocked(true);
		this.actorService.save(a);
	}

	public List<Admin> findAll() {
		return this.adminRepository.findAll();
	}

	/*
	 * public Admin getAdminByUsername(final String a) {
	 * return this.adminRepository.getAdminByUserName(a);
	 * }
	 */

	public Admin findOne(final int adminId) {
		return this.findOne(adminId);
	}

	public List<Float> showStatistics() {
		List<Float> statistics = new ArrayList<Float>();
		statistics.add(this.adminRepository.avgMembersPerBrotherhood());
		statistics.add(this.minMembersBrotherhood());
		statistics.add(this.maxMembersBrotherhood());
		statistics.add(this.adminRepository.stddevMembersPerBrotherhood());

		if (this.requestService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
		} else {
			statistics.add(this.adminRepository.ratioApprovedRequests());
			statistics.add(this.adminRepository.ratioPendingRequests());
			statistics.add(this.adminRepository.ratioRejectedRequests());
		}

		if (this.areaService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);

		} else {
			statistics.add(this.minBrotherhoodsArea());
			statistics.add(this.maxBrotherhoodsArea());
			statistics.add(this.adminRepository.avgNumberBrotherhoodPerArea());
			statistics.add(this.adminRepository.stddevNumberBrotherhoodPerArea());
		}

		statistics.add(this.adminRepository.minResultFinders());
		statistics.add(this.adminRepository.maxResultFinders());
		statistics.add(this.adminRepository.avgResultFinders());
		statistics.add(this.adminRepository.stddevResultFinders());
		if (this.adminRepository.numberNonEmptyFinders() == 0)
			statistics.add((float) -1);
		else
			statistics.add(this.adminRepository.ratioEmptyFinder());

		statistics.add(this.adminRepository.getRatioSpammers());
		statistics.add(this.adminRepository.getRatioNonSpammers());
		statistics.add(this.adminRepository.avgAdminPolarity() + 1);
		statistics.add(this.adminRepository.avgMemberPolarity() + 1);
		statistics.add(this.adminRepository.avgBrotherhoodPolarity() + 1);

		return statistics;
	}

	public Float maxMembersBrotherhood() {
		return this.adminRepository.maxNumberMembersPerBrotherhood();
	}

	public Float minMembersBrotherhood() {
		return this.adminRepository.minNumberMembersPerBrotherhood();
	}

	public Float maxBrotherhoodsArea() {
		return this.adminRepository.maxNumberBrotherhoodPerArea();
	}

	public Float minBrotherhoodsArea() {
		return this.adminRepository.minNumberBrotherhoodPerArea();
	}

	public Map<String, Float> countBrotherhoodsArea() {
		return this.nameStatisticsArea(this.adminRepository.numberBrotherhoodsPerArea());
	}

	public List<String> largestBrotherhoods() {
		return this.adminRepository.largestOrSmallestBrotherhoods(this.maxMembersBrotherhood());

	}
	public List<String> smallestBrotherhoods() {
		return this.adminRepository.largestOrSmallestBrotherhoods(this.minMembersBrotherhood());

	}
	public Map<String, Float> ratioBrotherhoodPerArea() {
		if (this.areaService.findAll().isEmpty())
			return new HashMap<String, Float>();
		else
			return this.nameStatisticsArea(this.adminRepository.ratioBrotherhoodPerArea());
	}
	public Map<String, Float> ratioRequestApprovedByProcession() {
		return this.nameStatisticsBrotherhood(this.noZeroDivision(this.adminRepository.ratioApprovedRequestsByProcessions()));
	}

	public Map<String, Float> ratioRequestPendingByProcession() {
		return this.nameStatisticsBrotherhood(this.noZeroDivision(this.adminRepository.ratioPendingRequestsByProcessions()));
	}

	public Map<String, Float> ratioRequestRejectedByProcession() {
		return this.nameStatisticsBrotherhood(this.noZeroDivision(this.adminRepository.ratioRejectedRequestsByProcessions()));
	}

	public List<String> processionsOfNextMonth() {
		return this.adminRepository.listProcessionBeforeDate(this.adminRepository.dateFuture());
	}

	public List<Member> membersAtLeastTenPercentRequestsApproved() {
		return this.adminRepository.listMembersAtLeastTenPercentRequestApproved();
	}

	public Float getRatioSpammers() {
		return this.adminRepository.getRatioSpammers();
	}

	public Float getRatioNonSpammers() {
		return this.adminRepository.getRatioNonSpammers();
	}

	public Float avgAdminPolarity() {
		return this.adminRepository.avgAdminPolarity();
	}

	public Float avgMemberPolarity() {
		return this.adminRepository.avgMemberPolarity();
	}

	public Float avgBrotherhoodPolarity() {
		return this.adminRepository.avgBrotherhoodPolarity();
	}

	public List<Float> noZeroDivision(List<Float> result) {
		List<Procession> pro = this.processionService.findAll();
		Assert.isTrue(result.size() == pro.size());
		for (Procession p : pro)
			if (p.getRequests().size() == 0)
				result.set(pro.indexOf(p), (float) 0);
		return result;

	}

	public Map<Position, Float> mapNumberPositions() {
		return this.numberPositions(this.adminRepository.numberPositions());
	}

	public Map<String, Float> nameStatisticsArea(List<Float> statistics) {
		Map<String, Float> result = new HashMap<String, Float>();
		List<Area> area = this.areaService.findAll();
		Assert.isTrue(statistics.size() == area.size());

		for (Area a : area)
			result.put(a.getName(), statistics.get(area.indexOf(a)));

		return result;
	}

	public Map<Position, Float> numberPositions(List<Float> statistics) {
		Map<Position, Float> result = new HashMap<Position, Float>();
		List<Position> positions = this.positionService.findAll();
		Assert.isTrue(statistics.size() == positions.size());

		for (Position p : positions)
			result.put(p, statistics.get(positions.indexOf(p)));

		return result;
	}

	public Map<String, Float> nameStatisticsBrotherhood(List<Float> statistics) {
		Map<String, Float> result = new HashMap<String, Float>();
		List<Brotherhood> brotherhood = this.brotherhoodService.findAll();
		Assert.isTrue(statistics.size() == brotherhood.size());

		for (Brotherhood b : brotherhood)
			result.put(b.getName() + "" + b.getMiddleName() + "" + b.getSurname(), statistics.get(brotherhood.indexOf(b)));

		return result;
	}

	public Admin getSystem() {
		return this.adminRepository.getSystem();
	}
	/*
	 * public List<Admin> findAll2() {
	 * return this.adminRepository.findAll2();
	 * }
	 */

	public Message reconstruct(Message message, BindingResult binding) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		domain.Message result;
		result = this.messageService.create();
		if (message.getId() == 0) {
			result = message;
			result.setSender(actor);

		} else {
			result = this.messageService.findOne(message.getId());

			result.setBody(message.getBody());
			result.setPriority(message.getPriority());
			result.setTags(message.getTags());
			result.setSubject(message.getSubject());
			result.setReceiver(actor);
		}

		//this.validator.validate(result, binding);

		return result;

	}

}
