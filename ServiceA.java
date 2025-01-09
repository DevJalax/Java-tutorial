import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Service
public class ServiceA {

	  private void performTask() {
	        System.out.println("Service A is performing the task... "+ new Date() );
	        // Your task logic goes here
	    }

	    @Scheduled(cron = "0/5 * * * * ?") // Every 1 minute
	    @SchedulerLock(name = "taskA", lockAtMostFor = "PT04S", lockAtLeastFor = "PT04S")
	    public void scheduledTaskA() {
	        performTask();
	    }
	
}
