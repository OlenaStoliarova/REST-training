package ua.training.hrm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table( name="employee", uniqueConstraints={@UniqueConstraint(columnNames={"firstName", "lastName"})})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    @Size(min=5, message = "Email can't be shorter than 5 characters")
    private String email;

    @Column(name = "jobFunction")
    @Enumerated(EnumType.STRING)
    private JobFunction jobFunction;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "primarySkill", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Skill primarySkill;
}
