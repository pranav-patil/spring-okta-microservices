package com.emprovise.service.data.config;

import com.emprovise.service.data.dto.AccountDTO;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 * @Refer https://piotrminkowski.wordpress.com/2017/11/13/in-memory-data-grid-with-apache-ignite/
 */
@Configuration
@EnableIgniteRepositories(value="com.emprovise.service.data.repository",queryLookupStrategy = QueryLookupStrategy.Key.CREATE)
public class SpringDataConfig {

    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration config = new IgniteConfiguration();

        // Setting some custom name for the node.
        config.setIgniteInstanceName("ignite-cluster-node");
        // Enabling peer-class loading feature.
        config.setPeerClassLoadingEnabled(true);
        // Defining and creating a new cache to be used by Ignite Spring Data repository.
        CacheConfiguration accountCache = new CacheConfiguration("accountCache");
        // Setting SQL schema for the cache.
        accountCache.setIndexedTypes(Integer.class, AccountDTO.class);
        config.setCacheConfiguration(accountCache);
        config.setGridLogger(new Slf4jLogger());
        return Ignition.start(config);
    }
}
