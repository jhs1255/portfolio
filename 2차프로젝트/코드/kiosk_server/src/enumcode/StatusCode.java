package enumcode;

import lombok.Getter;

@Getter
public enum StatusCode {
        SUCCESS(201),
        BAD_REQUEST(400),
        INTERNAL_ERROR(501),
        NOT_FOUND(404);

        private final int statusCode;
        StatusCode(int i) {
            this.statusCode = i;
        }

    public int getStatusCode() {
        return statusCode;
    }
}
