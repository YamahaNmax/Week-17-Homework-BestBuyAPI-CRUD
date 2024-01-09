package playground.bestbuy.crudTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;
import playground.bestbuy.model.StoresPojo;
import playground.bestbuy.path.EndPoints;
import playground.bestbuy.utils.TestUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class StoresCRUD {
    RequestSpecification requestSpecification;
    Response response;
    ValidatableResponse validatableResponse;

    int storeId;
    static String name = "less" + TestUtils.getRandomValue();
    static String type = "Grocery" + TestUtils.getRandomValue();
    static String address = "Islington" + TestUtils.getRandomValue();
    static String address2 = "Highbury" + TestUtils.getRandomValue();
    static String city = "London" + TestUtils.getRandomValue();
    static String state = "Middlesex" + TestUtils.getRandomValue();
    static String zip = "HA5" + TestUtils.getRandomValue();
    static int lat = 50;
    static int lng = 40;
    static String hours = "38" + TestUtils.getRandomValue();

    @Test(priority = 1)
    public void getAllStores() {
        response = given().log().all()
                .when()
                .get(EndPoints.GET_ALL_STORES);
        response.then().log().all().statusCode(200);

    }

    @Test(priority = 2)
    public void createNewStore() {

        StoresPojo storesPojo = new StoresPojo();
        storesPojo.setName(name);
        storesPojo.setType(type);
        storesPojo.setAddress(address);
        storesPojo.setAddress2(address2);
        storesPojo.setCity(city);
        storesPojo.setState(state);
        storesPojo.setZip(zip);
        storesPojo.setLat(lat);
        storesPojo.setLng(lng);
        storesPojo.setHours(hours);

        response = given().log().all()
                .contentType(ContentType.JSON)
                .body(storesPojo)
                .post("http://localhost:3030/stores");
        response.then().log().all()
                .statusCode(201)
                .body("name", equalTo(name))
                .body("type", equalTo(type));
        storeId = response.path("id");
        System.out.println(storeId);

    }

    @Test(priority = 3)
    public void getSingleStoreInfo() {

        response = RestAssured.given().log().all()
                .when()
                .pathParams("id", storeId)
                .get(EndPoints.GET_STORE_BY_ID);
        response.then().log().all().statusCode(200)
                .body("name", equalTo(name))
                .body("type", equalTo(type));

    }

    @Test(priority = 4)
    public void patchProductById() {

        StoresPojo storesPojo = new StoresPojo();
        storesPojo.setType(type + "zz");

        response = given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .pathParams("id", storeId)
                .body(storesPojo)
                .patch(EndPoints.GET_STORE_BY_ID);
        response.then().log().all().statusCode(200)
                .body("type", equalTo(type + "zz"));

    }

    @Test(priority = 5)
    public void getUpdatedSingleProductInfo() {

        response = RestAssured.given().log().all()
                .when()
                .pathParams("id", storeId)
                .get(EndPoints.GET_STORE_BY_ID);
        response.then().log().all().statusCode(200)
                .body("type", equalTo(type + "zz"));

    }

    @Test(priority = 6)
    public void verifyNewProductInfoDeletedByID() {
        response = given().log().all()
                .when()
                .pathParams("id", storeId)
                .delete(EndPoints.GET_STORE_BY_ID);
        response.then().log().all()
                .statusCode(200);
    }

    @Test(priority = 7)
    public void verifyProductInfoDeleted() {
        response = given().log().all()
                .when()
                .pathParams("id", storeId)
                .get(EndPoints.GET_STORE_BY_ID);
        response.then().log().all()
                .statusCode(404);
    }

}

