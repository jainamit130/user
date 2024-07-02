package com.amit.converse.user.service;

import com.amit.converse.common.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
        SendMessageResponse response = SendMessageResponse.newBuilder().setSuccess(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMessages(GetMessagesRequest request, StreamObserver<GetMessagesResponse> responseObserver) {
        GetMessagesResponse response = GetMessagesResponse.newBuilder()
                .addMessages(ChatMessage.newBuilder()
                        .setId("1")
                        .setSenderId("user1")
                        .setReceiverId("user2")
                        .setContent("Hello")
                        .setTimestamp(System.currentTimeMillis())
                        .build()
                ).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
