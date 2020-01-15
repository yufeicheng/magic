package com.magic.interview.service.event_listener;

import com.magic.dao.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Cheng Yufei
 * @create 2020-01-15 11:01
 **/
public class RegisterEvent extends ApplicationEvent {

    @Getter
    private User user;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
