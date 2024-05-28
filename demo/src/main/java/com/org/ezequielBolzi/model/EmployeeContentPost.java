package com.org.ezequielBolzi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_content_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeContentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)     //ESTO SE LO AGREGUE A LO ULTIMO
    @JoinColumn(name = "employee_id", referencedColumnName = "id", unique = true)
    private Employee employee;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "content_id", referencedColumnName = "id", unique = true)
    private Content content;

    @Column(name = "registered_at", updatable = false, nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "likes", nullable = false)
    private Integer likes;

    @ManyToOne
    @JoinColumn(name = "liked_by_employee_id")
    private Employee likedByEmployee;


}
