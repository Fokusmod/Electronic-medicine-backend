package com.electronic.medicine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receptions")
public class Reception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reception_time")
    private Date receptionTime;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reception reception = (Reception) o;
        return Objects.equals(id, reception.id) && Objects.equals(receptionTime, reception.receptionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, receptionTime);
    }
}
