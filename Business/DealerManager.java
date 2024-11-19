package business;
import business.product.Product;
import business.product.ProductManager;
import business.shop.Shop;
import business.shop.ShopManager;
import persistence.ApiHelper;

import java.util.ArrayList;
/**
 * Esta clase se encarga de hacer de intermediario entre los dos managers
 */
public class DealerManager {
    private ShopManager shopManager;
    private ProductManager productManager;
    private ApiHelper apiHelper;

    /**
     * Constructor de la clase DealerManager.
     * @param shopManager El gestor de tiendas que se usará para gestionar las tiendas.
     * @param productManager El gestor de productos que se usará para gestionar los productos.
     */
    public DealerManager(ShopManager shopManager, ProductManager productManager) {
        this.shopManager = shopManager;
        this.productManager = productManager;
        this.apiHelper = new ApiHelper();
    }

    /**
     * Obtiene una lista de productos disponibles en el catálogo de una tienda específica.
     * @param shopName El nombre de la tienda de la cual se desea obtener la lista de productos.
     * @return Una lista de objetos Product que están en el catálogo de la tienda especificada.
     * Si la tienda no existe o no tiene productos en su catálogo, se devuelve una lista vacía.
     */
    public ArrayList<Product> getProductList(String shopName){
        ArrayList<Product> productsInCatalogue = new ArrayList<>();
        if(shopManager.nameShopExists(shopName)){
            Shop shop = shopManager.findShopByName(shopName);
            if(shop!= null){
                ArrayList<Catalogue> catalogue = shop.getCatalogue();
                for(Catalogue item: catalogue){
                    String productName = item.getProductName();
                    Product product = productManager.getProductByName(productName);
                    if(product != null){
                        productsInCatalogue.add(product);
                    }
                }
            }
        }
        return productsInCatalogue;
    }

    /**
     * Muestra la información de las tiendas que tienen un producto específico en su catálogo.*
     * @param product El producto cuyo nombre se utilizará para buscar en los catálogos de las tiendas.
     * @return Una lista de objetos Shop que tienen el producto especificado en su catálogo.
     * Si no se encuentran tiendas con el producto, se devuelve una lista vacía.
     */
    public ArrayList<Shop> showInfoShops(Product product){
        ArrayList <Shop> shops;
        ArrayList <Shop> results = new ArrayList<>();

        if(apiHelper.isConnected()) {
            shops = shopManager.shopArrayList();
        }else{
            shops = shopManager.getShopList();
        }

        String productName = product.getName();

        for (Shop s: shops) {
            for (Catalogue catalogueItem: s.getCatalogue()) {
                String catalogueProductName = catalogueItem.getProductName();
                   if (catalogueProductName.equalsIgnoreCase(productName)) {
                       results.add(s);
                   }
            }
        }
       return results;
    }
}
