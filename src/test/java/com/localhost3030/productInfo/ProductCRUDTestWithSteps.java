package com.localhost3030.productInfo;

import com.localhost3030.productinfo.ProductSteps;
import com.localhost3030.testbase.TestBase;
import com.localhost3030.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;


@RunWith(SerenityRunner.class)
public class ProductCRUDTestWithSteps extends TestBase {
    static String name = TestUtils.getRandomValue()+"Duracell - AA Batteries (8-Pack)";
    static String type = TestUtils.getRandomValue()+"HardGood";
    static float price = 7.49f;
    static String upc = "041333825014";
    static int shipping = 1;
    static String description = "Compatible with select electronic devices; AA size; DURALOCK Power Preserve technology; 8-pack";
    static String manufacturer = "Duracell";
    static String model = "MN1500B8Z";

    static int productId; //productId - from EndPoint

    @Steps
    ProductSteps productSteps;

    @Title("This will get list of Products")
    @Test
    public void test001 (){
        productSteps.getAllProductsinfo().log().all().statusCode(200);
    }

    @Title("This will create a new product")
    @Test
    public void test002 (){
        ValidatableResponse response = productSteps.createProduct(name, type, price,upc,shipping,description,manufacturer,model);
        response.log().all().statusCode(201);
    }
    @Title("Verify if product is created")
    @Test
    public void test003 (){
        HashMap<String, Object> productMap = productSteps.getProductinfoByName(name);
        Assert.assertThat(productMap, hasValue(name));
        productId = (int) productMap.get("id");
        System.out.println(productId);
    }

    @Title("Update the product and verify the updated info")
    @Test
    public void test004 (){
        name = name + "updated";
        productSteps.updateProduct(productId, name,type, price, upc, shipping, description, manufacturer, model);
        HashMap<String, Object> productMap = productSteps.getProductinfoByName(name);
        Assert.assertThat(productMap, hasValue(name));
    }
    @Title("Delete product info by ProductId and verify its deleted")
    @Test
    public void test005 (){
        productSteps.deleteProductInfoById(productId).log().all().statusCode(200);
        productSteps.getProductInfoByProductId(productId).log().all().statusCode(404);
    }
}
