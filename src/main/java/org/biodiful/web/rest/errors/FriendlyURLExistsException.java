package org.biodiful.web.rest.errors;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;

public class FriendlyURLExistsException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    public FriendlyURLExistsException(String surveyName, String friendlyURL) {
        super(
            HttpStatus.BAD_REQUEST,
            ProblemDetailWithCause.ProblemDetailWithCauseBuilder.instance()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withType(ErrorConstants.DEFAULT_TYPE)
                .withTitle("Friendly URL already used!")
                .withProperty("message", "error.friendlyurlexists")
                .withProperty("params", Map.of("param0", surveyName, "param1", friendlyURL))
                .build(),
            null
        );
    }
}
