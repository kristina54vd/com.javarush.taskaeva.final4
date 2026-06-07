package com.javarush.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(schema = "world", name = "country_language")
public class CountryLanguage {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "country_id")
        private Country country;

        private String language;

        @Column(name = "is_official")
        private Boolean isOfficial;

        private BigDecimal percentage;
    }

