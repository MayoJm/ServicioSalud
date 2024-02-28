package com.appsalud.plataformaSalud.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Imagen {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String mime; // atributo que asigna el formato del archivo de imagen

    private String nombre;

    @Column(columnDefinition = "LONGBLOB")
    @Basic(fetch = FetchType.LAZY) // @Lob = informa a spring que el archivo puede ser grande @Basic(fetch.lazy) =
                                   // el contenido se va a cargar solo cuando le pidamos, haciendo que la query sea
                                   // mas liviana
    private byte[] contenido;

}
