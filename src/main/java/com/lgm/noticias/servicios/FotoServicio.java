package com.lgm.noticias.servicios;

import com.lgm.noticias.entidades.Foto;
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
    public Foto guardar(MultipartFile archivo) throws Exception{        
        if(archivo != null){
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContent(archivo.getBytes());
                foto.setFCreacion(new Date());
                foto.setActivo(true);
                return fotoRepositorio.save(foto);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
       return null;
    }  
    
    /*    @Transactional
    public Foto editar(String idFoto, MultipartFile archivo) throws Exception{
    if(archivo != null){
    try {
    
    Foto foto = new Foto();
    
    if(idFoto != null){
    Optional<Foto> respuestaFoto = fotoRepositorio.findById(idFoto);
    if(respuestaFoto.isPresent()){
    foto = respuestaFoto.get();
    }
    }
    foto.setMime(archivo.getContentType());
    foto.setNombre(archivo.getName());
    foto.setContent(archivo.getBytes());
    foto.setFCreacion(new Date());
    foto.setActivo(true);
    return fotoRepositorio.save(foto);
    } catch (Exception ex) {
    System.err.println(ex.getMessage());
    }
    }
    return null;
    } */
}
