package ru.tsu.hits.core_service.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.tsu.hits.core_service.model.Transaction;

@Controller
public class TransactionWebSocketController {

    @MessageMapping("/transaction")
    @SendTo("/topic/transactions")
    public Transaction sendTransactionUpdate(Transaction transaction) {
        System.out.println(transaction);
        return transaction;
    }
}

