package ru.tsu.hits.core_service.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.core_service.dto.SubscriptionDto;

import java.util.Collections;

@RestController
public class FirebaseSubscriptionController {

    @PostMapping("/subscribe")
    public String subscribeToTopic(@RequestBody SubscriptionDto dto) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(Collections.singletonList(dto.getToken()), dto.getTopic());
            return "Subscribed to " + dto.getTopic();
        } catch (Exception e) {
            return "Subscription failed: " + e.getMessage();
        }
    }

    @PostMapping("/unsubscribe")
    public String unsubscribeFromTopic(@RequestParam String token, @RequestParam String topic) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Collections.singletonList(token), topic);
            return "Unsubscribed from " + topic;
        } catch (Exception e) {
            return "Unsubscription failed: " + e.getMessage();
        }
    }
}
