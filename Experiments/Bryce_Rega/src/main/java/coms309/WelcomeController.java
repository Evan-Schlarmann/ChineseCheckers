package coms309;

// Controller Imports
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.*;

// Other Imports
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

@RestController
class WelcomeController {

    // Saved variables for session
    public int value = 0;
    public ArrayList<Move> moves = new ArrayList<>();

    // Get and Post Mapping
    @GetMapping("/")
    public String moves() {
        return moves.toString();
    }

    @PostMapping("/")
    public @ResponseBody String addMove(@RequestBody Move move) {
        moves.add(move);
        return "Added: " + move.toString();
    }

    @GetMapping("/{index}")
    public String findMove(@PathVariable String index) {
        return index + ": " + moves.get(Integer.parseInt(index)).toString();
    }

    @GetMapping("/time")
    public String dateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    @GetMapping("/time/{attribute}")
    public String dateAndTime(@PathVariable String attribute) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        switch (attribute) {
            case "year": return "" + now.getYear();
            case "month": return "" + now.getMonth();
            case "day": return "" + now.getDayOfMonth();
            case "dayOfWeek": return "" + now.getDayOfWeek();
            case "hour": return "" + now.getHour();
            case "minute": return "" + now.getMinute();
            case "second": return "" + now.getSecond();
        }
        return dtf.format(now);
    }

    @GetMapping("/value")
    public String value() {
        return("Value: " + (++value));
    }

    @PostMapping("/value")
    public String value(@RequestBody String increment) { return("Value: " + (value += Integer.parseInt(increment))); }

    @PostMapping("/count")
    public String count(@RequestBody String text) {
        int count = 0;
        Scanner scnr = new Scanner(text);
        while (scnr.hasNext()) {
            scnr.next();
            count++;
        }
        scnr.close();
        return "Word count: " + count;
    }

    @PostMapping("/count/{keyWord}")
    public String count(@RequestBody String text, @PathVariable String keyWord) {
        int count = 0;
        Scanner scnr = new Scanner(text);
        while (scnr.hasNext()) {
            if (scnr.next().equals(keyWord))
                count++;
        }
        scnr.close();
        return keyWord + " count: " + count;
    }

}
