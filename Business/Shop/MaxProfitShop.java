package business.shop;
import business.product.Product;
import business.Catalogue;
import java.util.ArrayList;

/**
 * Herencia de Shop para implementar una tienda enfocada en maximizar ganancias, sin ofrecer descuentos.
 */
public class MaxProfitShop extends Shop{
    /**
     * Construye una MaxProfitShop con detalles específicos para el funcionamiento del programa.
     * @param name El nombre de la tienda.
     * @param description La descripción de la tienda.
     * @param since El año de fundación de la tienda.
     * @param catalogue El catálogo de productos ofrecidos por la tienda.
     */
    public MaxProfitShop (String name, String description, int since, ArrayList<Catalogue> catalogue) {
        super(name, description, since, "MAX_PROFIT",catalogue);
    }
    /**
     * Calcula el descuento para un producto, en este caso, siempre retorna el precio sin descuento.
     * @param price El precio original del producto.
     * @param product El producto para el cual calcular el "descuento".
     * @return El precio sin aplicar ningún descuento, enfocado en maximizar las ganancias de la tienda.
     */
    @Override
    public double calculateDiscount(double price, Product product){
        return product.calculatePrice(price);
    }
}
