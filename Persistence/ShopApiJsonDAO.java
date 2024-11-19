package persistence;

import business.shop.LoyaltyShop;
import business.shop.MaxProfitShop;
import business.shop.Shop;
import business.shop.SponsoredShop;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Gestiona operaciones CRUD de tiendas con una API remota, serializando y deserializando datos JSON.
 */
public class ShopApiJsonDAO {
    private ApiHelper apiHelper;
    private Gson gson;
    private final String baseUrl = "https://balandrau.salle.url.edu/dpoo";

    /**
     * Constructor. Inicializa el ayudante de API y el analizador Gson.
     */
    public ShopApiJsonDAO()  {
        this.apiHelper = new ApiHelper();
        this.gson = new Gson();
    }

    /**
     * Guarda una tienda en la API, actualizándola si ya existe.
     * @param shop Tienda a guardar.
     * @throws ApiException Si hay un problema relacionado con la API.
     */
    public void saveShops(Shop shop) throws ApiException {
        String jsonShops = gson.toJson(shop);
        String endpoint = "/P1-G70/shops";

        int position = getShopPosition(shop);
        if (position != -1) {
            deleteExistingShop(position);
        }
        try {
            apiHelper.postToUrl(baseUrl + endpoint, jsonShops);
        } catch (ApiException e) {
            throw new ApiException("ERROR: API: " + e.getMessage(), e, baseUrl + endpoint);
        }
    }

    /**
     * Elimina una tienda existente de la API basada en su posición.
     * @param position Posición de la tienda a eliminar.
     * @throws ApiException Si hay un problema relacionado con la API.
     */
    public void deleteExistingShop(int position) throws ApiException {
        String deleteEndpoint = "/P1-G70/shops/" + position;
        try {
            apiHelper.deleteFromUrl(baseUrl + deleteEndpoint);
        } catch (ApiException e) {
            throw new ApiException("ERROR: Failed to delete existing shop: " + e.getMessage(), e, baseUrl + deleteEndpoint);
        }
    }

    /**
     * Obtiene la posición de una tienda en la lista de tiendas de la API.
     * @param shop Tienda a encontrar.
     * @return Posición de la tienda o -1 si no se encuentra.
     * @throws ApiException Si hay un problema relacionado con la API.
     */
    private int getShopPosition(Shop shop) throws ApiException {
        String endpoint = "/P1-G70/shops";
        try {
            String response = apiHelper.getFromUrl(baseUrl + endpoint);
            JsonArray shopsArray = gson.fromJson(response, JsonArray.class);
            for (int i = 0; i < shopsArray.size(); i++) {
                JsonObject shopObject = shopsArray.get(i).getAsJsonObject();

                if (shopObject.get("name").getAsString().equals(shop.getName())) {

                    return i;
                }
            }
        } catch (ApiException | JsonSyntaxException e) {
            throw new ApiException("ERROR: Failed to retrieve shops: " + e.getMessage(), e, baseUrl + endpoint);
        }
        return -1;
    }

    /**
     * Carga las tiendas desde la API.
     * @return Representación en cadena JSON de las tiendas.
     * @throws ApiException Si hay un problema relacionado con la API.
     * @throws JsonSyntaxException Si falla el análisis de JSON.
     */
    public ArrayList<Shop> loadShops() throws ApiException, JsonSyntaxException {
        String endpoint = ("/P1-G70/shops");
        String response = apiHelper.getFromUrl(baseUrl + endpoint);
        ArrayList<Shop> shops = convertJsonToShops(response);

        shops.removeIf(shop -> shop.getName() == null || shop.getName().isEmpty());

        return shops;
    }

    /**
     * Convierte una respuesta JSON en una lista de objetos Shop.
     * @param jsonResponse Respuesta JSON a convertir.
     * @return Lista de objetos Shop.
     */
    public ArrayList<Shop> convertJsonToShops (String jsonResponse) {
        ArrayList<Shop> shops = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(jsonResponse);

        if (!jsonElement.isJsonArray()) {
            throw new JsonParseException("Response is not a JSON Array");
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            if (element.isJsonArray()) {
                for (JsonElement innerElement : element.getAsJsonArray()) {
                    Shop shop= parseShop(innerElement);
                    if (shop != null) {
                        shops.add(shop);
                    }
                }
            } else if (element.isJsonObject()) {
               Shop shop = parseShop(element);
                if (shop != null) {
                    shops.add(shop);
                } else {
                    System.out.println("Invalid shop data found: " + element);
                }
            }
        }
        return shops;
    }

    /**
     * Analiza un elemento JSON para crear un objeto Shop.
     * @param jsonElement Elemento JSON a analizar.
     * @return Objeto Shop o null si el análisis falla.
     */
    private Shop parseShop(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement categoryElement = jsonObject.get("businessModel");

        if (categoryElement == null || categoryElement.isJsonNull()) {
            System.out.println("Warning: Found a shop without 'businessModel' property. Skipping this shop.");
            return null;
        }
        String businessModel = categoryElement.getAsString();
        switch (businessModel) {
            case "MAX_PROFIT":
                return gson.fromJson(jsonObject, MaxProfitShop.class);
            case "LOYALTY":
                return gson.fromJson(jsonObject, LoyaltyShop.class);
            case "SPONSORED":
                return gson.fromJson(jsonObject, SponsoredShop.class);
            default:
                throw new JsonParseException("Invalid category: " + businessModel);
        }
    }


    /**
     * Actualiza los ingresos de una tienda y envía la actualización a un servidor.
     * @param shop     El objeto Shop que contiene la información de la tienda.
     * @param earnings Los ingresos que se van a actualizar en el objeto Shop.
     * @throws ApiException Si la tienda no se encuentra o si falla la solicitud HTTP.
     */
    public void updateEarnings(Shop shop, double earnings) throws ApiException {
        shop.setEarnings(earnings);
        String shopJsonUpdate = new Gson().toJson(shop);
        int position = getPositionOfShop(shop.getName());
        if (position == -1) {
            throw new ApiException("Shop not found: " + shop.getName(), null, null);
        }
        String url = baseUrl + "/P1-G70/shops/" + position;
        try {
            apiHelper.putToUrl(url, shopJsonUpdate);
        } catch (IOException e) {
            throw new ApiException("Failed to make HTTP request to " + url, e, null);
        }
    }

    /**
     * Obtiene la posición de una tienda en la lista de tiendas cargadas a partir de su nombre.
     * @param shopName El nombre de la tienda cuya posición se desea encontrar.
     * @return La posición de la tienda en la lista, o -1 si no se encuentra.
     * @throws ApiException Si ocurre un error al cargar la lista de tiendas.
     * @throws JsonSyntaxException Si hay un error de sintaxis en el JSON al cargar la lista de tiendas.
     */
    public int getPositionOfShop(String shopName) throws ApiException, JsonSyntaxException {
        ArrayList<Shop> shops = loadShops();
        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).getName().equalsIgnoreCase(shopName)) {
                return i;
            }
        }
        return -1;
    }
}
