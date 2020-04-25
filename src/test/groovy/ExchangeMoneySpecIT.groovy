import io.restassured.RestAssured
import io.restassured.http.ContentType
import pl.plagodzinski.eurortvagdtask.currencyexchange.ExchangeRequestDTO
import pl.plagodzinski.eurortvagdtask.user.dto.RegisterUserDTO
import spock.lang.Specification

import static io.restassured.RestAssured.given

class ExchangeMoneySpecIT extends Specification {

    def setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = 8080

    }

    def register_user_with_amount() {
        given()
                .body(new RegisterUserDTO("Pawel", "Lagodziński", "87041822541", 100.0))
                .contentType(ContentType.JSON)
                .when()
                .post("/user/register")
                .then().statusCode(200)
    }

    def "Registered user exchange 10 PLN to USD"() {
        given:
        register_user_with_amount()
        expect:
        def exchangeMoneyDTO = new ExchangeRequestDTO("87041822541", 10.0, "PLN", "USD")
        given()
                .body(exchangeMoneyDTO)
                .contentType(ContentType.JSON)
                .when()
                .post("/money/exchange")
                .then()
                .statusCode(200)
    }

    def "Registered user has Insufficient Funds"() {
        expect:
        def exchangeMoneyDTO = new ExchangeRequestDTO("87041822541", 150.0, "PLN", "USD")
        given()
                .body(exchangeMoneyDTO)
                .contentType(ContentType.JSON)
                .when()
                .post("/money/exchange")
                .then()
                .statusCode(400)
    }
}
