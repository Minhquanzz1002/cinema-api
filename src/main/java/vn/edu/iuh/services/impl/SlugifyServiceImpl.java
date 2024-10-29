package vn.edu.iuh.services.impl;

import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;
import vn.edu.iuh.services.SlugifyService;

import java.util.UUID;

@Service
public class SlugifyServiceImpl implements SlugifyService {
    private final Slugify slugify;

    public SlugifyServiceImpl() {
        this.slugify = Slugify.builder()
                .customReplacement("đ", "d")
                .customReplacement("Đ", "D")
                .customReplacement("ê", "e")
                .customReplacement("ô", "o")
                .customReplacement("ơ", "o")
                .customReplacement("ư", "u")
                .customReplacement("ă", "a")
                .customReplacement("â", "a")
                .customReplacement("á", "a")
                .customReplacement("à", "a")
                .customReplacement("ả", "a")
                .customReplacement("ã", "a")
                .customReplacement("ạ", "a")
                .customReplacement("ấ", "a")
                .customReplacement("ầ", "a")
                .customReplacement("ẩ", "a")
                .customReplacement("ẫ", "a")
                .customReplacement("ậ", "a")
                .customReplacement("ắ", "a")
                .customReplacement("ằ", "a")
                .customReplacement("ẳ", "a")
                .customReplacement("ẵ", "a")
                .customReplacement("ặ", "a")
                .customReplacement("é", "e")
                .customReplacement("è", "e")
                .customReplacement("ẻ", "e")
                .customReplacement("ẽ", "e")
                .customReplacement("ẹ", "e")
                .customReplacement("ế", "e")
                .customReplacement("ề", "e")
                .customReplacement("ể", "e")
                .customReplacement("ễ", "e")
                .customReplacement("ệ", "e")
                .customReplacement("í", "i")
                .customReplacement("ì", "i")
                .customReplacement("ỉ", "i")
                .customReplacement("ĩ", "i")
                .customReplacement("ị", "i")
                .customReplacement("ó", "o")
                .customReplacement("ò", "o")
                .customReplacement("ỏ", "o")
                .customReplacement("õ", "o")
                .customReplacement("ọ", "o")
                .customReplacement("ố", "o")
                .customReplacement("ồ", "o")
                .customReplacement("ổ", "o")
                .customReplacement("ỗ", "o")
                .customReplacement("ộ", "o")
                .customReplacement("ớ", "o")
                .customReplacement("ờ", "o")
                .customReplacement("ở", "o")
                .customReplacement("ỡ", "o")
                .customReplacement("ợ", "o")
                .customReplacement("ú", "u")
                .customReplacement("ù", "u")
                .customReplacement("ủ", "u")
                .customReplacement("ũ", "u")
                .customReplacement("ụ", "u")
                .customReplacement("ứ", "u")
                .customReplacement("ừ", "u")
                .customReplacement("ử", "u")
                .customReplacement("ữ", "u")
                .customReplacement("ự", "u")
                .customReplacement("ý", "y")
                .customReplacement("ỳ", "y")
                .customReplacement("ỷ", "y")
                .customReplacement("ỹ", "y")
                .customReplacement("ỵ", "y")
                .customReplacement(" ", "-")
                .customReplacement(":", "-")
                .customReplacement(";", "-")
                .customReplacement(",", "-")
                .customReplacement(".", "-")
                .customReplacement("(", "-")
                .customReplacement(")", "-")
                .customReplacement("[", "-")
                .customReplacement("]", "-")
                .customReplacement("{", "-")
                .customReplacement("}", "-")
                .customReplacement("?", "-")
                .customReplacement("!", "-")
                .customReplacement("@", "-")
                .customReplacement("#", "-")
                .customReplacement("$", "-")
                .customReplacement("%", "-")
                .customReplacement("^", "-")
                .customReplacement("&", "-")
                .customReplacement("*", "-")
                .customReplacement("+", "-")
                .customReplacement("=", "-")
                .customReplacement("/", "-")
                .customReplacement("\\", "-")
                .customReplacement("|", "-")
                .customReplacement("~", "-")
                .customReplacement("`", "-")
                .customReplacement("<", "-")
                .customReplacement(">", "-")
                .customReplacement("\"", "-")
                .lowerCase(true)
                .build();
    }

    @Override
    public String generateSlug(String input) {
        if (input == null) return "";
        return slugify.slugify(input);
    }

    @Override
    public String generateUniqueSlug(String input) {
        String baseSlug = generateSlug(input);
        String randomStr = UUID.randomUUID().toString().substring(0, 4);
        return baseSlug + "-" + randomStr;
    }
}
