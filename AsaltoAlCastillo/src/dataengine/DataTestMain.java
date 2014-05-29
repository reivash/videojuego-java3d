package dataengine;

import eventos.Evento;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataTestMain {

    static int depth = 0;

    static public void print(DataGroup data) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
        System.out.print("* " + data.getIdentifier() + "\n");
        for (DataNode d : data.getAllNodes()) {
            if (d.isGroup()) {
                depth++;
                print(d.asGroup());
                depth--;
            } else {
                for (int i = 0; i <= depth; i++) {
                    System.out.print("  ");
                }
                if (d.isFunctionValue()) {
                    System.out.print("\\- Function: " + d.getIdentifier() + " -> " + d.asValue().getValue() + "\n");
                } else if (d.isKeyValue()) {
                    System.out.print("\\- KeyValue(" + d.getIdentifier() + ", " + d.asValue().getValue() + ")\n");
                }
            }
        }
    }

    static public void main(String argv[]) {
        /* Start the parser */
        try {
            /* Leer configuración teclado */
            InputStream in = new FileInputStream("teclado.txt");

            System.out.print("Write some test data defs: \n");
            BufferedReader aux = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = aux.readLine();
            while (line.length() > 0) {
                sb.append(line);
                sb.append(" ");
                line = aux.readLine();
            }
            in = new ByteArrayInputStream(sb.toString().getBytes());

            parser p = new parser(new Yylex(in));
            DataGroup data = (DataGroup) p.parse().value;

            FactoriaDatos.interpretarDatos(data);

            System.out.println("The parsing worked: " + (data != null));
            print(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
