Commands:

- SecurityConfig - "/v3/api-docs/**", "/swagger-ui/**"
- Prometheus
    - management.endpoint.health.show-details=always
    - management.metrics.export.prometheus.enabled=true

            <dependency>    
                <groupId>io.micrometer</groupId>   
                <artifactId>micrometer-registry-prometheus</artifactId>
            </dependency>
    - SecurityConfig - "/actuator/prometheus"    
 - ELK
    - setx ES_JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-21.0.4.7-hotspot"
    - bin/elasticsearch-create-enrollment-token -s kibana --url "https://localhost:9200"
    - bin/elasticsearch-reset-password -u elastic
- Logging
    - logging.file.name=C:/Docs/Intellij/BankingApp/RetailBanking.log
    - logging.file.name=RetailBanking.log
    - logging.level.root=INFO



 
