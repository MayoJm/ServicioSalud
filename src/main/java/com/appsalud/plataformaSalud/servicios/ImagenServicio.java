package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Imagen;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ImagenServicio {
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws MiException {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (IOException e) {
                throw new MiException("Error al guardar la imagen: " + e.getMessage());
            }
        } else {
            // Si el archivo es nulo o está vacío, carga la imagen preestablecida
            Imagen imagenPreestablecida = obtenerImagenPreestablecida();
            if (imagenPreestablecida != null) {
                return imagenRepositorio.save(imagenPreestablecida);
            } else {
                throw new MiException("No se pudo cargar la imagen preestablecida.");
            }
        }
    }

//    private Imagen obtenerImagenPreestablecida() {
//        try {
//            Resource resource = new ClassPathResource("../../resources/static/img/logo.jpg");
//            byte[] contenido = Files.readAllBytes(resource.getFile().toPath());
//
//            Imagen imagenPreestablecida = new Imagen();
//            imagenPreestablecida.setMime("image/jpeg");
//            imagenPreestablecida.setNombre("logo.jpg");
//            imagenPreestablecida.setContenido(contenido);
//
//            return imagenPreestablecida;
//        } catch (IOException e) {
//            return null;
//        }
//    }

    private Imagen obtenerImagenPreestablecida() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/img/logo.jpg");
            byte[] contenido = inputStream.readAllBytes();

            Imagen imagenPreestablecida = new Imagen();
            imagenPreestablecida.setMime("image/jpeg");
            imagenPreestablecida.setNombre("logo.jpg");
            imagenPreestablecida.setContenido(contenido);

            return imagenPreestablecida;
        } catch (IOException e) {
            e.printStackTrace(); // Manejo apropiado del error
            return null;
        }
    }
}
