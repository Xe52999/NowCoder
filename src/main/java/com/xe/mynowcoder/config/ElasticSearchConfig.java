package com.xe.mynowcoder.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

/**
 * @author xjx
 * @DATE 2022/8/19
 */
//@Configuration
public class ElasticSearchConfig {

    /**
     * localhost:9300 写在配置文件中就可以了
     */
    @Bean
    RestHighLevelClient elasticsearchClient() {
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo("localhost:9300")
                //.withConnectTimeout(Duration.ofSeconds(5))
                //.withSocketTimeout(Duration.ofSeconds(3))
                //.useSsl()
                //.withDefaultHeaders(defaultHeaders)
                //.withBasicAuth(username, password)
                // ... other options
                .build();
        RestHighLevelClient client = RestClients.create(configuration).rest();
        return client;
    }
}

