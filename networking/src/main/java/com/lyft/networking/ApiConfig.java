package com.lyft.networking;

public class ApiConfig {

    private final String clientId;
    private final String clientToken;

    private ApiConfig(String clientId, String clientToken) {
        this.clientId = clientId;
        this.clientToken = clientToken;
    }

    /**
     * @return The clientId associated with a Lyft Developer account.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return Client Token generated on the Lyft Developer page using a registered Developer account.
     */
    public String getClientToken() {
        return clientToken;
    }

    public static class Builder {

        private String clientId;
        private String clientToken;

        /**
         * @param clientId Required. The clientId associated with a Lyft Developer account.
         * See: <a href="https://www.lyft.com/developers">https://www.lyft.com/developers</a>
         */
        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * @param clientToken Required. Generated on the Lyft Developer page using a registered Developer account.
         */
        public Builder setClientToken(String clientToken) {
            this.clientToken = clientToken;
            return this;
        }

        public ApiConfig build() {
            Preconditions.checkNotNull(clientId, "clientId must be set!");
            Preconditions.checkNotNull(clientToken, "clientToken must be set!");
            return new ApiConfig(clientId, clientToken);
        }
    }
}
