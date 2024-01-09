package playground.bestbuy.crudTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;
import playground.bestbuy.model.ProductsPojo;
import playground.bestbuy.path.EndPoints;
import playground.bestbuy.utils.TestUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ProductsCRUD {

    RequestSpecification requestSpecification;
    Response response;
    ValidatableResponse validatableResponse;

    int productId;
    static String name = "Parle" + TestUtils.getRandomValue();
    static String type = "Parle" + TestUtils.getRandomValue();
    static int price = 10;
    static int shipping = 5;
    static String upc = "dpd" + TestUtils.getRandomValue();
    static String description = "Best Biscuit" + TestUtils.getRandomValue();
    static String manufacturer = "Parle" + TestUtils.getRandomValue();
    static String model = "1969" + TestUtils.getRandomValue();
    static String url = "String" + TestUtils.getRandomValue();
    static String image = "String" + TestUtils.getRandomValue();


    @Test(priority = 1)
    public void getAllProducts() {

        response = given().log().all()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS);
        response.then().log().all()
                .statusCode(200);

    }

    @Test(priority = 2)
    public void postNewProduct() {

        ProductsPojo productsPojo = new ProductsPojo();
        productsPojo.setName(name);
        productsPojo.setType(type);
        productsPojo.setPrice(price);
        productsPojo.setShipping(shipping);
        productsPojo.setUpc(upc);
        productsPojo.setDescription(description);
        productsPojo.setManufacturer(manufacturer);
        productsPojo.setModel(model);
        productsPojo.setUrl(url);
        productsPojo.setImage(image);

        response = given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(productsPojo)
                .post(EndPoints.GET_ALL_PRODUCTS);
        response.then().log().all().statusCode(201)
                .body("name", equalTo(name))
                .body("type", equalTo(type));
        productId = response.path("id");
        System.out.println(productId);
    }

    @Test(priority = 3)
    public void getSingleProductInfo() {

        response = RestAssured.given().log().all()
                .when()
                .pathParams("id", productId)
                .get(EndPoints.GET_BY_ID);
        response.then().log().all().statusCode(200)
                .body("name", equalTo(name))
                .body("type", equalTo(type));

    }


    @Test(priority = 4)
    public void patchProductById() {

        ProductsPojo productsPojo = new ProductsPojo();
        productsPojo.setType(type + "zz");

        response = given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .pathParams("id", productId)
                .body(productsPojo)
                .patch(EndPoints.GET_BY_ID);
        response.then().log().all().statusCode(200)
                .body("type", equalTo(type + "zz"));

    }

    @Test(priority = 5)
    public void getUpdatedSingleProductInfo() {

        response = RestAssured.given().log().all()
                .when()
                .pathParams("id", productId)
                .get(EndPoints.GET_BY_ID);
        response.then().log().all().statusCode(200)
                .body("type", equalTo(type + "zz"));

    }

    @Test(priority = 6)
    public void verifyNewProductInfoDeletedByID() {
        response = given().log().all()
                .when()
                .pathParams("id", productId)
                .delete(EndPoints.GET_BY_ID);
        response.then().log().all()
                .statusCode(200);
    }

    @Test(priority = 7)
    public void verifyProductInfoDeleted() {
        response = given().log().all()
                .when()
                .pathParams("id", productId)
                .get(EndPoints.GET_BY_ID);
        response.then().log().all()
                .statusCode(404);
    }

}
