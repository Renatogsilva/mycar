package br.com.renatogsilva.my_car.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_phone")
public class Phone implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "phone_id")
    private Long phoneId;

    @Column(length = 14, nullable = false)
    private String number;

    @Column(name = "type_phone", nullable = false)
    private Integer typePhone;

    @Column(name = "is_main", nullable = true)
    private boolean isMain;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    @ToString.Exclude
    private Person person;
}