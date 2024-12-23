import java.util.concurrent.TimeUnit;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile({"httprestconnection"})
@EnableScheduling
public class IdleConnectionMonitorScheduler {
   private static final Logger log = LoggerFactory.getLogger(IdleConnectionMonitorScheduler.class);
  
  @Value("${CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS}")
   private int CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS;
  
  @Autowired
   private PoolingHttpClientConnectionManager connectionManager;

   @Scheduled(
      initialDelayString = "${IdleConnectionMonitor_InitialDelay}",
      fixedDelayString = "${IdleConnectionMonitor_fixedDelay}"
   )
   public void run() {
      try {
         if (this.connectionManager != null) {
            log.trace("Scheduler Run IdleConnectionMonitor - Going to close if any expired and idle connections..");
            this.connectionManager.closeExpiredConnections();
            this.connectionManager.closeIdleConnections((long)this.CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS, TimeUnit.SECONDS);
         } else {
            log.trace("Scheduler Run IdleConnectionMonitor - HttpClient ConnectionManager is not initialized");
         }
      } catch (Exception var2) {
         log.error("Scheduler Run IdleConnectionMonitor Error- errmsg={}, error={}", var2.getMessage(), var2);
      }

   }
}
