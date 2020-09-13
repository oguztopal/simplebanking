package com.assignment.simplebanking.util;

public final class ApiPaths{
  private ApiPaths() {
  }
  public static final String BASE_PATH = "/api";


  public static final class AccountApi {
    private AccountApi() {
    }

    public static final String BASE = BASE_PATH + "/account/v1";
    public static final String CREDIT = "/credit";
    public static final String DEBIT = "/debit/";
    public static final String PHONE_BILL_PAYMENT = "/phone-bill-payment/";
    public static final String CREATE_ACCOUNT = "/create";

  }

}
