import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.example.myapp.exception.AccessDeniedException;
import com.example.myapp.exception.NotFoundException;

@ControllerAdvice
public class ExceptionHandlerController {

@ExceptionHandler(NotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public String handleNotFound() {
return “not_found”;
}

@ExceptionHandler(AccessDeniedException.class)
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public String handleAccessDenied() {
return “access_denied”;
}

}
