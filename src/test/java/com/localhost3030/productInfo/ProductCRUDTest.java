package com.localhost3030.productInfo;

import com.localhost3030.constants.EndPoints;
import com.localhost3030.model.ProductPojo;
import com.localhost3030.testbase.TestBase;
import com.localhost3030.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class ProductCRUDTest extends TestBase {

    static String name = TestUtils.getRandomValue() +"Duracell - AA Batteries (8-Pack)" ;
    static String type = TestUtils.getRandomValue() +"HardGood";
    static float price = 7.49f;
    static String upc = "041333825014";
    static int shipping = 1;
    static String description = "Compatible with select electronic devices; AA size; DURALOCK Power Preserve technology; 8-pack";
    static String manufacturer = "Duracell";
    static String model = "MN1500B8Z";

    static int productId;  //productId - from EndPoint




    @Title("This will give list of Products")
    @Test
    public void test001 (){
        SerenityRest.given()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Title("This will create a new prodct")
    @Test
    public void test002 (){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setType(type);
        productPojo.setPrice(price);
        productPojo.setUpc(upc);
        productPojo.setShipping(shipping);
        productPojo.setDescription(description);
        productPojo.setManufacturer(manufacturer);
        productPojo.setModel(model);

        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(productPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);
    //    System.out.println("test002 product id is - " +id);

//        Response response = given()
//                .header("Content-Type", "application/json") // get info from Postman Headers key and value
//                .body(productPojo)
//                .when()
//                .post();
//        response.then().statusCode(201);
//        response.prettyPrint();
//        id= response.body().path("id");
//        System.out.println(id);
    }

    @Title("Verify if product is created")
    @Test
    public void test003 (){
        String p1 = "data.findAll{it.name='";
        String p2 ="'}.get(0)";
     //   System.out.println("First" +p1+name+p2);
        HashMap<String, Object> productMap = SerenityRest.given().log().all()
   //     HashMap<String, Object> productMap = given().log().all()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then()
                .statusCode(200)
                .extract()
        //        .path("data.findAll{it.name='"+name+ "'}.get(0)");
           //     .path(name);
                .path(p1+name+p2);
     //   System.out.println("Second"+p1+name+p2);
        Assert.assertThat(productMap, hasValue(name));
        productId = (int) productMap.get("id");
    }

    @Title("Update the product and verify the updated info")
    @Test
    public void test004 (){
        name = name+"updated";

        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setType(type);
        productPojo.setPrice(price);
        productPojo.setUpc(upc);
        productPojo.setShipping(shipping);
        productPojo.setDescription(description);
        productPojo.setManufacturer(manufacturer);
        productPojo.setModel(model);
        SerenityRest.given().log().all()
        //        .contentType(ContentType.JSON)
                .header("Content-Type", "application/json; charset=UTF-8")
                .pathParam("productId", productId)
                .body(productPojo)
                .when()
                .put(EndPoints.UPDATE_PRODUCT_BY_ID)
                .then().log().all().statusCode(200);

        String p1 = "data.findAll{it.name='";
        String p2 = "'}.get(0)";
        HashMap<String, Object> proudctMap = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then()
                .statusCode(200)
                .extract()
                .path(p1+name+p2);
        Assert.assertThat(proudctMap, hasValue(name));

    }

    @Title("Delete the product and verify if the product is deleted")
    @Test
    public void test005 (){
        SerenityRest.given()
                .pathParam("productId", productId)
                .when()
                .delete(EndPoints.DELETE_PRODUCT_BY_ID)
                .then()
                .statusCode(200); // as per swagger normally 204
        given().log().all()
                .pathParam("productId", productId)
                .when()
                .get(EndPoints.GET_SINGLE_PRODUCT_BY_ID)
                .then()
                .statusCode(404);
    }
}
