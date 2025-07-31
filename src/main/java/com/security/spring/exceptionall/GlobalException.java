package com.security.spring.exceptionall;

import com.security.spring.gamesoft.callback.dto.GetBalanceCallBackResponse;
import com.security.spring.gamesoft.callback.dto.PushBetResponse;
import com.security.spring.gamesoft.callback.dto.Response;
import com.security.spring.utils.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import javax.naming.NoPermissionException;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FormatExceptionResponse> argumentValidationException(MethodArgumentNotValidException ex) {

        var response = FormatExceptionResponse.builder()
                .errorCode(16)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<FormatExceptionResponse> validationException(MethodValidationException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(16)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<FormatExceptionResponse> dataNotFoundException(DataNotFoundException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(404)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();

        return ResponseEntity.ok().body(response);
    }
    @ExceptionHandler(ApiDuplicateTransaction.class)
    public ResponseEntity<Response<GetBalanceCallBackResponse>> dataNotFoundException(ApiDuplicateTransaction ex) {
        var callBackResponse = GetBalanceCallBackResponse.builder()
                .code(ResponseCode.DUPLICATE_API_TRANSACTIONS)
                .message(ex.getMessage())
                .build();
        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(List.of(callBackResponse));
        return ResponseEntity.ok().body(response);
    }
    @ExceptionHandler(ApiMemberDoesNotExist.class)
    public ResponseEntity<Response<GetBalanceCallBackResponse>> apiMemberDoesNotExist(ApiMemberDoesNotExist ex){
        var callBackResponse = GetBalanceCallBackResponse.builder()
                .code(ResponseCode.API_MEMBER_DOES_NOT_EXIST)
                .message(ex.getMessage())
                .build();
        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(List.of(callBackResponse));
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(CurrencyDoesNotSupportException.class)
    public ResponseEntity<Response<GetBalanceCallBackResponse>> currencyDoesNotSupportException(CurrencyDoesNotSupportException ex){
        var callBackResponse = GetBalanceCallBackResponse.builder()
                .code(999)
                .message(ex.getMessage())
                .build();
        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(List.of(callBackResponse));
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<FormatExceptionResponse> dataAlreadyExistException(DataAlreadyExistException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(1003)
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }
    @ExceptionHandler(PushBetException.class)
    public ResponseEntity<PushBetResponse> dataAlreadyExistException(PushBetException ex) {
        var response = PushBetResponse.builder()
                .code(ResponseCode.API_MEMBER_DOES_NOT_EXIST)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<FormatExceptionResponse> businessExcepiton(BusinessException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .status(false)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(BetNotExistsException.class)
    public ResponseEntity<Response<GetBalanceCallBackResponse>> betNotExistException(BetNotExistsException ex){
        var callBackResponse = GetBalanceCallBackResponse.builder()
                .code(ResponseCode.API_BET_NOT_EXISTS)
                .message(ex.getMessage())
                .build();
        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(List.of(callBackResponse));
        return ResponseEntity.ok().body(response);
    }


    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Response<GetBalanceCallBackResponse>> insufficientBalanceException(InsufficientBalanceException ex){
        var callBackResponse = GetBalanceCallBackResponse.builder()
                .code(ResponseCode.API_MEMBER_BALANCE_IS_INSUFFICIENT)
                .message(ex.getMessage())
                .build();
        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(List.of(callBackResponse));
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(InvalidWrongSignException.class)
    public ResponseEntity<Response<GetBalanceCallBackResponse>> invalidWrongSignException(InvalidWrongSignException ex){
        var callBackResponse = GetBalanceCallBackResponse.builder()
                .code(ResponseCode.API_SIGNATURE_IS_INVALID)
                .message(ex.getMessage())
                .build();
        Response<GetBalanceCallBackResponse> response = new Response<>();
        response.setData(List.of(callBackResponse));
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<FormatExceptionResponse> noPermissionException(NoPermissionException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(401)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<FormatExceptionResponse> noPermissionException(InvalidException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(401)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(FieldRequireException.class)
    public ResponseEntity<FormatExceptionResponse> fieldRequireException(FieldRequireException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(400)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<FormatExceptionResponse> userNotFoundException(UsernameNotFoundException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(400)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<FormatExceptionResponse> authenticationException(AuthenticationException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(400)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<FormatExceptionResponse> unauthorizedException(UnauthorizedException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(403)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(CustomAlreadyExistException.class)
    public ResponseEntity<FormatExceptionResponse> customAlreadyExist(CustomAlreadyExistException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(401)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<FormatExceptionResponse> indexOutOfBoundsException(IndexOutOfBoundsException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(403)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<FormatExceptionResponse> tokenExpiredException(TokenExpiredException ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(401)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FormatExceptionResponse> RuntimeException(Exception ex) {
        var response = FormatExceptionResponse.builder()
                .errorCode(400)
                .errorMessage(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.ok().body(response);
    }


//    @ExceptionHandler(GameResponseException.class)
//    public ResponseEntity<GameExceptionResponse> gameResponseException(GameResponseException ex) {
////        var response = GameExceptionResponse.builder()
////                .message(ex.getMessage())
////                .statusCode(HttpStatus.BAD_REQUEST.value())
////                .status(false)
////                .build();
//
//        return ResponseEntity.badRequest().body(null);
//    }
}
