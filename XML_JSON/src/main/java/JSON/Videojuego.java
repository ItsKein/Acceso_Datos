package JSON;

import java.util.List;

 class VideoJuego {
    private String titulo;
    private int anioLanzamiento;
    private String desarrollador;
    private List<String> plataformas;

    // Es crucial tener un constructor sin argumentos para la deserialización
    public VideoJuego() {}

    // Constructor para crear objetos fácilmente
    public VideoJuego(String titulo, int anioLanzamiento, String desarrollador, List<String> plataformas) {
        this.titulo = titulo;
        this.anioLanzamiento = anioLanzamiento;
        this.desarrollador = desarrollador;
        this.plataformas = plataformas;
    }

    // Getters y Setters son necesarios para que Jackson acceda a las propiedades
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getAnioLanzamiento() { return anioLanzamiento; }
    public void setAnioLanzamiento(int anioLanzamiento) { this.anioLanzamiento = anioLanzamiento; }
    public String getDesarrollador() { return desarrollador; }
    public void setDesarrollador(String desarrollador) { this.desarrollador = desarrollador; }
    public List<String> getPlataformas() { return plataformas; }
    public void setPlataformas(List<String> plataformas) { this.plataformas = plataformas; }

    @Override
    public String toString() {
        return "Videojuego{" +
                "titulo='" + titulo + '\'' +
                ", anioLanzamiento=" + anioLanzamiento +
                ", desarrollador='" + desarrollador + '\'' +
                ", plataformas=" + plataformas +
                '}';
    }
}

