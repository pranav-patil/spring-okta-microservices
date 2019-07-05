package com.emprovise.service.data.repository;

import com.emprovise.service.data.dto.AccountDTO;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryConfig(cacheName = "accountCache")
public interface AccountRepository extends IgniteRepository<AccountDTO, Integer> {

//    List<AccountDTO> findByAccountHolder(String accountHolder);
//    AccountDTO getEmployeeDTOById(Integer id);

//    @Query("SELECT id FROM Person WHERE orgId > ?")
//    public List<Long> selectId(long orgId, Pageable pageable);
}