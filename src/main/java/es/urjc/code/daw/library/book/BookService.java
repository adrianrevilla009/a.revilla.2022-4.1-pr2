package es.urjc.code.daw.library.book;

import java.util.List;
import java.util.Optional;

import es.urjc.code.daw.library.Features;
import org.springframework.stereotype.Service;

import es.urjc.code.daw.library.notification.NotificationService;
import org.togglz.core.manager.FeatureManager;

/* Este servicio se usará para incluir la funcionalidad que sea 
 * usada desde el BookRestController y el BookWebController
 */
@Service
public class BookService {

	private BookRepository repository;
	private NotificationService notificationService;

	private FeatureManager featureManager;

	public BookService(BookRepository repository, NotificationService notificationService,
					   FeatureManager featureManager){
		this.repository = repository;
		this.notificationService = notificationService;
		this.featureManager = featureManager;
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
		Book newBook = repository.save(book);
		if (this.featureManager.isActive(Features.FEATURE_EVENT_NOTIFICATION)) {
			System.out.println("Feature event notification");
		} else {
			notificationService.notify("Book Event: book with title=" + newBook.getTitle() + " was created");
		}
		return newBook;
	}

	public void delete(long id) {
		repository.deleteById(id);
		if (this.featureManager.isActive(Features.FEATURE_EVENT_NOTIFICATION)) {
			System.out.println("Feature event notification");
		} else {
			notificationService.notify("Book Event: book with id=" + id + " was deleted");
		}
	}
}
