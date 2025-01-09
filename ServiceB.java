import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ServiceB {

	@Scheduled(cron = "0/30 * * * * ?") // Every 1 second
	public void independentTask() {
	    System.out.println("Independent task in App2 is running from demo... " + new Date());
	    // Logic for the independent task
	}

	
}
