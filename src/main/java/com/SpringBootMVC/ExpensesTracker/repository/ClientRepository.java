package com.SpringBootMVC.ExpensesTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBootMVC.ExpensesTracker.entity.Client;

// public interface ClientRepository extends JpaRepository<Client, Integer> {

// }

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByEmail(String email);
}
