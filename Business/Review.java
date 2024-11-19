package business;

/**
 * La clase Review representa una reseña con una calificación y un comentario
 */
public class Review {
    private int rating;
    private String comment;

    /**
     * Construye una nueva Review con la calificación y el comentario especificados.
     * @param rating  la calificación de la reseña, típicamente un valor numérico
     * @param comment el comentario asociado con la reseña, proporcionando retroalimentación o detalles adicionales
     */
    public Review(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    /**
     * Devuelve la calificación de la revisión.
     * @return int de la valoración
     */
    public int getRating() {
        return rating;
    }

    /**
     * Devuelve el comentario de la revisión.
     * @return una String con el comentario
     */
    public String getComment() {
        return comment;
    }
}
