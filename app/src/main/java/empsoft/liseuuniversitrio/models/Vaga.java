package empsoft.liseuuniversitrio.models;

import java.util.Date;

/**
 * Created by Arthur on 11/03/2017.
 */

public class Vaga {

    private final Date horario;
    private final String cargo;
    private final String descricao;

    public Vaga(String cargo, String descricao, Date horario) {
        this.cargo = cargo;
        this.descricao = descricao;
        this.horario = horario;
    }

    public Date getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCargo() {
        return cargo;
    }
}
