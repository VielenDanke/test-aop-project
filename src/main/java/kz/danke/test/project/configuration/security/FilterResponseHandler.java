package kz.danke.test.project.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.danke.test.project.dto.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class FilterResponseHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public FilterResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleFilterException(Throwable e,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      int status) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus(status);
        errorResponse.setError(e.getClass().getSimpleName());
        errorResponse.setMessage(e.getLocalizedMessage());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now().toString());

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
