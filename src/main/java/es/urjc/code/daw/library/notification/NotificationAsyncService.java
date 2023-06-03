package es.urjc.code.daw.library.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationAsyncService implements ApplicationListener<NotificationEvent> {
    private String message;

    Logger logger = LoggerFactory.getLogger(NotificationAsyncService.class);

    public void setMessage(String message) {
        this.message = message;
    }

    public void onApplicationEvent(NotificationEvent event) {
        this.setMessage(event.getMessage());
        logger.info(message);
    }
}
