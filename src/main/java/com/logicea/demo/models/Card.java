package com.logicea.demo.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.logicea.demo.util.CardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "CARDS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String colour;

    @Column()
    String description;

    public Card(String name, String colour, String description, User user, CardStatus status) {
        this.name = name;
        this.colour = colour;
        this.description = description;
        this.user = user;
        this.status = status;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('TO DO', 'IN PROGRESS', 'DONE')")
    private CardStatus status;

    public boolean isOwnedBy(Long userId) {
        return this.user.getId() == userId;
    }

}
