package com.kota101.innstant.data.repository;

import com.kota101.innstant.data.model.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Mono<Transaction> findBy_id(ObjectId _id);
}
