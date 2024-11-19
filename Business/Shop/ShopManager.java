package business.shop;

import business.Catalogue;
import com.google.gson.JsonSyntaxException;
import persistence.ApiException;
import persistence.ShopApiJsonDAO;
import persistence.ShopJsonDAO;
import persistence.ApiHelper;

import presentation.ConsoleUIManager;
import java.util.ArrayList;
/**
 * La clase ShopManager gestiona las operaciones relacionadas con las tiendas en el sistema.
 * Esto incluye la creación, eliminación, búsqueda y manipulación de tiendas y sus catálogos.
 */
public class ShopManager {
    private final ShopJsonDAO shopJsonDAO;
    private ArrayList<Shop> shopList;
    private ApiHelper apiHelper;
    private ShopApiJsonDAO shopApiJsonDAO;
    private ConsoleUIManager consoleUIManager;

    /** Constructor que inicializa un nuevo ShopManager. Carga las tiendas desde archivos JSON.
     **/
    public ShopManager(){
        this.shopJsonDAO = new ShopJsonDAO();
        this.shopList = new ArrayList<>();
        this.apiHelper = new ApiHelper();
        this.shopApiJsonDAO = new ShopApiJsonDAO();
        this.consoleUIManager = new ConsoleUIManager();
    }

    /**
     * Devuelve la lista de tiendas disponibles si está conectado a la API
     * @return shopArrayList
     */
    public ArrayList<Shop> shopArrayList() {
        ArrayList<Shop> shopArrayList = new ArrayList<>();
        try {
            shopArrayList = shopApiJsonDAO.loadShops();
        } catch (ApiException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
        return shopArrayList;
    }

    /**
     * Verifica si una tienda con el nombre especificado existe en la lista de tiendas.
     * @param name El nombre de la tienda que se desea verificar.
     * @return true si una tienda con el nombre especificado existe, false en caso contrario.
     */
    public boolean nameShopExists(String name) {
        boolean exists = false;
        ArrayList<Shop> shops = new ArrayList<>();
        if(apiHelper.isConnected()){
            shops = shopArrayList();
        }else{
            shops = getShopList();
        }
        for (Shop shop : shops) {
            if (shop.getName().equals(name)) {
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Crea y añade una nueva tienda basada en los parámetros dados y guarda los cambios tanto localmente como en la API.
     * @param name Nombre de la tienda.
     * @param description Descripción de la tienda.
     * @param year Año de fundación de la tienda.
     * @param category Categoría de la tienda.
     * @param catalogue Catálogo de productos de la tienda.
     * @param loyaltyThreshold Umbral de lealtad para tiendas de lealtad.
     * @param sponsorBrand Marca patrocinadora para tiendas patrocinadas.
     * @return true si la tienda se crea con éxito, false en caso de error.
     */
    public boolean createShop(String name, String description, int year, String category, ArrayList<Catalogue> catalogue, double loyaltyThreshold, String sponsorBrand) {
        if (apiHelper.isConnected()) {
            if ("SPONSORED".equalsIgnoreCase(category)) {
                SponsoredShop sponsoredShop = new SponsoredShop(name, description, year, catalogue, sponsorBrand);
                sponsoredShop.setSponsorBrand(sponsorBrand);
                try {
                    shopList = shopArrayList();
                    shopList.add(sponsoredShop);
                    shopJsonDAO.saveShops(shopList);
                    shopApiJsonDAO.saveShops(sponsoredShop);
                } catch (ApiException e) {
                    return false;
                }
            }
            if ("MAX_PROFIT".equalsIgnoreCase(category)) {
                MaxProfitShop maxProfitShop = new MaxProfitShop(name, description, year, catalogue);
                try {
                    shopList = shopArrayList();
                    shopList.add(maxProfitShop);
                    shopJsonDAO.saveShops(shopList);
                    shopApiJsonDAO.saveShops(maxProfitShop);
                } catch (ApiException e) {
                    return false;
                }
            }
            if ("LOYALTY".equalsIgnoreCase(category)) {
                LoyaltyShop loyaltyShop = new LoyaltyShop(name, description, year, catalogue, loyaltyThreshold);
                loyaltyShop.setLoyaltyThreshold(loyaltyThreshold);
                try {
                    shopList = shopArrayList();
                    shopList.add(loyaltyShop);
                    shopJsonDAO.saveShops(shopList);
                    shopApiJsonDAO.saveShops(loyaltyShop);
                } catch (ApiException e) {
                    return false;
                }
            }
        } else {
            if ("SPONSORED".equalsIgnoreCase(category)) {
                SponsoredShop sponsoredShop = new SponsoredShop(name, description, year, catalogue, sponsorBrand);
                sponsoredShop.setSponsorBrand(sponsorBrand);
                shopList.add(sponsoredShop);
                shopJsonDAO.saveShops(shopList);
            }
            if ("MAX_PROFIT".equalsIgnoreCase(category)) {
                MaxProfitShop maxProfitShop = new MaxProfitShop(name, description, year, catalogue);
                shopList.add(maxProfitShop);
                shopJsonDAO.saveShops(shopList);
            }
            if ("LOYALTY".equalsIgnoreCase(category)) {
                LoyaltyShop loyaltyShop = new LoyaltyShop(name, description, year, catalogue, loyaltyThreshold);
                loyaltyShop.setLoyaltyThreshold(loyaltyThreshold);
                shopList.add(loyaltyShop);
                shopJsonDAO.saveShops(shopList);
            }
        }
        return true;
    }

    /**
     * Obtiene la lista de todas las tiendas gestionadas.
     * @return Lista de tiendas.
     */
    public ArrayList<Shop> getShopList() {
        return shopList;
    }

    /**
     * Busca una tienda por su nombre.
     * @param name El nombre de la tienda a buscar.
     * @return La tienda si se encuentra, null en caso contrario.
     */
    public Shop findShopByName(String name) {
        ArrayList<Shop> shops = new ArrayList<>();
        if(apiHelper.isConnected()){
            shops = shopArrayList();
        }else{
            shops = shopList;
        }
        for (Shop shop :shops) {
            if (shop.getName().equalsIgnoreCase(name)) {
                return shop;
            }
        }
        return null;
    }

    /**
     * Añade un producto al catálogo de una tienda especificada y guarda los cambios, teniendo en cuenta si está conectada a la API.
     * @param shopName El nombre de la tienda.
     * @param productName El nombre del producto a añadir.
     * @param price El precio del producto.
     * @return true si el producto se añade con éxito, false en caso de error.
     */
    public boolean addToCatalogue(String shopName, String productName, double price) {
        boolean add = false;
        Shop shop = findShopByName(shopName);
        if (shop != null) {
            shop.addToCatalogue(productName, price);
            add = true;
            try {
                shopJsonDAO.saveShops(shopList);
                if (apiHelper.isConnected()) {
                    shopApiJsonDAO.saveShops(shop);
                }
            } catch (ApiException e) {
                add = false;
            }
        }
        return add;
    }

    /**
     * Elimina un producto del catálogo de una tienda especificada y guarda los cambios.
     * @param shopName El nombre de la tienda.
     * @param productName El nombre del producto a eliminar.
     */
    public void removeFromJsonCatalogue(String shopName, String productName) {
        if (nameShopExists(shopName)) {
            Shop shop = findShopByName(shopName);
            if (shop != null) {
                ArrayList<Catalogue> catalogues = shop.getCatalogue();
                Catalogue productToRemove = null;

                for (Catalogue item : catalogues) {
                    if (item.getProductName().equalsIgnoreCase(productName)) {
                        productToRemove = item;
                    }
                }
                catalogues.remove(productToRemove);
                try {
                    shopJsonDAO.saveShops(shopList);
                    if(apiHelper.isConnected()){
                        shopApiJsonDAO.saveShops(shop);
                    } else {
                        shopJsonDAO.saveShops(shopList);
                    }
                }catch(ApiException e){
                    consoleUIManager.showMessage("ERROR API EXCEPTION");
                }
            }
        }
    }

    /**
     * Obtiene el catálogo de productos de una tienda especificada.
     * @param shopName El nombre de la tienda de la cual obtener el catálogo.
     * @return El catálogo de la tienda, o una lista vacía si la tienda no existe.
     */
    public ArrayList<Catalogue> getCatalogueFromShop(String shopName) {
        Shop shop = findShopByName(shopName);
        if (shop != null) {
            return shop.getCatalogue();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Actualiza los ingresos de una tienda basado en el precio de un producto vendido.
     * @param shop La tienda cuyos ingresos se van a actualizar.
     * @param price El precio del producto vendido.
     */
    public void updateEarnings(Shop shop, double price) {
        if(apiHelper.isConnected()){
            try{
                double current = shop.getEarnings();
                double newIncome = current + price;
                shop.setEarnings(newIncome);
                shopApiJsonDAO.updateEarnings(shop, newIncome);
                shopApiJsonDAO.saveShops(shop);

            }catch(ApiException e){
                System.out.println("\nERROR: Failed to update earnings in API: " + e.getMessage());
            }
        }else {
            if (shop != null) {
                double current = shop.getEarnings();
                double newIncome = current + price;
                shop.setEarnings(newIncome);
            }
        }
    }

    /**
     * Carga y devuelve una lista de tiendas desde la API del DAO de tiendas.
     * @return Una lista de objetos Shop cargados desde la API. Si ocurre una excepción, devuelve una lista vacía.
     * @throws ApiException Si ocurre un error al cargar las tiendas desde la API.
     */
    public ArrayList<Shop> loadShops() throws ApiException{
        try{
            ArrayList<Shop> shops = shopApiJsonDAO.loadShops();
            shopList.clear();
            shopList.addAll(shops);
            return shops;

        } catch(ApiException e){
            consoleUIManager.showMessage("Error loading shops from API: " + e.getMessage() + "\n");
            return new ArrayList<>();
        }

    }
}