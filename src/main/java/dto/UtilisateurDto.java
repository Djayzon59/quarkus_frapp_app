package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UtilisateurDto {

    @Schema(required = true,example = "jasonbailleul@gmail.com")
    private String email;

    @Schema(required = true,minLength = 8, maxLength = 20 ,example = "MyPassword59%")
    private String password;
}
