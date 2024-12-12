package vn.edu.iuh.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UploadType {
    AVATAR("UPLOAD AVATAR", "avatars/"),
    PRODUCT_IMAGE("UPLOAD PRODUCT IMAGE", "products/"),
    MOVIE_IMAGE("UPLOAD MOVIE IMAGE", "movies/"),;

    private final String description;
    private final String path;
}
