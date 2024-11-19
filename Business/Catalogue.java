package business;

/**
 * Esta clase representa un catálogo de productos con atributos para el nombre del producto y su precio en la tienda
 */
public class Catalogue {
    private final String productName;
    private double priceAtShop;

    /**
     * Constructor de la clase Catalogue.*
     * @param productName El nombre del producto.
     * @param priceAtShop El precio del producto en la tienda.
     */
    public Catalogue(String productName, double priceAtShop) {
        this.productName = productName;
        this.priceAtShop = priceAtShop;
    }

    /**
     * Obtener el nombre del producto
     * @return String de productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Obtener el precio del producto en una tienda específica
     * @return double PriceAtShop
     */
    public double getPriceAtShop() {
        return priceAtShop;
    }
}
