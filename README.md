# Third-Party-OIDC-App

## Description:
A small Java web application that is used primarily for the use of interacting with POL services. The testing with this harness focuses on logging in via FIDC.
This is an example application of a third party app gaining an Oauth token.
The application uses OIDC protocol to authenticate the user.

### Requirements:
- [Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

### Usage:
* In application.properties file changes need to be made to environment variables in order to get the application to work locally.
* The authorise-uri, token-uri and user-uri must be changed according to the location of the IDP.
* Entrypoint for local testing is http://localhost:8090/login
* This test harness is built and tested for local use and is not intended to be run in production

### Docker Usage:
* `docker build -t test-harness .`
* `docker run --rm -it --name test-harness --env-file .env -p 8090:8090 test-harness`
* Entrypoint is http://localhost:8090/login

### Environment Variables:

Environment variables can be set in the `.env` file for running in Docker.

| Variable      | Description                                             | Example                                                                                |
| ------------- | ------------------------------------------------------- | -------------------------------------------------------------------------------------- |
| CLIENT_ID     | A value you set yourself within a Mongo collection      | THIRDPARTYCLIENT                                                                       |
| CLIENT_SECRET | A value you set yourself within a Mongo collection      | CLIENTSECRET                                                                           |
| REDIRECT_URI  | The redirect URI after you've finished your CHS journey | `http://localhost:8090/redirect`                                                       |
| TOKEN_URI     | The token URI for CHS live                              | `https://idam.amido.aws.chdev.org:443/am/oauth2/realms/root/realms/alpha/access_token` |
| USER_URI      | The user URI for CHS live                               | `https://idam.amido.aws.chdev.org:443/am/oauth2/realms/root/realms/alpha/userinfo`     |
| AUTHORISE_URI | The authorise URI for CHS live                          | `https://idam.amido.aws.chdev.org:443/am/oauth2/realms/root/realms/alpha/authorize`    |
