package business.shop;

import business.Catalogue;
import business.product.Product;

import java.util.ArrayList;
/**
 * Herencia de Shop para implementar una tienda con un programa de lealtad, sin ofrecer descuentos específicos.
 */
public class LoyaltyShop extends Shop {
    private double loyaltyThreshold;
    /**
     * Construye una LoyaltyShop con detalles específicos para el funcionamiento del programa siguiendo un umbral de lealtad.
     * @param name El nombre de la tienda.
     * @param description La descripción de la tienda.
     * @param since El año de fundación de la tienda.
     * @param catalogue El catálogo de productos ofrecidos por la tienda.
     * @param loyaltyThreshold El umbral de lealtad para calificar en el programa de lealtad de la tienda.
     */
    public LoyaltyShop(String name, String description, int since, ArrayList<Catalogue> catalogue, double loyaltyThreshold) {
        super (name, description, since, "LOYALTY",catalogue);
        this.loyaltyThreshold = loyaltyThreshold;
    }
    /**
     * Obtiene el umbral de lealtad para la tienda.
     * @return El umbral de lealtad.
     */
    public double getLoyaltyThreshold() {
        return loyaltyThreshold;
    }
    /**
     * Establece el umbral de lealtad para la tienda.
     * @param loyaltyThreshold El nuevo umbral de lealtad.
     */
    public void setLoyaltyThreshold(double loyaltyThreshold) {
        this.loyaltyThreshold = loyaltyThreshold;
    }
    /**
     * Calcula el descuento para un producto, en este caso, no aplica un descuento específico por lealtad.
     * @param price El precio original del producto.
     * @param product El producto para el cual calcular el descuento.
     * @return El precio calculado sin aplicar descuento por lealtad.
     */
    @Override
    public double calculateDiscount(double price, Product product) {
        return product.calculatePrice(price);
    }
}
