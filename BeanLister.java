import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanLister {
    public static void main(String[] args) {
        // Load the application context (you can configure it according to your setup)
        ApplicationContext context = new AnnotationConfigApplicationContext(MyAppConfig.class);
        
        // Cast the ApplicationContext to ListableBeanFactory
        ListableBeanFactory beanFactory = (ListableBeanFactory) context;
        
        // Get all bean definition names (whether instantiated or not)
        String[] allBeanNames = beanFactory.getBeanDefinitionNames();
        
        // Get active bean names (only those currently instantiated)
        String[] activeBeanNames = beanFactory.getBeanNamesForType(Object.class);
        
        // Print all beans
        System.out.println("All Beans in the application context:");
        for (String beanName : allBeanNames) {
            System.out.println(beanName);
        }
        
        // Print active beans
        System.out.println("\nActive Beans in the application context:");
        for (String activeBeanName : activeBeanNames) {
            System.out.println(activeBeanName);
        }
    }
}
