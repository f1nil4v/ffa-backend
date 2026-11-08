package com.ffa.tournaments.identity.application.port;

public interface EmailSender {

    void send(String to, String subject, String text);
}