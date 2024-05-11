package com.example.sources.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ProfileInfo")
public class ProfileInfo {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    private String firstname;
    private String surname;
    private String lastname;
    private String telephone;
    private String address;
}
