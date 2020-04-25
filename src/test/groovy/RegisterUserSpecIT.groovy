import io.restassured.RestAssured
import io.restassured.http.ContentType
import pl.plagodzinski.eurortvagdtask.user.dto.RegisterUserDTO
import spock.lang.Shared
import spock.lang.Specification

import static io.restassured.RestAssured.given

class RegisterUserSpecIT extends Specification {

    @Shared
    def defaultDto = new RegisterUserDTO("Pawel", "Lagodziński","67012885274", 0.0)

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
                .statusCode(200)
    }

    def "Can't register user with existing pesel"() {
        expect:
            given()
                .body(defaultDto)
                .contentType(ContentType.JSON)
                .when()
                .post(registerEndpoint)
                .then()
                .statusCode(400)
    }

    def "Can't register user with incorrect pesel"() {
        expect:
            given()
                    .body(new RegisterUserDTO("Pawel", "Lagodziński","1234", 0.0))
                    .contentType(ContentType.JSON)
                    .when()
                    .post(registerEndpoint)
                    .then()
                    .statusCode(400)
    }

    def "Can't register user with incorrect start amount"() {
        expect:
            given()
                    .body(new RegisterUserDTO("Pawel", "Lagodziński","85080988918", -1.0))
                    .contentType(ContentType.JSON)
                    .when()
                    .post(registerEndpoint)
                    .then()
                    .statusCode(400)
    }

    def "Can't register no adult user"() {
        expect:
        given()
                .body(new RegisterUserDTO("Pawel", "Lagodziński","11221023347", 0.0))
                .contentType(ContentType.JSON)
                .when()
                .post(registerEndpoint)
                .then()
                .statusCode(400)
    }
}
