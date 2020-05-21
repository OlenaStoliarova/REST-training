package ua.training.hrm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "skill", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 2, message = "Skill name can't be shorter than 2 characters")
    private String name;
}
