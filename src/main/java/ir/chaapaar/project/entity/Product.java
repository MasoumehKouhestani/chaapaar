package ir.chaapaar.project.entity;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_product")
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @NonNull
    @Column(length = 100, nullable = false, unique = true)
    String name;

    @NonNull
    @Column(nullable = false)
    Double price;

}
