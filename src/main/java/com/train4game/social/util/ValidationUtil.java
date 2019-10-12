package com.train4game.social.util;

import com.train4game.social.HasId;
import com.train4game.social.util.exception.IllegalRequestDataException;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ValidationUtil {
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e) {
        Throwable rootCause = getRootCause(e);
        log.error("{} at request {}", rootCause, req.getRequestURL());
        return rootCause;
    }
}
