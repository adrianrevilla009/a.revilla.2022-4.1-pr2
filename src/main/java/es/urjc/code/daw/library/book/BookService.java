package es.urjc.code.daw.library.book;

import java.util.List;
import java.util.Optional;

import es.urjc.code.daw.library.Features;
import es.urjc.code.daw.library.notification.NotificationAsyncPublisher;
import es.urjc.code.daw.library.services.LineBreaker;
import org.springframework.stereotype.Service;

import es.urjc.code.daw.library.notification.NotificationService;
import org.togglz.core.manager.FeatureManager;

/* Este servicio se usará para incluir la funcionalidad que sea 
 * usada desde el BookRestController y el BookWebController
 */
@Service
public class BookService {

	private final int LINE_LENGTH = 10;

	private BookRepository repository;
	private NotificationService notificationService;

	private FeatureManager featureManager;

	private NotificationAsyncPublisher notificationAsyncPublisher;

	private LineBreaker lineBreaker;

	public BookService(BookRepository repository, NotificationService notificationService,
					   FeatureManager featureManager, NotificationAsyncPublisher notificationAsyncPublisher,
					   LineBreaker lineBreaker){
		this.repository = repository;
		this.notificationService = notificationService;
		this.featureManager = featureManager;
		this.notificationAsyncPublisher = notificationAsyncPublisher;
		this.lineBreaker = lineBreaker;
	}

	public Optional<Book> findOne(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Book> findAll() {
		return repository.findAll();
	}

	public Book save(Book book) {
		Book newBook;

		/***** feature line breaker *****/
		if (this.featureManager.isActive(Features.FEATURE_LINE_BREAKER)) {
			String description = this.lineBreaker.breakLine(book.getDescription(), this.LINE_LENGTH);
			book.setDescription(description);
			newBook = repository.save(book);
		} else {
			newBook = repository.save(book);
		}

		/***** feature async notifications *****/
		if (this.featureManager.isActive(Features.FEATURE_EVENT_NOTIFICATION)) {
			notificationAsyncPublisher.sendNotification("[2] Book Async Event: book with title=" + newBook.getTitle() + " was created");
		} else {
			notificationService.notify("[1] Book Event: book with title=" + newBook.getTitle() + " was created");
		}

		return newBook;
	}

	public void delete(long id) {
		repository.deleteById(id);
		if (this.featureManager.isActive(Features.FEATURE_EVENT_NOTIFICATION)) {
			notificationAsyncPublisher.sendNotification("[2] Book Async Event: book with id=" + id + " was deleted");
		} else {
			notificationService.notify("[1] Book Event: book with id=" + id + " was deleted");
		}
	}
}
