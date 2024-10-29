package vn.edu.iuh.services;

public interface SlugifyService {
    String generateSlug(String input);

    String generateUniqueSlug(String input);
}
