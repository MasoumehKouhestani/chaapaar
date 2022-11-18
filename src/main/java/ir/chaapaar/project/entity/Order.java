package ir.chaapaar.project.entity;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_order")
@EqualsAndHashCode
public class Order implements Serializable {

    @EmbeddedId
    OrderId id;

    @NonNull
    @Column(nullable = false)
    Integer count;
}


