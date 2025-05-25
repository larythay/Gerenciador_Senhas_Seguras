package tech.buildrun.springsecurity.controller.dto;

public record EmailDTO(String to, String subject, String body) {
}