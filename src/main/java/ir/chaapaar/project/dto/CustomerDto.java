package ir.chaapaar.project.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CustomerDto {

    String description;
    String email;
    String firstName;
    String lastName;
}
