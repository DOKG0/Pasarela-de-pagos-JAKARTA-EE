package org.tallerjava.moduloComercio.dominio;

public enum CategoriaReclamo {
    POSITIVO(1),
    NEUTRO(2),
    NEGATIVO(3),
    SIN_DEFINIR(4);

    private int id;
    CategoriaReclamo(int id) { this.id = id; }

    public int getId() { return id; }

    public static CategoriaReclamo getById(int id) {
        switch (id) {
            case 1:
                return POSITIVO;
            case 2:
                return NEUTRO;
            case 3:
                return NEGATIVO;
            case 4:
                return SIN_DEFINIR;
            default:
                throw new IllegalArgumentException("Estado de Reclamo invalido");
        }
    }
}
