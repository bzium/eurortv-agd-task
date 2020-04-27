import io.restassured.RestAssured
import io.restassured.http.ContentType
import pl.plagodzinski.eurortvagdtask.user.dto.RegisterUserDTO
import spock.lang.Shared
import spock.lang.Specification

import static io.restassured.RestAssured.given

class RegisterUserSpecIT extends Specification {

    @Shared
    def BAD_REQEST_STATUS_CODE = 400
    
    @Shared
    def OK_STATUS_CODE = 200

    @Shared
    def DEFAULT_START_AMOUNT = 0.0

    @Shared
    def DEFAULT_NAME = "Pawel"

    @Shared
    def DEFAULT_SURNAME = "Lagodziński"

    @Shared
    def defaultDto = new RegisterUserDTO(DEFAULT_NAME, DEFAULT_SURNAME, "67012885274", DEFAULT_START_AMOUNT)

    @Shared
    def registerEndpoint = "/user/register"

    def setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = 8080
    }

    def "Register user with correct data"() {
        expect:
        given()
                .body(defaultDto)
                .contentType(ContentType.JSON)
                .when()
                .post(registerEndpoint)
                .then()
                .statusCode(OK_STATUS_CODE)
    }

    def "Can't register user with existing pesel"() {
        expect:
        given()
                .body(defaultDto)
                .contentType(ContentType.JSON)
                .when()
                .post(registerEndpoint)
                .then()
                .statusCode(BAD_REQEST_STATUS_CODE)
    }

    def "Can't register user with incorrect pesel"() {
        expect:
        given()
                .body(new RegisterUserDTO(DEFAULT_NAME, DEFAULT_SURNAME, "1234", DEFAULT_START_AMOUNT))
                .contentType(ContentType.JSON)
                .when()
                .post(registerEndpoint)
                .then()
                .statusCode(BAD_REQEST_STATUS_CODE)
    }

    def "Can't register user with incorrect start amount"() {
        expect:
        given()
                .body(new RegisterUserDTO(DEFAULT_NAME, DEFAULT_SURNAME, "85080988918", -1.0))
                .contentType(ContentType.JSON)
                .when()
                .post(registerEndpoint)
                .then()
                .statusCode(BAD_REQEST_STATUS_CODE)
    }

    def "Can't register no adult user"() {
        expect:
        given()
                .body(new RegisterUserDTO(DEFAULT_NAME, DEFAULT_SURNAME, "11221023347", DEFAULT_START_AMOUNT))
                .contentType(ContentType.JSON)
                .when()
                .post(registerEndpoint)
                .then()
                .statusCode(BAD_REQEST_STATUS_CODE)
    }
}
