package com.ffa.tournaments.shared.presentation;

import com.ffa.tournaments.identity.application.exception.EmailAlreadyRegisteredException;
import com.ffa.tournaments.identity.application.exception.EmailChangeRequestExpiredException;
import com.ffa.tournaments.identity.application.exception.EmailNotVerifiedException;
import com.ffa.tournaments.identity.application.exception.EmailUnchangedException;
import com.ffa.tournaments.identity.application.exception.InvalidCredentialsException;
import com.ffa.tournaments.identity.application.exception.InvalidEmailChangeTokenException;
import com.ffa.tournaments.identity.presentation.dto.response.common.ErrorResponse;

import com.ffa.tournaments.player.application.exception.InvalidNicknameException;
import com.ffa.tournaments.player.application.exception.InvalidSteamOpenIdResponseException;
import com.ffa.tournaments.player.application.exception.NicknameAlreadyTakenException;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotFoundException;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotOwnedException;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotPendingException;
import com.ffa.tournaments.player.application.exception.NicknameUnchangedException;
import com.ffa.tournaments.player.application.exception.PendingNicknameChangeRequestExistsException;
import com.ffa.tournaments.player.application.exception.PlayerProfileAlreadyExistsException;
import com.ffa.tournaments.player.application.exception.PlayerProfileNotFoundException;
import com.ffa.tournaments.player.application.exception.SteamAccountAlreadyLinkedException;
import com.ffa.tournaments.player.application.exception.SteamLinkStateNotFoundException;
import com.ffa.tournaments.player.infrastructure.persistence.exception.DuplicateNicknameException;

import com.ffa.tournaments.support.application.exception.InvalidSupportCredentialsException;

import com.ffa.tournaments.team.application.exception.CannotKickCaptainException;
import com.ffa.tournaments.team.application.exception.CaptainCannotLeaveTeamException;
import com.ffa.tournaments.team.application.exception.InvalidCaptainTransferException;
import com.ffa.tournaments.team.application.exception.NotTeamCaptainException;
import com.ffa.tournaments.team.application.exception.PlayerAlreadyInTeamException;
import com.ffa.tournaments.team.application.exception.PlayerNotInTeamException;
import com.ffa.tournaments.team.application.exception.TeamApplicationAlreadyExistsException;
import com.ffa.tournaments.team.application.exception.TeamApplicationNotFoundException;
import com.ffa.tournaments.team.application.exception.TeamApplicationNotOwnedException;
import com.ffa.tournaments.team.application.exception.TeamApplicationNotPendingException;
import com.ffa.tournaments.team.application.exception.TeamMemberNotFoundException;
import com.ffa.tournaments.team.application.exception.TeamNameAlreadyTakenException;
import com.ffa.tournaments.team.application.exception.TeamNotFoundException;
import com.ffa.tournaments.team.application.exception.TeamRosterFullException;
import com.ffa.tournaments.team.application.exception.TeamTagAlreadyTakenException;

import com.ffa.tournaments.tournament.application.exception.InvalidTeamRosterException;
import com.ffa.tournaments.tournament.application.exception.InvalidTournamentStatusException;
import com.ffa.tournaments.tournament.application.exception.TournamentCapacityExceededException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotEditableException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotOpenForRegistrationException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotFoundException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationAlreadyExistsException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationNotActiveException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationNotFoundException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationNotOwnedException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationNotPendingException;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //identity globalExceptions
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyRegistered() {
        return createResponse(HttpStatus.CONFLICT, "Email is already registered");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials() {
        return createResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotVerified() {
        return createResponse(HttpStatus.FORBIDDEN, "Email address is not verified");
    }

    @ExceptionHandler(EmailUnchangedException.class)
    public ResponseEntity<ErrorResponse> handleEmailUnchanged() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "New email must differ from the current one"
        );
    }

    @ExceptionHandler(InvalidEmailChangeTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmailChangeToken() {
        return createResponse(HttpStatus.BAD_REQUEST, "Invalid email change token");
    }

    @ExceptionHandler(EmailChangeRequestExpiredException.class)
    public ResponseEntity<ErrorResponse> handleEmailChangeRequestExpired() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "Email change request has expired"
        );
    }

    //support globalExceptions
    @ExceptionHandler(InvalidSupportCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSupportCredentials() {
        return createResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    //team globalExceptions
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFound() {
        return createResponse(HttpStatus.NOT_FOUND, "Team not found");
    }

    @ExceptionHandler(TeamNameAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> handleTeamNameAlreadyTaken() {
        return createResponse(HttpStatus.CONFLICT, "Team name is already taken");
    }

    @ExceptionHandler(TeamTagAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> handleTeamTagAlreadyTaken() {
        return createResponse(HttpStatus.CONFLICT, "Team tag is already taken");
    }

    @ExceptionHandler(PlayerAlreadyInTeamException.class)
    public ResponseEntity<ErrorResponse> handlePlayerAlreadyInTeam() {
        return createResponse(HttpStatus.CONFLICT, "Player is already in a team");
    }

    @ExceptionHandler(PlayerNotInTeamException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotInTeam() {
        return createResponse(HttpStatus.NOT_FOUND, "Player is not in a team");
    }

    @ExceptionHandler(TeamRosterFullException.class)
    public ResponseEntity<ErrorResponse> handleTeamRosterFull() {
        return createResponse(HttpStatus.CONFLICT, "Team roster is full");
    }

    @ExceptionHandler(NotTeamCaptainException.class)
    public ResponseEntity<ErrorResponse> handleNotTeamCaptain() {
        return createResponse(
                HttpStatus.FORBIDDEN,
                "Only the team captain can perform this action"
        );
    }

    @ExceptionHandler(TeamMemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamMemberNotFound() {
        return createResponse(HttpStatus.NOT_FOUND, "Team member not found");
    }

    @ExceptionHandler(CaptainCannotLeaveTeamException.class)
    public ResponseEntity<ErrorResponse> handleCaptainCannotLeaveTeam() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Captain cannot leave the team, transfer captaincy or disband the team"
        );
    }

    @ExceptionHandler(CannotKickCaptainException.class)
    public ResponseEntity<ErrorResponse> handleCannotKickCaptain() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "Captain cannot be kicked from the team"
        );
    }

    @ExceptionHandler(InvalidCaptainTransferException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCaptainTransfer() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "Captaincy can only be transferred to another team member"
        );
    }

    @ExceptionHandler(TeamApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamApplicationNotFound() {
        return createResponse(HttpStatus.NOT_FOUND, "Team application not found");
    }

    @ExceptionHandler(TeamApplicationNotPendingException.class)
    public ResponseEntity<ErrorResponse> handleTeamApplicationNotPending() {
        return createResponse(HttpStatus.CONFLICT, "Team application is not pending");
    }

    @ExceptionHandler(TeamApplicationNotOwnedException.class)
    public ResponseEntity<ErrorResponse> handleTeamApplicationNotOwned() {
        return createResponse(
                HttpStatus.FORBIDDEN,
                "Team application does not belong to the current player"
        );
    }

    @ExceptionHandler(TeamApplicationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTeamApplicationAlreadyExists() {
        return createResponse(
                HttpStatus.CONFLICT,
                "A pending application to this team already exists"
        );
    }

    //player globalExceptions
    @ExceptionHandler(NicknameAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> handleNicknameAlreadyTaken() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Nickname is already taken"
        );
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNickname() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Nickname is already taken"
        );
    }

    @ExceptionHandler(PlayerProfileAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePlayerProfileAlreadyExists() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Player profile already exists"
        );
    }

    @ExceptionHandler(InvalidNicknameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNickname() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "Nickname must contain 3-32 characters: Latin letters, numbers, _ or -"
        );
    }

    @ExceptionHandler(NicknameUnchangedException.class)
    public ResponseEntity<ErrorResponse> handleNicknameUnchanged() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "New nickname must differ from the current one"
        );
    }

    @ExceptionHandler(PlayerProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlayerProfileNotFound() {
        return createResponse(HttpStatus.NOT_FOUND, "Player profile not found");
    }

    @ExceptionHandler(NicknameChangeRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNicknameChangeRequestNotFound() {
        return createResponse(HttpStatus.NOT_FOUND, "Nickname change request not found");
    }

    @ExceptionHandler(PendingNicknameChangeRequestExistsException.class)
    public ResponseEntity<ErrorResponse> handlePendingNicknameChangeRequestExists() {
        return createResponse(
                HttpStatus.CONFLICT,
                "A pending nickname change request already exists"
        );
    }

    @ExceptionHandler(NicknameChangeRequestNotPendingException.class)
    public ResponseEntity<ErrorResponse> handleNicknameChangeRequestNotPending() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Nickname change request is not pending"
        );
    }

    @ExceptionHandler(NicknameChangeRequestNotOwnedException.class)
    public ResponseEntity<ErrorResponse> handleNicknameChangeRequestNotOwned() {
        return createResponse(
                HttpStatus.FORBIDDEN,
                "Nickname change request does not belong to the current player"
        );
    }

    @ExceptionHandler(SteamAccountAlreadyLinkedException.class)
    public ResponseEntity<ErrorResponse> handleSteamAccountAlreadyLinked() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Steam account is already linked"
        );
    }

    @ExceptionHandler(SteamLinkStateNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSteamLinkStateNotFound() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "Steam link state not found or expired"
        );
    }

    @ExceptionHandler(InvalidSteamOpenIdResponseException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSteamOpenIdResponse() {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Steam OpenID response"
        );
    }

    //tournament globalExceptions
    @ExceptionHandler(TournamentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTournamentNotFound() {
        return createResponse(HttpStatus.NOT_FOUND, "Tournament not found");
    }

    @ExceptionHandler(TournamentRegistrationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTournamentRegistrationNotFound() {
        return createResponse(
                HttpStatus.NOT_FOUND,
                "Tournament registration not found"
        );
    }

    @ExceptionHandler(TournamentRegistrationNotOwnedException.class)
    public ResponseEntity<ErrorResponse> handleTournamentRegistrationNotOwned() {
        return createResponse(
                HttpStatus.FORBIDDEN,
                "Tournament registration does not belong to the current team"
        );
    }

    @ExceptionHandler(TournamentNotOpenForRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleTournamentNotOpenForRegistration() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Tournament registration is not open"
        );
    }

    @ExceptionHandler(TournamentRegistrationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTournamentRegistrationAlreadyExists() {
        return createResponse(
                HttpStatus.CONFLICT,
                "An active tournament registration for this team already exists"
        );
    }

    @ExceptionHandler(TournamentRegistrationNotPendingException.class)
    public ResponseEntity<ErrorResponse> handleTournamentRegistrationNotPending() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Tournament registration is not pending"
        );
    }

    @ExceptionHandler(TournamentRegistrationNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleTournamentRegistrationNotActive() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Tournament registration is not active"
        );
    }

    @ExceptionHandler(TournamentCapacityExceededException.class)
    public ResponseEntity<ErrorResponse> handleTournamentCapacityExceeded() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Tournament team limit has been reached"
        );
    }

    @ExceptionHandler(TournamentNotEditableException.class)
    public ResponseEntity<ErrorResponse> handleTournamentNotEditable() {
        return createResponse(
                HttpStatus.CONFLICT,
                "Tournament can only be modified in DRAFT status"
        );
    }

    @ExceptionHandler(InvalidTournamentStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTournamentStatus(
            InvalidTournamentStatusException exception
    ) {
        return createResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidTeamRosterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTeamRoster(
            InvalidTeamRosterException exception
    ) {
        return createResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        return createResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessage() {
        return createResponse(HttpStatus.BAD_REQUEST, "Invalid request body");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid value for parameter: " + exception.getName()
        );
    }

    // Временно, пока не добавлены специализированные исключения для токенов.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        log.error("Unexpected error", exception);

        return createResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
    }

    private ResponseEntity<ErrorResponse> createResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );

        return ResponseEntity.status(status).body(error);
    }
}