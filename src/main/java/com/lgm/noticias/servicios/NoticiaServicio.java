package com.lgm.noticias.servicios;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.entidades.Foto;
import com.lgm.noticias.entidades.Noticia;
import com.lgm.noticias.excepciones.MyException;
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
    public void crearNoticia(MultipartFile archivo, String titulo, String contenido, String idAutor) throws MyException {
        
        
        validar(archivo, titulo, contenido, idAutor);

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

    public List<Noticia> listarNoticias() {
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
        return noticias;
    }

    @Transactional
    public void editarNoticia(MultipartFile archivo, String id, String titulo, String contenido, String idAutor) throws MyException {

        validar(archivo, titulo, contenido, idAutor);

        Optional<Noticia> respuestaNoticia = noticiaRepositorio.findById(id);
        if (respuestaNoticia.isPresent()) {
            Noticia noticia = respuestaNoticia.get();
            noticia.setTitulo(titulo);
            noticia.setContenido(contenido);
            Foto foto = fotoServicio.guardar(archivo);
            noticia.setFoto(foto);
            Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
            if (respuestaAutor.isPresent()) {
                Autor autor = respuestaAutor.get();
                noticia.setAutor(autor);
            }else{
                throw new MyException("El autor ingresado no está cargado");
            }
validar(archivo, titulo, contenido, idAutor);
            noticiaRepositorio.save(noticia);
        }else{
            throw new MyException("La noticia indicada no está cargada");
        }
    }

    public Noticia buscarNoticiaPorId(String idNoticia) {
        Noticia noticia = new Noticia();
        noticia = null;
        Optional<Noticia> respuestaNoticia = noticiaRepositorio.findById(idNoticia);
        if (respuestaNoticia.isPresent()) {
            noticia = respuestaNoticia.get();
        }
        return noticia;
    }

    public Noticia getOne(String id) {
        return noticiaRepositorio.getOne(id);
    }

    //este método me permite habilitar el borrado de autores solamente si no están vinculados a ninguna noticia
    public List<Noticia> noticiasPorAutor(String id) {
        List<Noticia> noticiasXautor = new ArrayList();
        noticiasXautor = noticiaRepositorio.noticiaXautor(id);
        return noticiasXautor;
    }

    @Transactional
    public void borrarNoticia(String idNoticia) throws MyException{
        if(buscarNoticiaPorId(idNoticia)!=null){
            noticiaRepositorio.deleteById(idNoticia);
        }else{
            throw new MyException("no hay noticia para borrar");
        }
    }

    @Transactional
    public boolean cambiarEstadoNoticia(String id) {
        Optional<Noticia> respuestaNoticia = noticiaRepositorio.findById(id);

        if (respuestaNoticia.isPresent()) {
            Noticia noticia = respuestaNoticia.get();

            if (noticia.isEstado()) {
                noticia.setEstado(false);
            } else {
                noticia.setEstado(true);
            }
            noticiaRepositorio.save(noticia);
            return noticia.isEstado();
        }

        return false;
    }

    private void validar(MultipartFile archivo, String titulo, String contenido, String idAutor) throws MyException {
        if (titulo == null || titulo.isEmpty()) {
            throw new MyException("El titulo no puede ser nulo o estar vacío");
        }

        if (contenido == null || contenido.isEmpty()) {
            throw new MyException("Se debe cargar un contenido para la noticia");
        }

        if (archivo == null || archivo.isEmpty()) {
            throw new MyException("La noticia debe contener una foto");
        }
        
        if (idAutor == null || idAutor.isEmpty()) {
            throw new MyException("La nota debe tener un autor");
        }
    }
}
