package com.lgm.noticias.controladores;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.entidades.Noticia;
import com.lgm.noticias.excepciones.MyException;
import com.lgm.noticias.servicios.AutorServicio;
import com.lgm.noticias.servicios.NoticiaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class NoticiaControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/cargar")
    public String cargarNoticia(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "noticia_carga.html";
    }

    @PostMapping("/registro")
    public String registroNoticia(@RequestParam (required = false) MultipartFile archivo, @RequestParam (required = false) String titulo, @RequestParam (required = false) String contenido, @RequestParam (required = false) String idAutor, ModelMap modelo, ModelMap model) throws MyException {
        try {
            noticiaServicio.crearNoticia(archivo, titulo, contenido, idAutor);
            model.put("exito", "La noticia se cargó correctamente");
            List<Noticia> noticias = noticiaServicio.listarNoticias();
            modelo.addAttribute("noticias", noticias);
        } catch (MyException ex) {
            List<Autor> autores = autorServicio.listarAutores();
            modelo.addAttribute("autores", autores);
            model.put("error", ex.getMessage());
            return "noticia_carga.html";
        }
        return "noticia_admin.html";
    }

    @GetMapping("/")
    public String listarNoticias(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        return "noticia.html";
    }

    @GetMapping("/editar/{id}")
    public String editarNoticia(@PathVariable String id, ModelMap modeloNoticia, ModelMap modeloAutor) {
        modeloNoticia.put("noticia", noticiaServicio.getOne(id));
        List<Autor> autores = autorServicio.listarAutores();
        modeloAutor.addAttribute("autores", autores);
        return "noticia_edicion.html";
    }

    @PostMapping("/editar/{id}")
    public String editarNoticia(@RequestParam(required = false) MultipartFile archivo, @RequestParam(required = false) String id, @RequestParam(required = false) String titulo, @RequestParam(required = false) String contenido, @RequestParam(required = false) String idAutor) throws MyException {

        try {
            noticiaServicio.editarNoticia(archivo, id, titulo, contenido, idAutor);
            return "redirect:/listar";
        } catch (MyException e) {
            System.out.println(e.getMessage());
            return "noticia.html";
        }
    }

    @GetMapping("/mostrar/{id}")
    public String verNoticia(@PathVariable String id, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_ver.html";
    }

    @GetMapping("/admin")
    public String adminNoticia(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);

        return "noticia_admin.html";
    }
    
    @GetMapping("/adminGral")
    public String gralAdmin() {

        return "admin.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarNoticia(@PathVariable String id) {
        try{
            noticiaServicio.borrarNoticia(id);
            return "redirect:../";
        }catch(MyException ex){
            System.out.println(ex.getMessage());
            return "redirect:../admin";
        }
  
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstadoNoticia(@PathVariable String id) {
        if (noticiaServicio.cambiarEstadoNoticia(id)) {
            return "redirect:/admin";
        } else {
            return "redirect:/admin";
        }
    }
}
