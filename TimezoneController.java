import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.timezone_mgmt.dto.TimezoneRequest;

@RestController
public class TimezoneController {

    @PostMapping("/timezones")
    public ResponseEntity<Map<String, Object>> getTimes(@RequestBody TimezoneRequest request) {
        DateTime residentTime = DateTime.now(DateTimeZone.forID(request.getResidentCountry()));
        DateTime workTime = DateTime.now(DateTimeZone.forID(request.getWorkLocation()));

        int hourDifference = workTime.getHourOfDay() - residentTime.getHourOfDay();

        Map<String, Object> response = new HashMap<>();
        response.put("residentTime", residentTime.toString());
        response.put("workTime", workTime.toString());
        response.put("hourDifference", hourDifference);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
