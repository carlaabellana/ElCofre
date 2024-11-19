package business.product;
/**
 * Clase GeneralProduct que representa un producto general en el sistema. Este tipo de producto
 * aplica un impuesto general sobre el precio base del producto.
 */
public class GeneralProduct extends Product {
    /**
     * Tasa de impuesto general aplicada al producto, expresada como un porcentaje.
     */
    public static final double GENERAL_TAX = 21;
    /**
     * Constructor para crear una instancia de un producto general.
     *
     * @param name El nombre del producto.
     * @param brand La marca del producto.
     * @param mrp El precio de venta sugerido por el fabricante (MRP).
     */
    public GeneralProduct(String name, String brand, double mrp) {
        super(name, brand, mrp, "GENERAL");
    }
    /**
     * Calcula el precio del producto aplicando la tasa de impuesto general.
     *
     * @param priceAtShop El precio base del producto sin impuestos.
     * @return El precio del producto después de aplicar el impuesto general.
     */
    @Override
    public double calculatePrice(double priceAtShop) {
        return priceAtShop / (1 + (GENERAL_TAX/100));
    }
}
