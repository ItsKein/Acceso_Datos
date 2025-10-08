package Tema1.ListadoFichero;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

public class ListadoFicheros {

    static void main(String[] args) {
        String ruta = ".";
        if ( args.length >= 1 ) ruta = args[0];
        File fich = new File(ruta);

        if ( !fich.exists()) {
            System.out.println("No existe fichero o ruta en: " + ruta );
        } else {
            if (fich.isFile()) {
                System.out.println( ruta + " es un fichero");
            } else {
                System.out.println( ruta + " Es un directorio. Contenidos: ");
                File[] ficheros = fich.listFiles();
                if (ficheros != null) {
                    for (File f : ficheros) {
                        mostrarInfo(f);
                    }
                }
            }
        }
    }
    private static void mostrarInfo( File f ) {
        String tipo = f.isDirectory() ? "DIR" : "FILE";

        long size = f.isFile() ? f.length() : 0;

        String permisos = ( f.canRead() ? "r" : "-" ) +
                ( f.canWrite() ? "w" : "-" ) +
                ( f.canExecute() ? "x" : "-" );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        String fecha = sdf.format(new Date(f.lastModified()));

        System.out.printf("(%s) %-20s [Permisos: %s] [Tamaño: %d bytes] [Modificado: %s]%n",
                tipo, f.getName(), permisos, size, fecha);
    }
}