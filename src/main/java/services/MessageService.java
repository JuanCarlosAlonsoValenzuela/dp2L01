
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Admin;
import domain.Box;
import domain.Brotherhood;
import domain.Member;
import domain.Message;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository		messageRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AdminService			adminService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	// Actualizar caja que tiene el mensaje EN ESTE ORDEN
	// ACTUALIZAR CAJA SIN EL MENSAJE
	// BORRAR EL MENSAJE Y TODAS SUS COPIAS
	public void delete(Message m) {
		this.messageRepository.delete(m);
	}

	public Message sendMessageBroadcasted(Message message) {

		this.actorService.loggedAsActor();

		Box boxRecieved = new Box();
		Box boxSpam = new Box();
		Box boxSent = new Box();

		List<String> spam = new ArrayList<String>();

		spam = this.configurationService.getSpamWords();

		Message messageSaved = this.messageRepository.saveAndFlush(message);
		Message messageCopy = this.create(messageSaved.getSubject(), messageSaved.getBody(), messageSaved.getPriority(), messageSaved.getReceiver());
		Message messageCopySaved = this.messageRepository.save(messageCopy);

		boxSent = this.boxService.getSentBoxByActor(messageSaved.getSender());
		boxRecieved = this.boxService.getRecievedBoxByActor(messageSaved.getReceiver());
		boxSpam = this.boxService.getSpamBoxByActor(messageSaved.getReceiver());

		// Guardar la box con ese mensaje;

		if (this.configurationService.isStringSpam(messageSaved.getBody(), spam) || this.configurationService.isStringSpam(messageSaved.getSubject(), spam)) {
			boxSent.getMessages().add(messageSaved);
			boxSpam.getMessages().add(messageCopySaved);

			this.boxService.saveSystem(boxSent);
			this.boxService.saveSystem(boxSpam);
			this.actorService.save(messageSaved.getSender());
			this.actorService.flushSave(messageSaved.getReceiver());

		} else {
			boxRecieved.getMessages().add(messageCopySaved);
			boxSent.getMessages().add(messageSaved);
			//boxRecieved.setMessages(list);
			this.boxService.saveSystem(boxSent);
			this.boxService.saveSystem(boxRecieved);
			this.actorService.save(messageSaved.getSender());
			this.actorService.flushSave(messageSaved.getReceiver());
		}
		return messageSaved;
	}

	// Metodo para enviar un mensaje a un ACTOR (O varios, que tambien puede ser)
	public Message sendMessage(Message message) {

		this.actorService.loggedAsActor();

		Actor actorRecieved = message.getReceiver();
		Actor senderActor = message.getSender();

		Box boxRecieved = new Box();
		Box boxSpam = new Box();
		Box boxSent = new Box();

		List<String> spam = new ArrayList<String>();

		spam = this.configurationService.getSpamWords();

		Message messageSaved = this.messageRepository.save(message);
		Message messageCopy = this.create(messageSaved.getSubject(), messageSaved.getBody(), messageSaved.getPriority(), messageSaved.getReceiver());
		Message messageCopySaved = this.messageRepository.save(messageCopy);
		boxSent = this.boxService.getSentBoxByActor(messageSaved.getSender());
		boxRecieved = this.boxService.getRecievedBoxByActor(actorRecieved);
		boxSpam = this.boxService.getSpamBoxByActor(actorRecieved);

		// Guardar la box con ese mensaje;

		if (this.configurationService.isStringSpam(messageSaved.getBody(), spam) || this.configurationService.isStringSpam(messageSaved.getSubject(), spam)) {
			boxSent.getMessages().add(messageSaved);
			boxSpam.getMessages().add(messageCopySaved);

			this.boxService.saveSystem(boxSent);
			this.boxService.saveSystem(boxSpam);
			this.actorService.save(messageSaved.getSender());
			this.actorService.save(actorRecieved);

		} else {
			boxRecieved.getMessages().add(messageCopySaved);
			boxSent.getMessages().add(messageSaved);
			//boxRecieved.setMessages(list);
			this.boxService.saveSystem(boxSent);
			this.boxService.saveSystem(boxRecieved);
			this.actorService.save(messageSaved.getSender());
			this.actorService.save(actorRecieved);
		}

		//Calculamos la Polarity y el hasSpam
		this.actorService.updateActorSpam(senderActor);
		this.configurationService.computeScore(senderActor);
		return messageSaved;
	}

	public void sendNotificationDropOut(Brotherhood bro) {
		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		Member loggedMember = this.memberService.loggedMember();
		Admin admin = this.adminService.getSystem();
		Box sentAdmin = this.boxService.getSentBoxByActor(admin);
		Box notMem = this.boxService.getNotificationBoxByActor(loggedMember);
		Box notBro = this.boxService.getNotificationBoxByActor(bro);
		Message messageBro = null;
		Message messageMem = null;
		Message copyBro = null;
		Message copyMem = null;
		if (locale == "EN") {
			messageBro = this.create("Drop out notification", "The user " + loggedMember.getUserAccount().getUsername() + " has dropped out the brotherhood.", "HIGH", bro);
			messageMem = this.create("Drop out notification", "you have dropped out the brotherhood " + bro.getTitle(), "HIGH", loggedMember);
			copyBro = this.create(messageBro.getSubject(), messageBro.getBody(), messageBro.getPriority(), messageBro.getSender());
			copyMem = this.create(messageMem.getSubject(), messageMem.getBody(), messageMem.getPriority(), messageMem.getSender());
		} else if (locale == "ES") {
			messageBro = this.create("Notificación de salida", "El usuario " + loggedMember.getUserAccount().getUsername() + " ha dejado la hermandad.", "HIGH", bro);
			messageMem = this.create("Notificación de salida", "Has dejado la hermandad " + bro.getTitle(), "HIGH", loggedMember);
			copyBro = this.create(messageBro.getSubject(), messageBro.getBody(), messageBro.getPriority(), messageBro.getSender());
			copyMem = this.create(messageMem.getSubject(), messageMem.getBody(), messageMem.getPriority(), messageMem.getSender());
		}
		this.messageRepository.save(messageBro);
		this.messageRepository.save(messageMem);
		this.messageRepository.save(copyBro);
		this.messageRepository.save(copyMem);
		sentAdmin.getMessages().add(messageBro);
		sentAdmin.getMessages().add(messageMem);
		notMem.getMessages().add(copyMem);
		notBro.getMessages().add(copyBro);
		this.boxService.save(sentAdmin);
		this.boxService.save(notMem);
		this.boxService.save(notBro);
		this.adminService.save(admin);
		this.memberService.save(loggedMember);
		this.brotherhoodService.save(bro);
	}

	public void sendNotificationBroEnrolMem(Member mem) {
		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Admin admin = this.adminService.getSystem();
		Box sentAdmin = this.boxService.getSentBoxByActor(admin);
		Box notMem = this.boxService.getNotificationBoxByActor(mem);
		Box notBro = this.boxService.getNotificationBoxByActor(loggedBrotherhood);
		Message messageBro = null;
		Message messageMem = null;
		Message copyBro = null;
		Message copyMem = null;
		if (locale == "EN") {
			messageBro = this.create("Enrol notification", "You have accepted the user " + mem.getUserAccount().getUsername() + " to the brotherhood.", "HIGH", loggedBrotherhood);
			messageMem = this.create("Enrol notification", "You have been accepted into the brotherhood " + loggedBrotherhood.getTitle(), "HIGH", mem);
			copyBro = this.create(messageBro.getSubject(), messageBro.getBody(), messageBro.getPriority(), messageBro.getSender());
			copyMem = this.create(messageMem.getSubject(), messageMem.getBody(), messageMem.getPriority(), messageMem.getSender());
		} else if (locale == "ES") {
			messageBro = this.create("Notificación de inscripción", "Has aceptado al usuario " + mem.getUserAccount().getUsername() + " a la hermandad.", "HIGH", loggedBrotherhood);
			messageMem = this.create("Notificación de inscripción", "Has sido aceptado en la hermandad " + loggedBrotherhood.getTitle(), "HIGH", mem);
			copyBro = this.create(messageBro.getSubject(), messageBro.getBody(), messageBro.getPriority(), messageBro.getSender());
			copyMem = this.create(messageMem.getSubject(), messageMem.getBody(), messageMem.getPriority(), messageMem.getSender());
		}
		this.messageRepository.save(messageBro);
		this.messageRepository.save(messageMem);
		this.messageRepository.save(copyBro);
		this.messageRepository.save(copyMem);
		sentAdmin.getMessages().add(messageBro);
		sentAdmin.getMessages().add(messageMem);
		notMem.getMessages().add(copyMem);
		notBro.getMessages().add(copyBro);
		this.boxService.save(sentAdmin);
		this.boxService.save(notMem);
		this.boxService.save(notBro);
		this.adminService.save(admin);
		this.memberService.save(mem);
		this.brotherhoodService.save(loggedBrotherhood);

	}

	public Message sendMessageAnotherSender(Message message) {

		Actor actorRecieved = message.getReceiver();
		List<String> spam = new ArrayList<String>();

		spam = this.configurationService.getSpamWords();

		Box boxRecieved = new Box();
		Box boxSpam = new Box();
		Box boxSent = new Box();

		Message messageSaved = this.messageRepository.save(message);
		Message messageCopy = this.createNotification(messageSaved.getSubject(), messageSaved.getBody(), messageSaved.getPriority(), message.getTags(), messageSaved.getReceiver());
		Message messageCopySaved = this.messageRepository.save(messageCopy);
		boxSent = this.boxService.getSentBoxByActor(messageSaved.getSender());
		boxRecieved = this.boxService.getRecievedBoxByActor(actorRecieved);
		boxSpam = this.boxService.getSpamBoxByActor(actorRecieved);

		// Guardar la box con ese mensaje;

		if (this.configurationService.isStringSpam(messageSaved.getBody(), spam) || this.configurationService.isStringSpam(messageSaved.getSubject(), spam)) {
			boxSent.getMessages().add(messageSaved);
			boxSpam.getMessages().add(messageCopySaved);

			this.boxService.saveSystem(boxSent);
			this.boxService.saveSystem(boxSpam);
			this.actorService.save(messageSaved.getSender());
			this.actorService.save(actorRecieved);

		} else {
			boxRecieved.getMessages().add(messageCopySaved);
			boxSent.getMessages().add(messageSaved);
			//boxRecieved.setMessages(list);
			this.boxService.saveSystem(boxSent);
			this.boxService.saveSystem(boxRecieved);
			this.actorService.save(messageSaved.getSender());
			this.actorService.save(actorRecieved);
		}
		return messageSaved;
	}
	public Message save(Message message) {
		return this.messageRepository.save(message);

	}

	public Message create() {

		this.actorService.loggedAsActor();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1000);

		Message message = new Message();
		Actor sender = this.actorService.getActorByUsername(userAccount.getUsername());
		Actor receiver = new Actor();
		message.setMoment(thisMoment);
		message.setSubject("");
		message.setBody("");
		message.setPriority("");
		message.setReceiver(receiver);
		message.setTags("");
		message.setSender(sender);

		return message;
	}

	public Message create(String Subject, String body, String priority, Actor recipient) {

		this.actorService.loggedAsActor();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		Message message = new Message();

		Actor sender = this.actorService.getActorByUsername(userAccount.getUsername());

		message.setMoment(thisMoment);
		message.setSubject(Subject);
		message.setBody(body);
		message.setPriority(priority);
		message.setReceiver(recipient);
		message.setTags("");
		message.setSender(sender);

		return message;
	}

	public Message createNotification(String Subject, String body, String priority, String tags, Actor recipient) {
		this.actorService.loggedAsActor();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		Message message = new Message();

		Actor sender = this.actorService.getActorByUsername("system");

		message.setMoment(thisMoment);
		message.setSubject(Subject);
		message.setBody(body);
		message.setPriority(priority);
		message.setReceiver(recipient);
		message.setTags(tags);
		message.setSender(sender);

		return message;
	}

	public void updateMessage(Message message, Box box) { // Posible problema
		// con copia

		this.actorService.loggedAsActor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		for (Box b : actor.getBoxes()) {
			if (b.getMessages().contains(message))
				b.getMessages().remove(message);
			//list.remove(message);
			//b.setMessages(list);
			if (b.getName().equals(box.getName())) {
				List<Message> list = b.getMessages();
				list.add(message);
				b.setMessages(list);
			}
		}
	}

	public void deleteMessageToTrashBox(Message message) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		//Box currentBox = this.boxService.getCurrentBoxByMessage(message);

		List<Box> currentBoxes = new ArrayList<>();

		for (Box b : actor.getBoxes())
			if (b.getMessages().contains(message))
				currentBoxes.add(b);

		Box trash = this.boxService.getTrashBoxByActor(actor);

		// When an actor removes a message from a box other than trash box, it
		// is moved to the trash box;
		for (Box currentBox : currentBoxes)
			if (currentBox.equals(trash)) {
				for (Box b : actor.getBoxes())
					if (b.getMessages().contains(message)) {
						b.getMessages().remove(message);
						this.messageRepository.delete(message);
					}
			} else
				this.updateMessage(message, trash);
		// this.messageRepository.save(message); Si se pone en el metodo
		// updateMessage no hace falta aqui
	}

	public void copyMessage(Message message, Box box) {

		this.actorService.loggedAsActor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		for (Box b : actor.getBoxes())
			if (b.getName().equals(box.getName())) {
				List<Message> list = b.getMessages();
				list.add(message);
				b.setMessages(list);
			}
	}

	public List<Message> findAll() {
		return this.messageRepository.findAll();
	}

	public List<Message> findAll2() {
		return this.messageRepository.findAll2();
	}

	public Message findOne(int id) {
		return this.messageRepository.findOne(id);
	}

	public List<Message> getMessagesByBox(Box b) {
		return b.getMessages();
	}
}
