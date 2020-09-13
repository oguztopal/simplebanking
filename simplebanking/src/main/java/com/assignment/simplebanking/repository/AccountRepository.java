package com.assignment.simplebanking.repository;

import com.assignment.simplebanking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


public interface AccountRepository extends JpaRepository<Account,Long>{

  /**
   * get account by account Number
   *
   * @param accountNumber
   * @return Account
   */

  Account getByAccountNumber(String accountNumber);

  /**
   * check approvelCode in system
   *
   * @param accountId
   * @param approvalCode
   * @return Long
   */
  @Query("SELECT count(a) FROM Account a INNER JOIN Transaction t ON t.account=a.id where t.approvalCode =:approvalCode and a.id =:accountId")
  Long checkApprovalCode(@Param("accountId") Long accountId,@Param("approvalCode") UUID approvalCode);


  /**
   * check Account Number
   *
   * @param accountNumber
   * @return Long
   */
  Long countAccountByAccountNumber(String accountNumber);

}
