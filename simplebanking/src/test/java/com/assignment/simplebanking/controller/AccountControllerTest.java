package com.assignment.simplebanking.controller;

import com.assignment.simplebanking.SimplebankingApplication;
import com.assignment.simplebanking.model.*;
import com.assignment.simplebanking.model.dto.AccountDto;
import com.assignment.simplebanking.repository.AccountRepository;
import com.assignment.simplebanking.services.AccountService;
import com.assignment.simplebanking.services.impl.AccountServiceImpl;
import com.assignment.simplebanking.util.ApiPaths;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ SimplebankingApplication.class })
public class AccountControllerTest{

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @InjectMocks
  private AccountController controller;



  @Autowired
  private EntityManager entityManager;

  @Mock
  private AccountServiceImpl accountService;

  @Autowired
  private AccountRepository accountRepository;

  @Before
  public void setUp() throws Exception{
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getAccount() throws Exception{
    final String acountNumber = "1234";
    Account account = createAccount(acountNumber,"Oguz Topal",1000.0);
    when(accountService.findAccount(account.getAccountNumber().iterator().next())).thenReturn(account);

    mockMvc.perform(get(ApiPaths.AccountApi.BASE+ "/" +acountNumber).accept(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.accountNumber").value(acountNumber))
        .andExpect(jsonPath("$.owner").value("Oguz Topal"))
        .andExpect(jsonPath("$.balance").value(1000.0));

  }

  @Test
  public void deposit() throws Exception{
    final String accountNumber = "669-7788";
    Account account = accountRepository.getByAccountNumber(accountNumber);
    doReturn(account).when(accountService).findAccount(accountNumber);

    Transaction transaction = new DepositTransaction();
    transaction.setAmount(100.0);
    transaction.setDate(LocalDateTime.now());

    mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.AccountApi.BASE+ApiPaths.AccountApi.CREDIT +"/"+accountNumber).content(asJsonString(transaction))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(jsonPath("$.approvalCode").exists())
        .andExpect(status().isOk())
        .andReturn();
    Account result = accountRepository.getByAccountNumber(accountNumber);
    assertEquals(result.getBalance(),account.getBalance()+100.0,0.001);


  }

  @Test
  public void credit() throws Exception{
    final String accountNumber = "669-7788";
    Account account = accountRepository.getByAccountNumber(accountNumber);
    doReturn(account).when(accountService).findAccount(accountNumber);

    Transaction transaction = new WithdrawalTransaction();
    transaction.setAmount(1000.0);
    transaction.setDate(LocalDateTime.now());

    mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.AccountApi.BASE+ApiPaths.AccountApi.DEBIT +"/"+accountNumber).content(asJsonString(transaction))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(jsonPath("$.approvalCode").exists())
        .andExpect(status().isOk())
        .andReturn();

    Account result = accountRepository.getByAccountNumber(accountNumber);
    assertEquals(result.getBalance(),account.getBalance()-1000.0,0.001);

  }

  @Test
  public void phoneBillPayment() throws Exception{

    final String accountNumber = "669-7788";
    Account account = accountRepository.getByAccountNumber(accountNumber);
    doReturn(account).when(accountService).findAccount(accountNumber);

    Transaction transaction = new PhoneBillPaymentTransaction();
    transaction.setAmount(1000.0);
    transaction.setDate(LocalDateTime.now());

    mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.AccountApi.BASE+ApiPaths.AccountApi.PHONE_BILL_PAYMENT +"/"+accountNumber).content(asJsonString(transaction))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(jsonPath("$.approvalCode").exists())
        .andExpect(status().isOk())
        .andReturn();

    Account result = accountRepository.getByAccountNumber(accountNumber);
    assertEquals(result.getBalance(),account.getBalance()-1000.0,0.001);
  }
  @Test
  public void createAccount() throws Exception{
    Account account = createAccount("11111","Oguz Topal", 5000.0);

    mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.AccountApi.BASE+ApiPaths.AccountApi.CREATE_ACCOUNT).content(asJsonString(account))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

  }

  private Account createAccount(String acountNumber,String owner , Double balance){
    Set<String> stringSet = new HashSet<>();
    stringSet.add(acountNumber);
    Account account = new Account(stringSet,owner,balance);
    return account;
  }

  public static String asJsonString(final Object object){
    try{
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JSR310Module());
      objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
      return objectMapper.writeValueAsString(object);
    }catch (Exception ex){
      throw new RuntimeException(ex);
    }
  }
}