package com.kota101.innstant.service.implementation;

import com.kota101.innstant.data.model.Transaction;
import com.kota101.innstant.data.repository.TransactionRepository;
import com.kota101.innstant.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransactionServiceImplementation implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Flux<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Mono<Transaction> getTransactionById(ObjectId transactionId) {
        return transactionRepository.findBy_id(transactionId).switchIfEmpty(Mono.error(new Exception("No transaction found with ID: " + transactionId)));
    }

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        transaction.setTransactionTimestamp(Timestamp.from(Instant.now()));
        return transactionRepository.insert(transaction);
    }

    @Override
    public Mono<Transaction> updateTransaction(ObjectId transactionId, Transaction transaction) {
        return transactionRepository.findBy_id(transactionId).doOnSuccess(findTransaction -> {
            findTransaction.setHostId(transaction.getHostId());
            findTransaction.setGuestId(transaction.getGuestId());
            findTransaction.setRoomId(transaction.getRoomId());
            findTransaction.setTransactionTimestamp(transaction.getTransactionTimestamp());
            findTransaction.setBookStartDate(transaction.getBookStartDate());
            findTransaction.setBookEndDate(transaction.getBookEndDate());
            findTransaction.setPaymentStatus(transaction.getPaymentStatus());
            transactionRepository.save(findTransaction).subscribe();
        });
    }

    @Override
    public Mono<Transaction> modifyTransactionPaymentStatus(ObjectId transactionId, String paymentStatus) {
        return transactionRepository.findBy_id(transactionId).doOnSuccess(findTransaction -> {
            try {
                findTransaction.setPaymentStatus(new JSONObject(paymentStatus).getString("paymentStatus"));
                transactionRepository.save(findTransaction).subscribe();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Mono<Transaction> deleteTransaction(ObjectId transactionId) {
        return getTransactionById(transactionId).doOnSuccess(transaction -> transactionRepository.delete(transaction).subscribe());
    }
}
