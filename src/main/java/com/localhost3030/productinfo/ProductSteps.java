package com.localhost3030.productinfo;

import com.localhost3030.constants.EndPoints;
import com.localhost3030.model.ProductPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;

public class ProductSteps {

    @Step ("Getting all product info: {0}")
    public ValidatableResponse getAllProductsinfo  (){
        return SerenityRest.given()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then();

    }

    @Step("Creating product with name:{0}, type:{1}, price:{2}, upc:{3}, shipping:{4}, description:{5}, manufacturer:{6}, model:{7}")
    public ValidatableResponse createProduct(String name, String type, Float price, String upc, int shipping, String description, String manufacturer, String model) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setType(type);
        productPojo.setPrice(price);
        productPojo.setUpc(upc);
        productPojo.setShipping(shipping);
        productPojo.setDescription(description);
        productPojo.setManufacturer(manufacturer);
        productPojo.setModel(model);
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(productPojo)
                .when()
                .post()
                .then();//.log().all().statusCode(201);
    }
    @Step ("getting product info by name: {0}")
    public HashMap<String, Object> getProductinfoByName(String name) {
        String p1 = "data.findAll{it.name='";
        String p2 ="'}.get(0)";
        return SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then()
                .statusCode(200)
                .extract()
                .path(p1+name+p2);
         }
    @Step ("update product info with productId:{0}, name:{1}, type:{2}, price:{3}, upc:{4}, shipping:{5}, description:{6}, manufacturer:{7}, model:{8}")
    public ValidatableResponse updateProduct (int productId, String name, String type, float price, String upc, int shipping, String description, String manufacturer, String model){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setType(type);
        productPojo.setPrice(price);
        productPojo.setUpc(upc);
        productPojo.setShipping(shipping);
        productPojo.setDescription(description);
        productPojo.setManufacturer(manufacturer);
        productPojo.setModel(model);
        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json; charset=UTF-8")
                .pathParam("productId", productId)
                .body(productPojo)
                .when()
                .put(EndPoints.UPDATE_PRODUCT_BY_ID)
                .then();
    // don't need assert here again as its same as above created steps
    }
    @Step ("delete product info with productId: {0}")
    public ValidatableResponse deleteProductInfoById (int productId){
        return SerenityRest.given()
                .pathParam("productId", productId)
                .when()
                .delete(EndPoints.DELETE_PRODUCT_BY_ID)
                .then();
    }
    @Step ("getting product info by productId: {0}")
    public ValidatableResponse getProductInfoByProductId (int productId){
        return SerenityRest.given()
                .pathParam("productId", productId)
                .when()
                .get(EndPoints.GET_SINGLE_PRODUCT_BY_ID)
                .then();
    }
}
