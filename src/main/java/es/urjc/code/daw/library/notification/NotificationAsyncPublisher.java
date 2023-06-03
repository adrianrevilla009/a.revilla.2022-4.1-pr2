package es.urjc.code.daw.library.notification;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class NotificationAsyncPublisher implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;


    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void sendNotification(String message) {
        publisher.publishEvent(new NotificationEvent(this, message));
    }
}
