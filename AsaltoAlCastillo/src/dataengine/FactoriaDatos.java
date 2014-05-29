package dataengine;

import entrada.Teclado;
import eventos.Evento;
import java.util.ArrayList;
import java.util.List;

public class FactoriaDatos {

    public static void interpretarDatos(DataNode datos) {
        String tipoDatos = datos.asGroup().getIdentifier();

        DataGroup grupoDatos = datos.asGroup();

        if (grupoDatos == null) {
            throw new RuntimeException("Formato de datos erróneo");
        }

        switch (tipoDatos) {
            case "Teclado":
                for (DataNode dn : grupoDatos) {

                    DataGroup datosTecla = dn.asGroup();

                    String tecla = datosTecla.getIdentifier();

                    String comando = datosTecla.getNodeByIndex(0).getIdentifier();

                    /* Parámetros */
                    List<String> parametros = new ArrayList<String>();
                    for (int i = 2; i < datosTecla.getAllNodes().size(); i++) {
                        parametros.add(datosTecla.getNodeByIndex(i).asValue().getValue().getDatum().toString());
                    }
                    
                    Evento e = new Evento();
                    
                }
                break;
        }
    }
}
