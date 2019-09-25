package com.train4game.social.web;

import com.train4game.social.util.exception.NotFoundException;
import com.train4game.social.util.exception.TokenExpiredException;
import com.train4game.social.web.controllers.ProfileController;
import com.train4game.social.web.controllers.RegistrationController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.train4game.social.util.ValidationUtil.logAndGetRootCause;

@ControllerAdvice(basePackageClasses = {ProfileController.class, RegistrationController.class})
@AllArgsConstructor
@Slf4j
public class ProfileExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public String handleExpiredVerificationToken(HttpServletRequest req, TokenExpiredException e) {
        logAndGetRootCause(log, req, e);
        return "redirect:/register?expired";
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(HttpServletRequest req, NotFoundException e) {
        logAndGetRootCause(log, req, e);
        return new ModelAndView("redirect:/login?notfound");
    }
}
