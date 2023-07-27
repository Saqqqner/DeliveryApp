package ru.adel.deliveryapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotEmpty(message = "Адрес необходим для заполнения")
    @Pattern(regexp = "^[\\p{L}\\d\\s-,]+?,\\s*[\\p{L}\\d\\s-,]+?,\\s*[\\p{L}\\d\\s-,]+?,\\s*[\\p{L}\\d]+(?:\\s*[\\p{L}\\d]+)?$", message = "Address must be in the format: Country, City, Street, House number, Apartment number")
    private String address;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private CustomerRole role;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();


}


