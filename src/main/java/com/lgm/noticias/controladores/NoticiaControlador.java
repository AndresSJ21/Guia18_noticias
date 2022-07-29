package com.lgm.noticias.controladores;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.entidades.Noticia;
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
    public String registroNoticia(MultipartFile archivo, @RequestParam String titulo, @RequestParam String contenido, @RequestParam String idAutor, ModelMap modelo) throws Exception {
        try {
            noticiaServicio.crearNoticia(archivo, titulo, contenido, idAutor);
        } catch (Exception ex) {
            List<Autor> autores = autorServicio.listarAutores();
            modelo.addAttribute("autores", autores);
            return "redirect:/cargar";
        }
        return "redirect:/";
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
    public String editarNoticia(@RequestParam(required = false) MultipartFile archivo, @RequestParam(required = false) String id, @RequestParam(required = false) String titulo, @RequestParam(required = false) String contenido, @RequestParam(required = false) String idAutor) throws Exception {

        try {
            noticiaServicio.editarNoticia(archivo, id, titulo, contenido, idAutor);
            return "redirect:/listar";
        } catch (Exception e) {
            return "redirect:/editar";
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

        noticiaServicio.borrarNoticia(id);

        return "noticia.html";
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
