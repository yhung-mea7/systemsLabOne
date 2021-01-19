package neumont.donavon.tommy.LabOne.configs;

import neumont.donavon.tommy.LabOne.exceptions.BadServiceRequestStatusUpdateException;
import neumont.donavon.tommy.LabOne.exceptions.ResourceNotFoundException;
import neumont.donavon.tommy.LabOne.exceptions.UserAlreadyExistException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlingConfiguration {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected HttpEntity<Map<String, String>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest web
    ) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("code", "NO_RESOURCE_FOUND");
        if (ex.getId() == 0) {
            responseBody.put("message", "No resource could be found with the provided id");
        } else {
            responseBody.put("message", String.format("No resource with the id %s could be found", ex.getId()));

        }
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected HttpEntity<Map<String, String>> handleUserNameNotFoundException(
            UsernameNotFoundException ex,
            WebRequest web
    ) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("code", "USER_NOT_FOUND");
        responseBody.put("message", "That user could not be found");
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected HttpEntity<Map<String, String>> handleUserAlreadyExistsException(
            UserAlreadyExistException ex,
            WebRequest web
    ) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("code", "USER_ALREADY_EXISTS");
        responseBody.put("message", "A user with the provided username or email already exists");
        return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadServiceRequestStatusUpdateException.class)
    protected HttpEntity<Map<String, String>> handleBadStatusException(
            BadServiceRequestStatusUpdateException ex,
            WebRequest web
    ){
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("code", "BAD_STATUS_UPDATE_REQUEST");
        responseBody.put("message", "status must be set to either claim or complete in URI or you do not have permission to alter this service request");
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
