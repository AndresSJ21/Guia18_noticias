package com.lgm.noticias.servicios;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.entidades.Foto;
import com.lgm.noticias.excepciones.MyException;
import com.lgm.noticias.repositorios.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    @Transactional
    public void crearAutor (MultipartFile archivo, String nombre)throws MyException{
       
        validar(archivo, nombre);
        
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
        autores = autorRepositorio.findAll(Sort.by(Sort.Direction.ASC, "nombre"));
        return autores;
    }
    public void editarAutor (MultipartFile archivo, String id, String nombre) throws MyException{
        
        validar(archivo, nombre);
        
        Optional<Autor> respuestaAutor = autorRepositorio.findById(id);
        if (respuestaAutor.isPresent()) {
            Autor autor = respuestaAutor.get();
            autor.setNombre(nombre);
            
            Foto foto = fotoServicio.guardar(archivo);
            autor.setFoto(foto);

            autorRepositorio.save(autor);
        }else{
            throw new MyException ("No existe el autor indicado");
        }
    }
    
    public Autor getOne(String id){
        return autorRepositorio.getOne(id);
    }
    
    
    public Autor buscarAutorPorId(String idAutor) throws MyException{
        Autor autor = new Autor();
        autor = null;
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        if (respuestaAutor.isPresent()){
            autor = respuestaAutor.get();   
        }else{
            throw new MyException("No existe el autor especificado");
        }          
        return autor;
    }
   
    @Transactional
    public void softDeleteAutor(String idAutor) throws MyException{
        Autor autor = buscarAutorPorId(idAutor);
        autor.setEstado(false);
    }
   
    @Transactional
    public boolean borrarAutor (String idAutor) throws MyException{
        boolean borradoExitoso = false;
            if (noticiaServicio.noticiasPorAutor(idAutor).isEmpty()){
                autorRepositorio.deleteById(idAutor);
                borradoExitoso = true;
            }else{
                borradoExitoso = false;
                throw new MyException ("El autor tiene noticias vinculadas y no puede borrarse");
            }
        return borradoExitoso;
        
    }
    
    @Transactional    
    public boolean cambiarEstadoAutor(String id){
        Optional<Autor> respuestaAutor = autorRepositorio.findById(id);
        
        if (respuestaAutor.isPresent()) {
            Autor autor = respuestaAutor.get();
           
            if(autor.isEstado()){
                autor.setEstado(false);
            }else{
                autor.setEstado(true);
            }
            autorRepositorio.save(autor);
            return autor.isEstado();
        }

        return false;
    }
    
    private void validar (MultipartFile archivo, String nombre) throws MyException{
                
        if(nombre == null || nombre.isEmpty()){
            throw new MyException ("El nombre no puede ser nullo o estar vacío");
        }  
        
        if(archivo == null || archivo.isEmpty()){
            throw new MyException ("Debe ingresar una foto del autor");
        }
    }

}
