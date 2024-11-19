package presentation;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Se encarga de gestionar la interfaz de usuario en una aplicación.
 */
public class ConsoleUIManager {
    private static Scanner scanner;
    /**
     * Este constructor para la clase ConsoleUIManager inicializa un objeto Scanner para la entrada estándar del sistema (System.in).
     * Básicamente, se utiliza para recibir la entrada del usuario desde la consola.
     */
    public ConsoleUIManager() {
        this.scanner = new Scanner(System.in);
    }
    /**
     * Muestra un mensaje de bienvenida y verifica archivos locales.
     */
    public void showPrincipalMessage(){
        System.out.println("       ________     ____ \n" +
                "  ___ / / ____/___ / __/_______\n" +
                " / _ \\/ / / / __ \\/ /_/ ___/ _ \\\n" +
                "/ __/ / /___/ /_/ / __/ / / __/ \n" +
                "\\___/_/\\____/\\____/_/ /_/ \\___/\n");
        System.out.println("Welcome to elCofre Digital Shopping Experiences.\n");
        System.out.println("\nChecking API status...");
    }
    /**
     *  Muestra un mensaje de inicio correcto.
     */
    public void showCorrectMessage(){
        System.out.println("\n\nVerifying local files...");
        System.out.println("Starting program...");
    }

    /**
     * Muestra el menú principal de la aplicación con opciones.
     */
    public void showMainMenu(){
        System.out.println("\n\t1) Manage Products\n\t2) Manage Shops\n\t3) Search Products\n\t4) List Shops\n\t5) Your Cart\n\n\t6) Exit");
    }

    /**
     * Solicita al usuario una opción numérica y la devuelve como un entero.
     */
    public int getOption(String message){
        while(true){
            try{
                System.out.print(message);
                return scanner.nextInt();
            } catch( InputMismatchException e){
                System.out.println("ERROR: option must be an integer!\n");
            } finally {
                scanner.nextLine();
            }
        }
    }

    /**
     * Muestra un mensaje en la consola.
     * @param message El mensaje que se quiere mostrar
     */
    public void showMessage(String message){
        System.out.print(message);
    }

    /**
     * Muestra un menú relacionado con la gestión de productos.
     */
    public void showProductMenu(){
        System.out.println("\n\t1) Create a Product\n\t2) Remove a Product\n\n\t3) Back");
    }

    /**
     * Solicita al usuario una cadena de texto y la devuelve.
     * @param message El mensaje que se quiere escribir
     * @return String devuelve la string que se lee
     */
    public String askForString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Solicita al usuario un número decimal y lo devuelve como un valor double.
     * @param message El mensaje que se quiere escribir
     * @return Double devuelve el numero double que se lee
     */
    public double askForNumber(String message) {
        System.out.print(message);
        while (true) {
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Number not valid.");
                System.out.print(message);
            }
        }
    }

    /**
     * Solicita al usuario seleccionar una categoría de producto.
     * @return String la categoría del producto
     */
    public String getCategory(){
        Scanner scanner = new Scanner(System.in);
        String category;
        System.out.println("\nThe system supports the following product categories:\n");
        System.out.println("\tA) General\n\tB) Reduced Taxes\n\tC) Superreduced Taxes\n");

        System.out.print("Please pick the product’s category: ");
        category = scanner.nextLine().toUpperCase();
        while(!(category.equals("A") || category.equals("B") || category.equals("C"))){
            System.out.println("ERROR: Invalid category. Please enter a valid category\n");
            System.out.print("Please pick the product’s category: ");
            category = scanner.nextLine().toUpperCase();
        }
        switch(category){
            case "A":
                return "GENERAL";
            case "B":
                return "REDUCED";
            case "C":
                return "SUPER_REDUCED";
        }
        return category;
    }

    /**
     * Solicita al usuario seleccionar un modelo de negocio para una tienda.
     * @return String el modelo de negocio del producto
     */
    public String getBusinessModel(){
        Scanner scanner = new Scanner(System.in);
        String model;
        System.out.println("\nThe system supports the following business models:\n");
        System.out.println("\tA) Maximum Benefits\n\tB) Loyalty\n\tC) Sponsored\n");

        System.out.print("Please pick the shop’s business model: ");
        model = scanner.nextLine().toUpperCase();

        while(!(model.equals("A") || model.equals("B") || model.equals("C"))){
            System.out.println("ERROR: Invalid business model.\n");
            System.out.print("Please try again: ");
            model = scanner.nextLine().toUpperCase();
        }
        switch(model){
            case "A":
                return "MAX_PROFIT";
            case "B":
                return "LOYALTY";
            case "C":
                return "SPONSORED";
        }
        return model;
    }

    /**
     * Muestra un menú relacionado con la gestión de tiendas.
     */
    public void showManageShopMenu(){
        System.out.println("\n\t1) Create a Shop\n\t2) Expand a Shop's Catalogue\n\t3) Reduce a Shop's Catalogue\n\n\t4) Back");
    }

    /**
     * Muestra un menú relacionado con la revisión de productos.
     * @return String la categoría del producto
     */
    public void showReviewProduct(){
        System.out.println("\n\t1) Read Reviews\n\t2) Review Product\n\n\t3) Back");
    }

    /**
     * Muestra un menú relacionado con la gestión de productos en un catálogo de tienda.
     */
    public void showProductCatalogueMenu(){
        System.out.println("\n\t1) Read Reviews\n\t2) Review Product\n\t3) Add to a cart\n\n\t4) Back");
    }

    /**
     * Muestra un menú relacionado con el carrito de compras del usuario.
     */
    public void showCartMenu () {
        System.out.println("\n\t1) Checkout\n\t2) Clear Cart\n\n\t3) Back");
    }

}
