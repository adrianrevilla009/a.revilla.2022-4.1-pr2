package es.urjc.code.daw.library.unitary;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.urjc.code.daw.library.Features;
import es.urjc.code.daw.library.notification.NotificationAsyncPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.urjc.code.daw.library.book.Book;
import es.urjc.code.daw.library.book.BookRepository;
import es.urjc.code.daw.library.book.BookService;
import es.urjc.code.daw.library.notification.NotificationService;
import org.togglz.core.manager.FeatureManager;

@DisplayName("BookService Unitary tests")
public class BookServiceUnitaryTest {

    private BookService bookService;
    private NotificationService notificationService;
    private BookRepository repository;

    private FeatureManager featureManager;

    private NotificationAsyncPublisher notificationAsyncPublisher;

    @BeforeEach
	public void setup() {
        
        repository = mock(BookRepository.class);
        notificationService = mock(NotificationService.class);
        featureManager = mock(FeatureManager.class);
        notificationAsyncPublisher = mock(NotificationAsyncPublisher.class);
        bookService = new BookService(repository, notificationService, featureManager,
                notificationAsyncPublisher);
			
    }

    @Test
    @DisplayName("Cuando se guarda un libro utilizando BookService, se guarda en el repositorio y se lanza una notificación")
	public void createBook(){

        Book book = new Book("FAKE BOOK", "FAKE DESCRIPTION");

		// Given
		when(repository.save(book)).thenReturn(book);
		
		// When
		bookService.save(book);

        // Then
        verify(repository).save(book);
        if (this.featureManager.isActive(Features.FEATURE_EVENT_NOTIFICATION)) {
            verify(notificationAsyncPublisher).sendNotification("[2] Book Async Event: book with title=" + book.getTitle() + " was created");
        } else {
            verify(notificationService).notify("[1] Book Event: book with title=" + book.getTitle() + " was created");
        }
    }
    
    @Test
    @DisplayName("Cuando se borra un libro utilizando BookService, se elimina del repositorio y se lanza una notificación")
	public void deleteBook(){
        
        long fakeId = 1L;

        // Given
        // No es necesario definir el comportamiento del delete()
        // -> mock() se ocupa de que no realice las acciones reales
        // -> Devuelve void
		// when(repository.delete(fakeId);
		
		// When
		bookService.delete(fakeId);

        // Then
        verify(repository).deleteById(fakeId);
        if (this.featureManager.isActive(Features.FEATURE_EVENT_NOTIFICATION)) {
            verify(notificationAsyncPublisher).sendNotification("[2] Book Async Event: book with id=" + fakeId + " was deleted");
        } else {
            verify(notificationService).notify("[1] Book Event: book with id=" + fakeId + " was deleted");
        }
	}

}
