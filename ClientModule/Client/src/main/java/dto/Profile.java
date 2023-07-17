package dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Profile implements Serializable {
    private String f_name;
    private String l_name;
    private String profilePhoto;
}
