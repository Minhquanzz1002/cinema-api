package vn.edu.iuh.exceptions;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.edu.iuh.dto.res.ErrorResponse;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        ErrorResponse<String> errorResponse = new ErrorResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "error",
                "Truyền giá trị không hợp lệ",
                "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại định dạng JSON của bạn."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        FieldError firstFieldError = ex.getBindingResult().getFieldErrors().get(0);

        String errorMessage = String.format("%s (Giá trị không hợp lệ: %s: %s)",
                firstFieldError.getDefaultMessage(),
                firstFieldError.getField(),
                firstFieldError.getRejectedValue());

        ErrorResponse<String> errorResponse = new ErrorResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "error",
                "Truyền giá trị không hợp lệ",
                errorMessage
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<String> handleMethodArgumentTypeMismatchException(ValidationException exception) {
        return new ErrorResponse<>(
                400,
                "error",
                "Truyền giá trị không hợp lệ",
                exception.getMessage()
        );
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
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
