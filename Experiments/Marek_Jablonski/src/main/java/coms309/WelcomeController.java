package coms309;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class WelcomeController {

    public String welcomeMessage = "Hello and welcome to COMS 309";
    public String totallySecurePassword = "coms309";

    @GetMapping("/")
    public String welcome() {
        return welcomeMessage;
    }

    @GetMapping("/api/calculator/{function}")
    public String calc_received(@RequestBody String data,@PathVariable String function) {
        String[] split = data.split(",");
        int total =0;

    switch(function){
        case "add":
            total =0;
            for(String element : split) {
                total += Integer.parseInt(element);
            }
            break;
        case "multiply":
            total =1;
            for(String element : split){
                total *= Integer.parseInt(element);
            }
            break;
    }

        return "calc received: "+total;
    }

    @PostMapping("/api/site/{function}")
    public ResponseEntity<?> welcome(@RequestBody String data, @PathVariable String function, @RequestParam String password) {

        if(!password.equals(totallySecurePassword) ){
            return new ResponseEntity("", HttpStatus.FORBIDDEN);
        }

        String retVal = "ok";

        switch(function) {
            case "setWelcome":
                welcomeMessage = data;
                break;
            default:
                retVal = "function invalid";
                break;
        }
        return new ResponseEntity(retVal, HttpStatus.OK);
    }
}
