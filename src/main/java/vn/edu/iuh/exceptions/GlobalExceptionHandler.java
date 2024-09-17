package vn.edu.iuh.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.edu.iuh.dto.res.ErrorResponse;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String name = exception.getName();
        String type = Objects.requireNonNull(exception.getRequiredType()).getSimpleName();
        Object value = exception.getValue();
        String message = String.format("'%s' phải là '%s' và '%s' là không hợp lệ",
                name, type, value);
        log.error(message);
        return new ErrorResponse<>(
                400,
                "error",
                "Truyền giá trị không hợp lệ",
                message
        );
    }

    @ExceptionHandler(value = {BadRequestException.class, OTPMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleBadRequestException(RuntimeException exception) {
        return new ErrorResponse<>(
                400,
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
                404,
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
