package vn.edu.iuh.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.edu.iuh.dto.res.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {BadRequestException.class, OTPMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleBadRequestException(RuntimeException exception) {
        return new ErrorResponse<>(
                500,
                "error",
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(value = {InternalServerErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse<?> handleBadCredentialsException() {
        return new ErrorResponse<>(
                500,
                "error",
                "Đã có lỗi xảy ra. Hãy thử lại sau",
                null
        );
    }

    @ExceptionHandler({DataNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse<?> handleDataNotFoundException(DataNotFoundException exception) {
        return new ErrorResponse<>(
                500,
                "error",
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(value = {UnauthorizedException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse<?> handleBadCredentialsException(RuntimeException exception) {
        return new ErrorResponse<>(
                401,
                "error",
                exception.getMessage(),
                null
        );
    }
}
