@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        AbstractRoutingDataSource routingDataSource = new TenantRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        
        // Load configurations for each tenant
        dataSourceMap.put("tenant1", dataSourceTenant1());
        dataSourceMap.put("tenant2", dataSourceTenant2());
        
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(dataSourceDefault());
        return routingDataSource;
    }
