package com.assignment.simplebanking.services.impl;

import com.assignment.simplebanking.model.Account;
import com.assignment.simplebanking.model.dto.AccountDto;
import com.assignment.simplebanking.model.exception.InsufficientBalanceException;
import com.assignment.simplebanking.model.Transaction;
import com.assignment.simplebanking.controller.TransactionStatus;
import com.assignment.simplebanking.repository.AccountRepository;
import com.assignment.simplebanking.services.AccountService;
import com.assignment.simplebanking.type.ErrorCode;
import com.assignment.simplebanking.type.TransactionType;
import com.assignment.simplebanking.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

  private  AccountRepository accountRepository;

  @Override
  public Account findAccount(String accountNumber){
    if (accountNumber != null && !accountNumber.equalsIgnoreCase("")){
      return accountRepository.getByAccountNumber(accountNumber);
    }
    return null;
  }

  @Override
  public  <T extends Transaction> TransactionStatus transaction(String accountNumber, T trasaction) throws InsufficientBalanceException{

    Account account = accountRepository.getByAccountNumber(accountNumber);
    if (account != null){
      if (trasaction.getAmount() != null) {
        if (!trasaction.getAmount().equals(0.0) && trasaction.getAmount().compareTo(0.0) > 0){
          //Transaction operation
          Set<Transaction> transactionSet = createNewTransaction(account,trasaction);

          //Account operation and save
          Double newBalance = transactionSet.iterator().next().calculateBalance(trasaction.getAmount());
          if (newBalance.compareTo(0.0)<0){
            throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE, Constants.INSUFFICIENT_BALANCE);
          }else{
            account.setBalance(newBalance);
          }

          account.getTransactions().addAll(transactionSet);

          accountRepository.save(account);

          return createNewTransactionResponse(HttpStatus.OK,transactionSet.iterator().next().getApprovalCode());
        }else{
          throw new InsufficientBalanceException(ErrorCode.AMOUNT_NOT_BE_NEGATIVE_OR_ZERO,Constants.AMOUNT_NOT_BE_NEGATIVE_OR_ZERO);
        }
      }else {
        throw new InsufficientBalanceException(ErrorCode.AMOUNT_NOT_BE_NULL,Constants.ACCOUNT_NOT_BE_NULL);
      }
    }else{
      throw new InsufficientBalanceException(ErrorCode.ENTITY_NOT_FOUND, Constants.ACCOUNT_NOT_FOUND);
    }
  }



  @Override
  public void createAccount(AccountDto dto) throws EntityExistsException{
    if (dto!= null){
      if (accountRepository.countAccountByAccountNumber(dto.getAccountNumber()).compareTo(0L)==0){
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(dto,Account.class);
        Set<String> accountNumbers = new HashSet<>();
        accountNumbers.add(dto.getAccountNumber());
        account.setAccountNumber(accountNumbers);
        accountRepository.save(account);
      }else{
        throw new EntityExistsException("Account Number Already Exist in System");
      }
    }
  }

  private <T extends Transaction> Set<Transaction> createNewTransaction(Account account,T transaction){
    try {
      Set<Transaction> transactionSet = new HashSet<>();
      Transaction newTranstion = createNewInstance(transaction.getClass());
      if (newTranstion != null){
        newTranstion.setAccount(account);
        newTranstion.setAmount(transaction.getAmount());
        newTranstion.setType(TransactionType.DepositTransaction);
        boolean check = false;
        while (!check){
          UUID generetadUUID = UUID.randomUUID();
          if (accountRepository.checkApprovalCode(account.getId(),generetadUUID).compareTo(0L) == 0){
            newTranstion.setApprovalCode(generetadUUID);
            check = true;
          }
        }
        transactionSet.add(newTranstion);
        return transactionSet;
      }else{
        return Collections.emptySet();
      }
    }catch (Exception ex){
      log.error(String.format("error while creatin new Transaction ,errormessage:%s ", ex.getMessage()));
    }
    return Collections.emptySet();
  }

  private TransactionStatus createNewTransactionResponse(HttpStatus httpStatus,UUID approvalCode){
    TransactionStatus transactionStatus = new TransactionStatus();
    transactionStatus.setStatus(httpStatus);
    transactionStatus.setApprovalCode(approvalCode);
    return transactionStatus;
  }

  private <T> T createNewInstance(Class<T> transaction){
    try {
      return transaction.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      log.error(String.format("error while creating new instance ,errormessage:%s ", e.getMessage()));
    }
    return null;
  }
}
