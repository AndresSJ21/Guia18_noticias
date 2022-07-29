package com.lgm.noticias.repositorios;

import com.lgm.noticias.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticiaRepositorio extends JpaRepository <Noticia, String>{
    @Query("SELECT n FROM Noticia n WHERE n.autor.id = :id")
    public List<Noticia> noticiaXautor(@Param ("id") String id);
    
    
    /*
    @Query("SELECT new com.lgm.noticias.dtos.NoticiasDto" 
            + "(n.id, n.titulo, n.autor.id)"
            + "FROM noticia n")
    public List<NoticiaDto> listarNoticiasPorAutor();

    @Query("SELECT n FROM noticias n WHERE n.titulo LIKE CONCAT('%',:titulo,'%')")
    public List<Noticia> buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT n FROM noticias n WHERE n.contenido LIKE CONCAT('%',:contenido,'%')")
    public List<Noticia> buscarPorContenido(@Param("contenido") String contenido);
  
    
    @Query("SELECT n FROM noticias n WHERE n.autor.nombre = :nombre")
    public List<Noticia> buscarPorAutor (@Param("nombre") String nombre);
    
  */
    
}
