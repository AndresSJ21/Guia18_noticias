package com.lgm.noticias.controladores;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.entidades.Noticia;
import com.lgm.noticias.servicios.AutorServicio;
import com.lgm.noticias.servicios.NoticiaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/foto")
public class FotoControlador {
    
    @Autowired
    private AutorServicio autorServicio;
    
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    @GetMapping("/autor")
    public ResponseEntity<byte[]> fotoAutor(@RequestParam String idAutor) throws Exception{
        Autor autor = autorServicio.buscarAutorPorId(idAutor);
        try{
            if(autor.getFoto()==null){
                throw new Exception ();
            }
            byte[] foto = autor.getFoto().getContent();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        }catch(Exception ex){
            System.out.println("El usuario no tiene foto");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }  
    }
    
        @GetMapping("/noticia")
    public ResponseEntity<byte[]> fotoNoticia(@RequestParam String idNoticia) throws Exception{
        Noticia noticia = noticiaServicio.buscarNoticiaPorId(idNoticia);
        try{
            if(noticia.getFoto()==null){
                throw new Exception ();
            }
            byte[] foto = noticia.getFoto().getContent();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        }catch(Exception ex){
            System.out.println("La noticia no tiene foto");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }  
    }
}
