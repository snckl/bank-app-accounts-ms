package com.cnsn.accounts.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class Accounts extends BaseEntity {
    @Id // I am not doing autoGeneration cuz I will handle it.
    private long accountNumber;
    private Long customerId;
    private String accountType;
    private String branchAddress;
}
