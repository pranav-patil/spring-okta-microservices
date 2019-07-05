package com.emprovise.service.data.config;

import com.emprovise.service.data.dto.AccountDTO;
import com.emprovise.service.data.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class AppInitializator {

    @Autowired
    private AccountRepository repository;
    private static final Logger log = LoggerFactory.getLogger(AppInitializator.class);

    @PostConstruct
    private void init() {
        log.info("AppInitializator initialization logic ...");

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1);
        accountDTO.setAccountHolder("John");
        accountDTO.setStockId("APPL");
        accountDTO.setStockName("Apple Inc");
        accountDTO.setTotalStocks(20);
        repository.save(accountDTO.getId(), accountDTO);
    }
}