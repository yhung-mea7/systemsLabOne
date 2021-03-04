package neumont.donavon.tommy.LabOne.controllers;

import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class DockerRestController {



    @GetMapping
    public HttpEntity<Map<String, String>> roundRobin() throws UnknownHostException {
        String s = InetAddress.getLocalHost().getHostName();
        Map<String, String> response = new HashMap<>();
        response.put("Message", "Heya I've been hit at " + s);
        System.out.println("I've been hit!" + s);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }



}
