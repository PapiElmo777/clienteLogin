import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Main {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;
        try (
                Socket socket = new Socket(hostname, port);
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader lectorServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String delServidor;
            String delUsuario;
            boolean loggedIn = false;
            System.out.println("--- Conectando al servidor de autenticación ---");
            while ((delServidor = lectorServidor.readLine()) != null) {
                System.out.println("SERVIDOR: " + delServidor);
                if (delServidor.startsWith("EXITO: Login correcto")) {
                    loggedIn = true;
                    break;
                }
                if (delServidor.startsWith("EXITO: Usuario registrado")) {
                    System.out.println("Registro exitoso. Vuelva a conectarse con el servidor.");
                    break;
                }
                delUsuario = teclado.readLine();
                if (delUsuario == null) {
                    break;
                }
                escritor.println(delUsuario);
            }

            if (loggedIn) {
                System.out.println("\n--- CHAT INICIADO (Escriba 'FIN' para salir) ---");
                String cadena;
                String mensaje;

                cadena = teclado.readLine();

                while (cadena != null && !cadena.equalsIgnoreCase("FIN")) {
                    escritor.println(cadena);
                    mensaje = lectorServidor.readLine();
                    if (mensaje == null) {
                        System.out.println("El servidor ha cerrado la conexión.");
                        break;
                    }
                    System.out.println("SERVIDOR: " + mensaje);
                    cadena = teclado.readLine();
                }
                escritor.println("FIN");
            }

        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor o la conexión se perdió.");
        }
        System.out.println("Programa cliente terminado.");
    }
}
