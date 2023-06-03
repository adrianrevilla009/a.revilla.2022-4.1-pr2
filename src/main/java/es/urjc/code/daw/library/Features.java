package es.urjc.code.daw.library;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;

public enum Features implements Feature {
    @Label("Feature Line Breaker")
    FEATURE_LINE_BREAKER,

    @Label("Feature Event Notification")
    FEATURE_EVENT_NOTIFICATION;
}
