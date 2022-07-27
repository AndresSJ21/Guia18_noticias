package com.lgm.noticias.repositorios;

import com.lgm.noticias.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String> {

}

