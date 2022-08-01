package com.lgm.noticias.servicios;

import com.lgm.noticias.entidades.Foto;
import com.lgm.noticias.excepciones.MyException;
import com.lgm.noticias.repositorios.FotoRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;
    
    @Transactional
    public Foto guardar(MultipartFile archivo) throws MyException{        
        if(archivo != null){
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContent(archivo.getBytes());
                foto.setFCreacion(new Date());
                foto.setEstado(true);
                return fotoRepositorio.save(foto);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
       return null;
    }  
    
    public Foto buscarFotoPorId(String idFoto){
        Foto autor = new Foto();
        autor = null;
        Optional<Foto> respuestaFoto = fotoRepositorio.findById(idFoto);
        if (respuestaFoto.isPresent()){
            autor = respuestaFoto.get();   
        }          
        return autor;
    }
    
    @Transactional
    public void softDeleteFoto(String idFoto){
        Foto foto = buscarFotoPorId(idFoto);
        foto.setEstado(false);
    }
    
    public Foto actualizarFoto(MultipartFile archivo, String idFoto){
        if(archivo != null ){
            try{
                Foto foto = new Foto();
                if (idFoto != null){
                    Optional<Foto> respuestaFoto = fotoRepositorio.findById(idFoto);
                    if(respuestaFoto.isPresent()){
                        foto = respuestaFoto.get();
                    }
                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContent(archivo.getBytes());
                
                return fotoRepositorio.save(foto);
            }catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
}
