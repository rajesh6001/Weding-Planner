package com.weddingplanning.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingplanning.entity.Payment;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Integer> {

}
