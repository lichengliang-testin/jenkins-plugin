package cn.testin.plugins.testinpro.bean;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

public class ExecResponse {

    private Throwable throwable;
    private boolean hasError;

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean isHasError() {
        return hasError;
    }

    public static class Builder {

        private ExecResponse execResponse;

        public Builder() {
            execResponse = new ExecResponse();
        }

        public Builder throwable(Throwable throwable) {
            execResponse.throwable = throwable;
            return this;
        }

        public ExecResponse build() {

            if (!isEmpty(execResponse.throwable)) {
                execResponse.hasError = true;
            }

            return execResponse;
        }
    }
}
