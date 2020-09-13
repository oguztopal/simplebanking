package com.assignment.simplebanking.model;


import com.assignment.simplebanking.type.TransactionType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction{

  @Id
  @SequenceGenerator(name = "transaction_seq_generator", sequenceName = "transaction_seq")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq_generator")
  private Long id;

  @Column(name = "date", columnDefinition = "TIMESTAMP", updatable = false)
  private LocalDateTime date=LocalDateTime.now();

  @Column(name = "amount")
  private Double amount;

  @ManyToOne(fetch = FetchType.LAZY)
  private Account account;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  protected TransactionType type;


  @Type(type="org.hibernate.type.UUIDCharType")
  @Column(name = "approval_code")
  private UUID approvalCode;


 /* @PrePersist
  public void onPrePersist() {
    setDate(LocalDateTime.now());
  }*/

  @Override public String toString(){
    return "Transaction{" + "date=" + date + ", amount=" + amount + ", type=" + type + ", approvalCode=" + approvalCode + '}';
  }

  public Double calculateBalance(Double amount){
    return null;
  }

  public Transaction(Double amount){
    this.amount = amount;
  }
}
