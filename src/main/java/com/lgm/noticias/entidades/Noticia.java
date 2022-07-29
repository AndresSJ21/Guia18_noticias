package com.lgm.noticias.entidades;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class Noticia {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String titulo;

    @Column(columnDefinition="varchar(1000)")
    private String contenido;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @OneToOne
    private Foto foto;

    @ManyToOne
    private Autor autor;
    
    private boolean estado;
}
