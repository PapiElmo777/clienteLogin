import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Main {
    public static void main(String[] args) {
        Socket salida = null;
        try {
            salida = new Socket("localhost", 8080);
        } catch (IOException e) {
            System.out.println("Hubo problemas en la conexion de red");
            System.exit(1);
        }

        try (
                PrintWriter escritor = new PrintWriter(salida.getOutputStream(), true);
                BufferedReader lector = new BufferedReader(new InputStreamReader(salida.getInputStream()));
                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String respuestaServidor;
            String userInput;
            boolean loggedIn = false;


            while (!loggedIn) {
                respuestaServidor = lector.readLine();
                System.out.println("SERVIDOR: " + respuestaServidor);


                if (respuestaServidor != null && respuestaServidor.startsWith("EXITO: Usuario registrado")) {

                }
                if (respuestaServidor != null && respuestaServidor.startsWith("EXITO: Login correcto")) {
                    loggedIn = true;
                    continue;
                }
                if (respuestaServidor != null && respuestaServidor.startsWith("ERROR:")) {

                }


                userInput = teclado.readLine();
                escritor.println(userInput);


                respuestaServidor = lector.readLine();
                System.out.println("SERVIDOR: " + respuestaServidor);
                userInput = teclado.readLine();
                escritor.println(userInput);


                respuestaServidor = lector.readLine();
                System.out.println("SERVIDOR: " + respuestaServidor);
                userInput = teclado.readLine();
                escritor.println(userInput);
            }

            if (loggedIn) {
                System.out.println("--- CHAT INICIADO (Escriba 'FIN' para salir) ---");
                String cadena;
                String mensaje;


                cadena = teclado.readLine();

                while (!cadena.equalsIgnoreCase("FIN")) {
                    escritor.println(cadena);
                    mensaje = lector.readLine();
                    System.out.println("SERVIDOR: " + mensaje);
                    if (mensaje == null || mensaje.equalsIgnoreCase("FIN")) {
                        System.out.println("El servidor ha terminado la conexión.");
                        break;
                    }
                    cadena = teclado.readLine();
                }
                escritor.println("FIN");
            }

        } catch (IOException e) {
            System.out.println("Error de comunicacion entre los sockets");
        } finally {
            try {
                if (salida != null){
                    salida.close();
                }
            } catch (IOException e) {
                System.out.println("Hubo problemas al cerrar la conexión.");
            }
        }
    }
}