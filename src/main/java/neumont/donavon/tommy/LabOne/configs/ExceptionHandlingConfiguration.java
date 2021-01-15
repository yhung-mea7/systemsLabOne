package neumont.donavon.tommy.LabOne.configs;

import neumont.donavon.tommy.LabOne.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlingConfiguration {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Map<String, String>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest web
    )
    {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("code", "NO_RESOURCE_FOUND");
        responseBody.put("message", String.format("No resource with the id %s could be found", ex.getId()));
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
}
