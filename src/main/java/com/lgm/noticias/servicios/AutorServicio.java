package com.lgm.noticias.servicios;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.entidades.Foto;
import com.lgm.noticias.repositorios.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Transactional
    public void crearAutor (MultipartFile archivo, String nombre)throws Exception{
        
        Autor autor = new Autor();
        autor.setNombre(nombre);
        
        if (!archivo.isEmpty()){
            Foto foto = fotoServicio.guardar(archivo);
            autor.setFoto(foto);
        }
        
        autor.setEstado(true);
        
        autorRepositorio.save(autor);
    }
    
    public List<Autor> listarAutores(){
        List<Autor> autores = new ArrayList();
        autores = autorRepositorio.findAll();
        return autores;
    }
    
    @Transactional
    public void editarAutor (MultipartFile archivo, String id, String nombre) throws Exception{
        Optional<Autor> respuestaAutor = autorRepositorio.findById(id);
        if (respuestaAutor.isPresent()) {
            Autor autor = respuestaAutor.get();
            autor.setNombre(nombre);
            
            if(archivo != null){
                Foto foto = fotoServicio.guardar(archivo);
                autor.setFoto(foto);
            }
            autorRepositorio.save(autor);
        }
    }
    
    public Autor getOne(String id){
        return autorRepositorio.getOne(id);
    }
    
    
    public Autor buscarAutorPorId(String idAutor){
        Autor autor = new Autor();
        autor = null;
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        if (respuestaAutor.isPresent()){
            autor = respuestaAutor.get();   
        }          
        return autor;
    }
   
    @Transactional
    public void softDeleteAutor(String idAutor){
        Autor autor = buscarAutorPorId(idAutor);
        autor.setEstado(false);
    }
   
    
    private void validar(){
        /*
        To Do
        */
    }
    

}
