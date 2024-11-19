package business;
/**
 * La clase Cart representa un carrito de compras con dos atributos
 * el nombre del producto y nombre de la tienda y métodos para obtener estos valores.
 */
public class Cart {
    private String productName;
    private String shopName;

    /**
     * Construye un nuevo Cart con el nombre del producto y el nombre de la tienda especificados.
     * @param productName el nombre del producto a añadir al carrito
     * @param shopName    el nombre de la tienda donde está disponible el producto
     */
    public Cart(String productName, String shopName) {
        this.productName = productName;
        this.shopName = shopName;
    }
    /**
     * Permiten obtener el valor del nombre del producto
     * @return string productName
     */
    public String getProductName() {
        return productName;
    }
    /**
     * Permiten obtener el valor del nombre de la tienda
     * @return string shopName
     */
    public String getShopName() {
        return shopName;
    }
}
