package com.amit.converse.user.service;

import com.amit.converse.common.SendMessageRequest;
import com.amit.converse.common.SendMessageResponse;
import com.amit.converse.common.User;
import com.amit.converse.common.UserServiceGrpc;
import com.amit.converse.user.dto.UserEventDTO;
import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SendUserToChatService  {
    private final UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    @Transactional
    public boolean createUser(UserEventDTO userEventDTO) {
        // Prepare the user details to send to Chat Microservice
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setUser(User.newBuilder()
                        .setUserId(userEventDTO.getUserId())
                        .setUsername(userEventDTO.getUsername())
                        .setCreationDate(Timestamp.newBuilder()
                                .setSeconds(userEventDTO.getCreationDate().getEpochSecond())
                                .setNanos(userEventDTO.getCreationDate().getNano())
                                .build())
                        .build())
                .build();

        // Send the user details to Chat Microservice
        SendMessageResponse response = userServiceStub.sendMessage(request);
        return response.getSuccess();
    }
}

