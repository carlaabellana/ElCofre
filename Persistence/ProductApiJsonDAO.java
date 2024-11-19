package persistence;

import business.product.GeneralProduct;
import business.product.Product;
import business.product.ReducedProduct;
import business.product.SuperReducedProduct;
import business.shop.Shop;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
/**
 * Gestiona operaciones CRUD de productos con una API remota, serializando y deserializando datos JSON.
 */
public class ProductApiJsonDAO {
    private final ApiHelper apiHelper;
    private final Gson gson;
    private final String baseUrl = "https://balandrau.salle.url.edu/dpoo";
    /**
     * Constructor. Inicializa el ayudante de API y el analizador Gson.
     */
    public ProductApiJsonDAO() {
        this.apiHelper = new ApiHelper();
        this.gson = new Gson();
    }
    /**
     * Guarda un producto en la API.
     * @param product Producto a guardar.
     * @throws ApiException Si hay un problema relacionado con la API.
     */
    public void saveProduct(Product product) throws ApiException{
        String jsonProduct = gson.toJson(product);
        String endpoint = "/P1-G70/products";
        try{
            apiHelper.postToUrl(baseUrl + endpoint, jsonProduct);
        }catch (ApiException e){
            apiHelper.deleteFromUrl("https://balandrau.salle.url.edu/dpoo/P1-G70/shops/4");
            throw new ApiException("ERROR: API: " + e.getMessage(), e, baseUrl + endpoint);
        }
    }

    /**
     * Carga los productos desde la API.
     * @return Representación en cadena JSON de los productos.
     * @throws ApiException Si hay un problema relacionado con la API.
     * @throws JsonSyntaxException Si falla el análisis de JSON.
     */
    public ArrayList<Product> loadProducts() throws ApiException, JsonSyntaxException {
        String endpoint = ("/P1-G70/products");
        String response = apiHelper.getFromUrl(baseUrl + endpoint);
        ArrayList<Product> products = convertJsonToProducts(response);
        return products;
    }
    /**
     * Elimina un producto específico basado en ID y productId de la API.
     * @param id Identificador general para un grupo de productos.
     * @param productId Identificador específico para el producto a eliminar.
     * @throws ApiException Si hay un problema relacionado con la API.
     */
    public void removeProduct(String id, int productId) throws ApiException {
        String endpoint = "/" + id + "/products/" + productId;
        try {
            apiHelper.deleteFromUrl(baseUrl + endpoint);
        } catch (ApiException e) {
            throw new ApiException("ERROR API: " + e.getMessage(), e, baseUrl + endpoint);
        }
    }
    /**
     * Convierte una respuesta JSON en una lista de objetos Product.
     * @param jsonResponse Respuesta JSON a convertir.
     * @return Lista de objetos Product.
     */
    public ArrayList<Product> convertJsonToProducts(String jsonResponse) {
        ArrayList<Product> products = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);

        if (!jsonElement.isJsonArray()) {
            throw new JsonParseException("Response is not a JSON Array");
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            Product product = parseProduct(element);
            if (product != null) {
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Analiza un elemento JSON para crear un objeto Product.
     * @param jsonElement Elemento JSON a analizar.
     * @return Objeto Product o null si el análisis falla.
     */
    private Product parseProduct(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement categoryElement = jsonObject.get("category");

        if (categoryElement == null) {
            throw new JsonParseException("Missing category property in JSON object");
        }
        String category = categoryElement.getAsString();
        switch (category) {
            case "GENERAL":
                return gson.fromJson(jsonObject, GeneralProduct.class);
            case "REDUCED":
                return gson.fromJson(jsonObject, ReducedProduct.class);
            case "SUPER_REDUCED":
                return gson.fromJson(jsonObject, SuperReducedProduct.class);
            default:
                throw new JsonParseException("Invalid category: " + category);
        }
    }

    /**
     * Actualiza un producto en la lista de productos.
     * @param product el producto a actualizar
     * @throws RuntimeException si ocurre un error al cargar, eliminar o guardar el producto
     */
    public void updateProduct(Product product) {

        int index = 0;
        try {
            ArrayList <Product> products = loadProducts();

            for (Product p : products) {
                if (p.getName().equalsIgnoreCase(product.getName())) {
                    break;
                }
                index++;
            }
            removeProduct("P1-G70", index);
            saveProduct(product);

        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }
}
