package com.lgm.noticias.servicios;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.entidades.Foto;
import com.lgm.noticias.entidades.Noticia;
import com.lgm.noticias.repositorios.AutorRepositorio;
import com.lgm.noticias.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;

    
    @Transactional
    public void crearNoticia (MultipartFile archivo, String titulo, String contenido, String idAutor)throws Exception{
        
        Autor autor = autorRepositorio.findById(idAutor).get();        
        Noticia noticia = new Noticia();
        noticia.setTitulo(titulo);
        noticia.setContenido(contenido);
        noticia.setFecha(new Date());
        
        Foto foto = fotoServicio.guardar(archivo);
        
        noticia.setFoto(foto);
        noticia.setAutor(autor);
        noticia.setEstado(true);
        
        noticiaRepositorio.save(noticia);
    }
   
    
    public List<Noticia> listarNoticias (){
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.findAll(Sort.by(Sort.Direction.DESC,"fecha"));
        return noticias;
    }
    
    @Transactional
    public void editarNoticia (MultipartFile archivo, String id, String titulo, String Contenido, String idAutor) throws Exception{
        Optional<Noticia> respuestaNoticia = noticiaRepositorio.findById(id);
        if(respuestaNoticia.isPresent()){
            Noticia noticia  = respuestaNoticia.get();
            noticia.setTitulo(titulo);
            noticia.setContenido(Contenido);

            if(archivo != null){
                Foto foto = fotoServicio.guardar(archivo);
                noticia.setFoto(foto);
            }

            if(idAutor != null){
                Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
                if(respuestaAutor.isPresent()){
                    Autor autor = respuestaAutor.get();
                    noticia.setAutor(autor);
                }
                
            }else{
                noticia.setAutor(noticia.getAutor());
            }
            noticiaRepositorio.save(noticia);
        }
    }
    
      
    public Noticia buscarNoticiaPorId(String idNoticia){
        Noticia noticia = new Noticia();
        noticia = null;
        Optional<Noticia> respuestaNoticia = noticiaRepositorio.findById(idNoticia);
        if (respuestaNoticia.isPresent()){
            noticia = respuestaNoticia.get();   
        }          
        return noticia;
    }
    
    public Noticia getOne (String id){
        return noticiaRepositorio.getOne(id);
    }
    
    @Transactional
    public void softDeleteNoticia(String idNoticia){
        Noticia noticia = buscarNoticiaPorId(idNoticia);
        noticia.setEstado(false);
    }
    
    
}
