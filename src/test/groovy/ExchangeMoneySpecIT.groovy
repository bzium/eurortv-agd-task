import io.restassured.RestAssured
import io.restassured.http.ContentType
import pl.plagodzinski.eurortvagdtask.currencyexchange.ExchangeRequestDTO
import pl.plagodzinski.eurortvagdtask.user.dto.RegisterUserDTO
import spock.lang.Shared
import spock.lang.Specification

import static io.restassured.RestAssured.given

class ExchangeMoneySpecIT extends Specification {


    @Shared
    def DEFAULT_PESEL = "87041822541"
    @Shared
    def DEFAULT_SURNAME = "Lagodziński"
    @Shared
    def DEFAULT_NAME = "Pawel"
    @Shared
    def DEFAULT_FROM_CURRENCY_CODE = "PLN"
    @Shared
    def DEFAULT_TO_CURRENCY_CODE = "USD"
    @Shared
    def EXCHANGE_MONEY_ENDPOINT = "/money/exchange"

    def setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = 8080

    }

    def register_user_with_amount() {
        given()
                .body(new RegisterUserDTO(DEFAULT_NAME, DEFAULT_SURNAME, DEFAULT_PESEL, 100.0))
                .contentType(ContentType.JSON)
                .when()
                .post("/user/register")
                .then().statusCode(200)
    }

    def "Registered user exchange 10 PLN to USD"() {
        given:
        register_user_with_amount()
        expect:
        def exchangeMoneyDTO = new ExchangeRequestDTO(DEFAULT_PESEL, 10.0, DEFAULT_FROM_CURRENCY_CODE, DEFAULT_TO_CURRENCY_CODE)
        given()
                .body(exchangeMoneyDTO)
                .contentType(ContentType.JSON)
                .when()
                .post(EXCHANGE_MONEY_ENDPOINT)
                .then()
                .statusCode(200)
    }

    def "Registered user has Insufficient Funds"() {
        expect:
        def exchangeMoneyDTO = new ExchangeRequestDTO(DEFAULT_PESEL, 150.0, DEFAULT_FROM_CURRENCY_CODE, DEFAULT_TO_CURRENCY_CODE)
        given()
                .body(exchangeMoneyDTO)
                .contentType(ContentType.JSON)
                .when()
                .post(EXCHANGE_MONEY_ENDPOINT)
                .then()
                .statusCode(400)
    }
}
