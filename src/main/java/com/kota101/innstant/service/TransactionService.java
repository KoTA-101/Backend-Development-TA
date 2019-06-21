package com.kota101.innstant.service;

import com.kota101.innstant.data.model.Transaction;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Flux<Transaction> getTransactions();

    Mono<Transaction> getTransactionById(ObjectId transactionId);

    Mono<Transaction> createTransaction(Transaction transaction);

    Mono<Transaction> updateTransaction(ObjectId transactionId, Transaction transaction);

    Mono<Transaction> modifyTransactionPaymentStatus(ObjectId transactionId, String paymentStatus);

    Mono<Transaction> deleteTransaction(ObjectId transactionId);
}
