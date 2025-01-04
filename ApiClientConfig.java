@Configuration
public class ApiClientConfig {
    
    @Bean
    public ApiClient apiClient() {
        return (ApiClient) Proxy.newProxyInstance(
            ApiClient.class.getClassLoader(),
            new Class[] { ApiClient.class },
            (proxy, method, args) -> {
                // Change endpoint based on configuration
                String endpoint = configService.getEndpoint();
                return method.invoke(new ApiClientImpl(endpoint), args);
            }
        );
    }
}
