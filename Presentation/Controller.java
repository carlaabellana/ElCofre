package presentation;
import business.*;
import business.product.*;
import business.shop.*;
import com.google.gson.JsonSyntaxException;
import persistence.ApiException;
import persistence.ApiHelper;
import persistence.ProductApiJsonDAO;
import persistence.ShopApiJsonDAO;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Este constructor Java para la clase Controller recibe instancias de ConsoleUIManager, ProductManager y ShopManager como parámetros,
 * las asigna a las variables correspondientes en la clase. Presumiblemente, se utiliza para establecer la interfaz de usuario y los gestores de productos y tiendas en el controlador.
 */
public class Controller {
    private final ShopApiJsonDAO shopApiJsonDAO;
    private final ProductApiJsonDAO productApiJsonDAO;
    private final ConsoleUIManager consoleUIManager;
    private final ProductManager productManager;
    private final ShopManager shopManager;
    private final DealerManager dealerManager;
    private final ApiHelper apiHelper;
    private CartManager cartManager;
    private ArrayList<Cart> carts;

    /**
     * Este constructor Java para la clase Controller recibe instancias de ConsoleUIManager, ProductManager y ShopManager como parámetros,
     * las asigna a las variables correspondientes en la clase. Presumiblemente, se utiliza para establecer la interfaz de usuario y los gestores de productos y tiendas en el controlador.
     * @param ui necesario para intractuar con el usuario
     * @param productManager necesario para trabajar con los productos y sus reseñas
     * @param shopManager necesario para trabajar con las tiendas y sus catálogos
     * @param apiHelper necsario para la relación con la API
     * @param productApiJsonDAO necesario para comunicarnos con la API de productoa
     */
    public Controller(ConsoleUIManager ui, ProductManager productManager, ShopManager shopManager, ApiHelper apiHelper, ProductApiJsonDAO productApiJsonDAO) {
        this.consoleUIManager = ui;
        this.productManager = productManager;
        this.shopManager = shopManager;
        this.dealerManager = new DealerManager(shopManager, productManager);
        this.carts = new ArrayList<>();
        this.cartManager = new CartManager(carts);
        this.apiHelper = apiHelper;
        this.productApiJsonDAO = productApiJsonDAO;
        this.shopApiJsonDAO = new ShopApiJsonDAO();
    }

    /**
     *  Ejecuta la aplicación de compras digitales permitiendo su ejecución.
     * @throws FileNotFoundException por la necesidad de los ficheros para su funcionalidad
     */
    public void run() throws FileNotFoundException {
        consoleUIManager.showCorrectMessage();
        int option;
        do {
            consoleUIManager.showMainMenu();
            option = consoleUIManager.getOption("\nChoose a Digital Shopping Experience: ");
            executeOption(option);
        } while (option != 6);
    }

    /**
     *  Ejecuta la aplicación de compras digitales manejando las opciones del menú principal.
     * @param  option la opción que decide el usuario
     */
    private void executeOption(int option){
        switch (option) {
            case 1:
                manageProductMenu();
                break;
            case 2:
                manageShopsMenu();
                break;
            case 3:
                searchProducts();
                break;
            case 4:
                listShops();
                break;
            case 5:
                showUserCart();
                break;
            case 6:
                consoleUIManager.showMessage("\nWe hope to see you again!\n");
                break;
        }
    }

    /**
     *  Gestiona las opciones relacionadas con la administración de productos en el menú secundario.
     */
    private void manageProductMenu() {
        int productOption;
        do {
            consoleUIManager.showProductMenu();
            productOption = consoleUIManager.getOption("\nChoose an option: ");
            executeProductOption(productOption);
        } while (productOption != 3);
    }

    /**
     *  Ejecuta la aplicación de compras digitales manejando las opciones del menú secundario.
     * @param productOption la opción que decide el usuario
     */
    private void executeProductOption(int productOption) {
        switch (productOption) {
            case 1:
                createProduct();
                break;
            case 2:
                removeProduct();
                break;
            case 3:
                break;
            default:
                consoleUIManager.showMessage("ERROR: invalid Option\n");
        }
    }

    /**
     *  Permite la creación de un nuevo producto.
     */
    public void createProduct() {
        String name = consoleUIManager.askForString("\nPlease enter the product’s name: ");
        if (!(productManager.nameProductExists(name))) {
            String brand = consoleUIManager.askForString("Please enter the product’s brand: ");
            brand = convertBrand(brand);
            double mrp = consoleUIManager.askForNumber("Please enter the product’s maximum retail price: ");
            String category = consoleUIManager.getCategory();

            if (productManager.createProduct(name, brand, mrp, category, 0)) {
                consoleUIManager.showMessage("\nThe product \"" + name + "\" by \"" + brand + "\" was added to the system.\n");
            }
        } else {
           consoleUIManager.showMessage("\nThis product already exists\n");
        }
    }
    /**
     *  Permite la eliminación de un producto existente.
     */
    public void removeProduct() {
        try{
            if(apiHelper.isConnected()){
                ArrayList<Product> products = productApiJsonDAO.loadProducts();
                if(!products.isEmpty()){
                    consoleUIManager.showMessage("These are the currently available products:\n\n");
                    for(int i= 0; i<products.size(); i++){
                        consoleUIManager.showMessage("\n" + (i + 1) + ") " + products.get(i).getName());
                    }
                    int apiIndex = consoleUIManager.getOption("\nWhich one would you like to remove? ");
                    try {
                        confirmationApiRemove(apiIndex - 1, products);

                    }catch (ApiException e){
                        consoleUIManager.showMessage("ERROR Api: " + e.getMessage());
                    }
                }else{
                    consoleUIManager.showMessage("\nERROR: There are no products to remove...\n\n");
                }
            }else{
                if (productManager.getProductList().isEmpty()) {
                    consoleUIManager.showMessage("\nERROR: There are no products to remove...\n\n");
                } else {
                    showProductList();
                    int index = consoleUIManager.getOption("\nWhich one would you like to remove? ");
                    if (index > 0 && index <= productManager.getProductList().size()) {
                        confirmProductRemove(index - 1);
                    } else {
                        consoleUIManager.showMessage("\nERROR: please enter a valid number\n\n");
                    }
                }
            }
        }catch(ApiException e){
            consoleUIManager.showMessage("ERROR with API");
        }
    }

    /**
     *  Muestra la lista de productos
     */
    public void showProductList() {
        consoleUIManager.showMessage("These are the currently available products:\n\n");
        ArrayList<Product> products = productManager.getProductList();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            consoleUIManager.showMessage("\t" + (i + 1) + ") \"" + product.getName() + "\" by \"" + product.getBrand() + "\"\n");
        }
        int backOption = products.size() + 1;
        consoleUIManager.showMessage("\n\t" + backOption + ") Back\n\n");
    }

    /**
     *  Espera la confirmación del usuario a eliminar el producto
     * @param apiIndex la posición que se encuentra el producto que desea eliminar
     * @param products array de la lista de productos que se encuentran en la api
     * @throws ApiException controla las exepciones que puede haber en la API.
     */
    private void confirmationApiRemove(int apiIndex, ArrayList<Product> products) throws ApiException {
        if (apiIndex >= 0 && apiIndex < products.size()) {
            Product productToRemove = products.get(apiIndex);
            String confirmation = consoleUIManager.askForString("\nAre you sure you want to remove \"" + productToRemove.getName() + "\" by \"" + productToRemove.getBrand() + "\"? ");
            if (confirmation.equalsIgnoreCase("yes")) {
                productManager.removeProductFromApi(apiIndex, products);
                productManager.removeProduct(apiIndex + 1);
                consoleUIManager.showMessage("\n\"" + productToRemove.getName() + "\" by \"" + productToRemove.getBrand() + "\" has been withdrawn from sale.\n");
            } else {
                consoleUIManager.showMessage("\nRemoval canceled!\n");
            }
        } else {
            consoleUIManager.showMessage("\nERROR: please enter a valid number.\n");
        }
    }
    /**
     *  Espera la confirmación del usuario a eliminar el producto
     * @param index la posición que se encuentra el producto que desea eliminar
     */
    private void confirmProductRemove(int index) {
        Product productToRemove = productManager.getProductList().get(index);
        String response = consoleUIManager.askForString("\nAre you sure you want to remove \"" + productToRemove.getName() + "\" by \"" + productToRemove.getBrand() + "\"? ");
        if (response.equalsIgnoreCase("yes")) {
            productManager.removeProduct(index);
            consoleUIManager.showMessage("\n\"" + productToRemove.getName() + "\" by \"" + productToRemove.getBrand() + "\" has been withdrawn from sale.\n");
        } else {
            consoleUIManager.showMessage("\nRemoval canceled!\n");
        }
    }
    /**
     *  Convierte la marca según el formato indicado en el enunciado
     * @param brand la marca que escribe el usuario del producto a convertir
     */
    public String convertBrand(String brand) {
        StringBuilder correctBrand = new StringBuilder();
        String[] words = brand.toLowerCase().split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                if (correctBrand.length() > 0) {
                    correctBrand.append(" ");
                }
                char initial = Character.toUpperCase(word.charAt(0));
                correctBrand.append(initial).append(word.substring(1)).append(" ");
            }
            correctBrand.setLength(correctBrand.length() - 1);
        }
        return correctBrand.toString();
    }
    /**
     *  Ejecuta la aplicación de compras digitales manejando las opciones del menú de tiendas.
     */
    private void manageShopsMenu() {
        int shopOption;
        do {
            consoleUIManager.showManageShopMenu();
            shopOption = consoleUIManager.getOption("\nChoose an option: ");
            executeShopsOption(shopOption);
        } while (shopOption != 4);
    }
    /**
     *  Ejecuta la aplicación de compras digitales manejando las opciones del menú de las tiendas.
     * @param shopOption la opción que decide el usuario
     */
    private void executeShopsOption(int shopOption) {
        switch (shopOption) {
            case 1:
                createShop();
                break;
            case 2:
                expandCatalogueShop();
                break;
            case 3:
                reduceCatalogueShop();
                break;
            case 4:
                break;
            default:
                consoleUIManager.showMessage("\nERROR: invalid Option\n");
        }
    }

    /**
     *  Crea una nueva tienda con nombre, descripción, año de fundación y modelo de negocio.
     */
    public void createShop()  {
        String name = consoleUIManager.askForString("Please enter the shop’s name: ");
        if (!(shopManager.nameShopExists(name))) {
            String description = consoleUIManager.askForString("Please enter the shop’s description: ");
            int year = consoleUIManager.getOption("Please enter the shop’s founding year: ");
            String businessModel = consoleUIManager.getBusinessModel();
            ArrayList<Catalogue> catalogue = new ArrayList<>();

            double loyaltyThreshold = 0;
            String sponsorBrand = null;

            if(businessModel.equalsIgnoreCase("LOYALTY")){
                loyaltyThreshold = consoleUIManager.askForNumber("Please enter the shop’s loyalty threshold: ");
            }else if(businessModel.equalsIgnoreCase("SPONSORED")){
                sponsorBrand = consoleUIManager.askForString("Please enter the shop’s sponsoring brand:");
            }
            if (shopManager.createShop(name, description, year, businessModel, catalogue, loyaltyThreshold, sponsorBrand)) {
                System.out.println("\n\"" + name + "\" is now part of elCofre family.\n");
            }


        } else {
            consoleUIManager.showMessage("\nThis shop already exists\n");
        }
    }

    /**
     * Agrega un producto al catálogo de una tienda existente con su nombre y precio.
     */
    public void expandCatalogueShop() {
        String shopName = consoleUIManager.askForString("Please enter the shop’s name: ");
        if (shopManager.nameShopExists(shopName)) {
            String productName = consoleUIManager.askForString("Please enter the product's name: ");
            if (productManager.nameProductExists(productName)) {
                double productPrice = consoleUIManager.askForNumber("Please enter the product's price at this shop: ");
                if (productManager.priceProductExceed(productPrice)) {
                    Product product = productManager.getProductByName(productName);
                    if (product != null) {
                            if (shopManager.addToCatalogue(shopName, productName, productPrice)) {
                                consoleUIManager.showMessage("\n\"" + product.getName() + "\" by \"" + product.getBrand() + "\" is now being sold at \"" + shopName + "\".\n");
                            } else {
                                consoleUIManager.showMessage("ERROR: catalog could not be expanded\n");
                            }
                    }
                } else {
                    consoleUIManager.showMessage("\nERROR: Price exceeds maximum allowed!\n");
                }
            } else {
                consoleUIManager.showMessage("\nERROR: Product does not exists!\n");
            }
        } else {
            consoleUIManager.showMessage("\nERROR: shop does not exists!\n");
        }
    }

    /**
     * Elimina productos del catálogo de una tienda, seleccionados por el usuario.
     */
    public void reduceCatalogueShop() {
        String shopName = consoleUIManager.askForString("Please enter the shop’s name: ");
        if (shopManager.nameShopExists(shopName)) {
            ArrayList<Product> catalogue = dealerManager.getProductList(shopName);
            if (catalogue != null){
                consoleUIManager.showMessage("\nThis shop sells the following products:\n");
                int option = 0;
                do {
                    for (int i = 0; i < catalogue.size(); i++) {
                        consoleUIManager.showMessage("\n\t" + (i + 1) + ") \"" + catalogue.get(i).getName() + "\" by \"" + catalogue.get(i).getBrand() + "\"");
                    }
                    consoleUIManager.showMessage("\n\n\t" + (catalogue.size() + 1) + ") Back\n");
                    option = consoleUIManager.getOption("\nWhich one would you like to remove? ");

                    if (option > 0 && option <= catalogue.size()) {
                        Product selectedProduct = catalogue.get(option - 1);
                        consoleUIManager.showMessage("\n\"" + selectedProduct.getName() + "\" by \"" + selectedProduct.getBrand() + "\" is no longer being sold at \"" + shopName + "\".\n");
                        catalogue.remove(selectedProduct);
                        shopManager.removeFromJsonCatalogue(shopName, selectedProduct.getName());
                    } else if (option > catalogue.size() + 1) {
                        consoleUIManager.showMessage("\nERROR: Invalid option.\n");
                    }
                } while (option != (catalogue.size() + 1));
            }else{
                consoleUIManager.showMessage("\n" + shopName + " has no products in its catalogue\n");
            }
        } else {
            consoleUIManager.showMessage("\nERROR: Wrong shop name or this shop does not exists.\n");
        }
    }

    /**
     * Permite a los usuarios buscar productos por nombre o marca, ver detalles y reseñas, y agregar reseñas si es necesario.
     */
    public void searchProducts() {
        String searchInput = consoleUIManager.askForString("\nEnter your query: ");
        ArrayList<Product> results = productManager.searchProducts(searchInput);
        if (results.isEmpty()) {
            consoleUIManager.showMessage("\nSorry, there's no results for your search\n");
        }  else {
            consoleUIManager.showMessage("\nThe following products where found:\n\n");
            for (int i = 0; i < results.size(); i++) {
                Product product = results.get(i);
                consoleUIManager.showMessage("\t" + (i + 1) + ") \"" + product.getName() + "\" by \"" + product.getBrand() + "\"\n");
                ArrayList<Shop> shops = dealerManager.showInfoShops(product);
                if(!shops.isEmpty()){
                    consoleUIManager.showMessage("\t\tSold at:");
                    for(Shop shop: shops){
                        consoleUIManager.showMessage("\n\t\t\t- " + shop.getName() + ": " + getPriceProduct(shop, product)+ "\n");
                    }
                }else{
                    consoleUIManager.showMessage("\tThis product is not currently being sold in any shops.\n");
                }
            }
            consoleUIManager.showMessage("\n\t" + (results.size() + 1) + ") Back\n");

            int reviewOption = consoleUIManager.getOption("\nWhich one would you like to review? ");
            if (reviewOption > 0 && reviewOption <= (results.size())) {
                Product reviewedProduct = results.get(reviewOption - 1);
                consoleUIManager.showReviewProduct();
                int subMenuOption = consoleUIManager.getOption("\nChoose an option: ");
                if (subMenuOption == 1) {
                    ArrayList<Review> reviews = productManager.getReviews(reviewedProduct);
                    if(reviews != null){
                        consoleUIManager.showMessage("These are the reviews for \"" + reviewedProduct.getName() + "\" by \"" + reviewedProduct.getBrand() + "\":\n\n");
                        for(Review review: reviews){
                            consoleUIManager.showMessage("\t" + review.getRating() + "* " + review.getComment() + ".\n");
                        }
                        double averageRating = calculateAverageRating(reviews);
                        consoleUIManager.showMessage("\nAverage rating: " + averageRating + "*\n");

                    }else{
                        consoleUIManager.showMessage("No reviews available for \"" + reviewedProduct.getName() + "\" by \"" + reviewedProduct.getBrand() + "\".\n");
                    }
                } else if (subMenuOption == 2) {
                    boolean validRating = false;
                    int rating = 0;
                    while (!validRating) {
                        String star = consoleUIManager.askForString("\nPlease rate the product (1-5 stars): ");
                        rating = countStars(star);
                        if (rating >= 1 && rating <= 5) {
                            validRating = true;
                        } else {
                            consoleUIManager.showMessage("Please enter a valid rating (1-5 stars) using asterisks.\n");
                        }
                    }
                    String comment = consoleUIManager.askForString("Please add a comment to your review: ");
                    productManager.addProductReview(rating, comment, reviewedProduct);
                    consoleUIManager.showMessage("\nThank you for your review of \"" + reviewedProduct.getName() + "\" by \"" + reviewedProduct.getBrand() + "\".\n");

                }
            } else if(reviewOption == results.size()+1) {
                consoleUIManager.showMessage("Going back to to menu...\n");
            }else{
                consoleUIManager.showMessage("\nERROR: invalid option\n");
            }
        }
    }

    /**
     *  Calcula el promedio de calificaciones de las reseñas de un producto.
     * @param reviews la lista de reviews
     */
    private double calculateAverageRating(List<Review> reviews){
        if(reviews.isEmpty()){
            return 0.0;
        }
        int total = 0;
        for( Review review: reviews){
            total += review.getRating();
        }
        double average = (double)total / reviews.size();
        return Math.round(average * 100.0) / 100.0;
    }
    /**
     * Obtiene el precio de un producto en una tienda específica.
     * @param shop la tienda donde tenemos que sacar información
     * @param product el producto para comparar en todo el catálogo
     */
    public double getPriceProduct(Shop shop, Product product){
        double priceShop = 0.0;
        for(Catalogue catalogueItem: shop.getCatalogue()){
            if(catalogueItem.getProductName().equalsIgnoreCase(product.getName())){
                priceShop = catalogueItem.getPriceAtShop();
            }
        }
        return priceShop;
    }

    /**
     * Cuenta la cantidad de asteriscos en una cadena, utilizada para ingresar calificaciones.
     * @param star la cadena de carácteres que tenemos que contar
     */
    public int countStars(String star){
        int count = 0;
        for( char c: star.toCharArray()){
            if(c == '*'){
                count ++;
            }else{
                return -1;
            }
        }
        return count;
    }

    /**
     * Muestra una lista de todas las tiendas, su descripción y productos en su catálogo.
     */
    public void listShops(){
        boolean exit = false;
        do {
            List<Shop> shops = shopManager.getShopList();
            ArrayList<Cart> cartProducts = cartManager.getCartProducts();
            if(!shops.isEmpty()){
                consoleUIManager.showMessage("\nThe elCofre family is formed by the following shops: \n");
                try{
                    if(apiHelper.isConnected()) {
                        shops = shopManager.loadShops();
                    } else {
                        shops = shopManager.getShopList();
                    }
                } catch(ApiException | JsonSyntaxException e){
                    consoleUIManager.showMessage("Error loading shops from API: " + e.getMessage() + "\n");
                    return;
                }
            }else{
                try{
                    if(apiHelper.isConnected()) {
                        shops = shopManager.loadShops();
                        if(shops.isEmpty()){
                            consoleUIManager.showMessage("\nNo shops available in API\n");
                        }else{
                            consoleUIManager.showMessage("\nThe elCofre family is formed by the following shops: \n");
                        }
                    }else{
                        consoleUIManager.showMessage("\nNo shops available\n");
                    }
                } catch(ApiException | JsonSyntaxException e){
                    consoleUIManager.showMessage("Error loading shops from API: " + e.getMessage() + "\n");
                    return;
                }
            }
            for (int i = 0; i < shops.size(); i++) {
                consoleUIManager.showMessage("\n\t" + (i + 1) + ") " + shops.get(i).getName());
            }
            consoleUIManager.showMessage("\n\n\t" + (shops.size() + 1) + ") Back\n");
            int shopIndex = consoleUIManager.getOption("\nWhich catalogue do you want to see? ");
            if (shopIndex > 0 && shopIndex <= shops.size()) {
                Shop shop = shops.get(shopIndex - 1);
                consoleUIManager.showMessage("\n" + shop.getName() + " - " + "Since " + shop.getSince());
                consoleUIManager.showMessage("\n" + shop.getDescription() + "\n");
                ArrayList<Catalogue> catalogueList = shopManager.getCatalogueFromShop(shop.getName());
                if (!catalogueList.isEmpty()) {
                    int option;
                    do {
                        for (int i = 0; i < catalogueList.size(); i++) {
                            Catalogue catalogue = catalogueList.get(i);
                            Product productCatalogue = productManager.getProductByName(catalogue.getProductName());
                            if (productCatalogue != null) {
                                consoleUIManager.showMessage("\n\t" + (i + 1) + ") \"" + productCatalogue.getName() + "\" by \"" + productCatalogue.getBrand() + "\"");
                                consoleUIManager.showMessage("\n\t" + "Price: " + catalogue.getPriceAtShop() + "\n");
                            }else{
                                consoleUIManager.showMessage("IS NULL");
                            }
                        }
                        consoleUIManager.showMessage("\n\n\t" + (catalogueList.size() + 1) + ") Back\n");
                        option = consoleUIManager.getOption("\nWhich one are you interested in? ");
                        if (option > 0 && option <= catalogueList.size()) {
                            Catalogue selected = catalogueList.get(option - 1);
                            Product productReview = productManager.getProductByName(selected.getProductName());
                            consoleUIManager.showProductCatalogueMenu();
                            int subMenuOption = consoleUIManager.getOption("\nChoose an option: ");
                            if (subMenuOption == 1) {
                                ArrayList<Review> reviews = productManager.getReviews(productReview);
                                if (reviews != null) {
                                    consoleUIManager.showMessage("\nThese are the reviews for \"" + productReview.getName() + "\" by \"" + productReview.getBrand() + "\":\n\n");
                                    for (Review review : reviews) {
                                        consoleUIManager.showMessage("\t" + review.getRating() + "* " + review.getComment() + ".\n");
                                    }
                                    double averageRating = calculateAverageRating(reviews);
                                    consoleUIManager.showMessage("\nAverage rating: " + averageRating + "*\n");
                                }
                            } else if (subMenuOption == 2) {
                                String star = consoleUIManager.askForString("\nPlease rate the product (1-5 stars): ");
                                int rating = countStars(star);
                                if (rating >= 1 && rating <= 5) {
                                    String comment = consoleUIManager.askForString("Please add a comment to your review: ");
                                    productManager.addProductReview(rating, comment, productReview);
                                    consoleUIManager.showMessage("\nThank you for your review of \"" + productReview.getName() + "\" by \"" + productReview.getBrand() + "\".\n");
                                } else {
                                    consoleUIManager.showMessage("\nERROR: Invalid rating\n");
                                }
                            } else if (subMenuOption == 3) {
                                Catalogue auxName = catalogueList.get(option - 1);
                                Product productToAddCart = productManager.getProductByName(auxName.getProductName());
                                Cart ItemToAdd = new Cart(productToAddCart.getName(), shop.getName());
                                cartProducts.add(ItemToAdd);
                                consoleUIManager.showMessage("\n1x \"" + productToAddCart.getName() + "\" by \"" + productToAddCart.getBrand() + "\" has been added to your cart.\n");
                            }
                        }
                    } while (option != (catalogueList.size() + 1));
                } else {
                    consoleUIManager.showMessage("No products available in the catalogue\n");
                }
            } else if(shopIndex == (shops.size()+1)){
                exit = true;
            }
        }while(!exit);
    }

    /**
     * Muestra los productos en el carrito de un usuario, calcula el total y permite el pago o limpiar el carrito.
     */
    public void showUserCart() {
        ArrayList<Cart> cartProducts = cartManager.getCartProducts();
        if (cartProducts.isEmpty()) {
            consoleUIManager.showMessage("Your cart is empty.\n");
        } else {
            double total = 0.0;
            consoleUIManager.showMessage("\nYour cart contains the following items: \n");
            HashMap<String, Double> shopEarningsMap = new HashMap<>();
            for (Cart cart : cartProducts) {
                String productName = cart.getProductName();
                String shopName = cart.getShopName();
                Product product = productManager.getProductByName(productName);
                Shop shop = shopManager.findShopByName(shopName);
                if (product != null) {
                    double priceAtShop = getPriceProduct(shop, product);
                    double priceForCalculation = priceAtShop;
                    if (isHabitualCustomer(shop)) {
                        priceForCalculation = shop.calculateDiscount(priceAtShop, product);
                    }
                    total += priceForCalculation;
                    consoleUIManager.showMessage(String.format("\n\t- \"%s\" by \"%s\"\n\t\tPrice: %.2f\n", product.getName(), product.getBrand(), priceForCalculation));
                    shopEarningsMap.merge(shopName, priceForCalculation, Double::sum);
                }
            }
            consoleUIManager.showMessage(String.format("\nTotal: %.2f\n", total));
            consoleUIManager.showCartMenu();
            int cartOption = consoleUIManager.getOption("\nChoose an option: ");
            // Procesar el checkout.
            if (cartOption == 1) {
                boolean ok = false;
                HashMap<String, Double> shopTotalEarningsWithoutIVA = new HashMap<>();
                String answer = consoleUIManager.askForString("\nAre you sure you want to checkout? ");

                if (answer.equalsIgnoreCase("YES")) {
                    ArrayList<Cart> cartList = cartManager.getCartProducts();
                    for (Cart cart : cartList) {
                        String productName = cart.getProductName();
                        String shopName = cart.getShopName();
                        Product product = productManager.getProductByName(productName);
                        Shop shop = shopManager.findShopByName(shopName);

                        if (product != null && shop != null) {
                            double priceAtShop = getPriceProduct(shop, product);
                            double priceWithoutIVA= shop.calculateDiscount(priceAtShop, product);
                            shopTotalEarningsWithoutIVA.merge(shopName, priceWithoutIVA, Double::sum);
                            shopManager.updateEarnings(shop, priceWithoutIVA);
                            }
                        }
                    for (Map.Entry<String, Double> entry : shopTotalEarningsWithoutIVA.entrySet()) {
                        Shop shop = shopManager.findShopByName(entry.getKey());
                        double totalEarnings = entry.getValue();
                        String totalEarningsFormat = String.format("%.2f", totalEarnings);
                        double earningsShop = shop.getEarnings();
                        String earningShopFormat = String.format("%.2f", earningsShop);

                        consoleUIManager.showMessage("\n\"" + shop.getName() + "\" has earned " + totalEarningsFormat + " for a historic total of " + earningShopFormat + ".\n");

                        if (shop instanceof LoyaltyShop loyaltyShop && !ok && loyaltyShop.getEarnings() >= loyaltyShop.getLoyaltyThreshold()) {
                            ok = true;
                            consoleUIManager.showMessage("You are now a regular at \"" + shop.getName() + "\".\n");
                        }
                    }
                    cartList.clear();
                } else {
                    consoleUIManager.showMessage("\nCancelling Checkout...\n");
                }
            } else if (cartOption == 2) {
                String answer = consoleUIManager.askForString("Are you sure you want to clear your cart? ");
                if(answer.equalsIgnoreCase("YES")) {
                    emptyCart();
                }else{
                    consoleUIManager.showMessage("\nClearing cart canceled.\n");
                }
            }
        }
    }

    /**
     * Comprueba si el cliente es habitual, por lo que ha superado el umbral que ponía la tienda
     * @param shop la tienda a la que se refiere
     * @return boolean, ya que valida si es true o false
     */
    public boolean isHabitualCustomer(Shop shop){
    if(shop instanceof LoyaltyShop loyaltyShop){
        return loyaltyShop.getEarnings() >= loyaltyShop.getLoyaltyThreshold();
    }
    return false;
}
    /**
     * Limpia el carrito de compras del usuario.
     */
    public void emptyCart(){
        ArrayList<Cart> cartProducts = cartManager.getCartProducts();
        if (cartProducts.isEmpty()) {
            consoleUIManager.showMessage("\nYour cart is already empty.\n");
        } else {
            cartProducts.clear();
            consoleUIManager.showMessage("\nYour cart has been cleared.\n");
        }
    }
}