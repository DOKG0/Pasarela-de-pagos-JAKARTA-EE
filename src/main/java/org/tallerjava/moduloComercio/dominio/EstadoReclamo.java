package org.tallerjava.moduloComercio.dominio;

public enum EstadoReclamo {
    PENDIENTE(1),
    REVISION(2),
    FINALIZADO(3);


    private int id;
    EstadoReclamo(int id) { this.id = id; }
    public int getId() { return id; }

    public static EstadoReclamo getById(int id) {
        switch (id) {
            case 1:
                return PENDIENTE;
            case 2:
                return REVISION;
            case 3:
                return FINALIZADO;
            default:
                throw new IllegalArgumentException("Estado de Reclamo invalido");
        }
    }
}
