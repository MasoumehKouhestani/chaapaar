package ir.chaapaar.project.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_customer")
@EqualsAndHashCode
public class Customer {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    String id;

    @Column(length = 500)
    String description;

    @NonNull
    @Email
    @Column(length = 100, nullable = false, unique = true)
    String email;

    @Column(length = 100)
    String firstName;

    @Column
    String lastName;


}
