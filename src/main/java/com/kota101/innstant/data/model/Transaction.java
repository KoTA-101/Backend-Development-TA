package com.kota101.innstant.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transaction")
public class Transaction {
    @Id
    @Field("_id")
    private ObjectId _id;

    @Field("transaction_id")
    private String transactionId;

    @Field("host_id")
    private String hostId;

    @Field("guest_id")
    private String guestId;

    @Field("room_id")
    private String roomId;

    @Field("transaction_timestamp")
    private Date transactionTimestamp;

    @Field("book_start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date bookStartDate;

    @Field("book_end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date bookEndDate;

    @Field("payment_status")
    private String paymentStatus;

    @Field("is_booking_canceled")
    private Boolean isBookingCanceled;
}
