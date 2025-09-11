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
            while ((delServidor = lectorServidor.readLine()) != null) {
                System.out.println("SERVIDOR: " + delServidor);
                if (delServidor.startsWith("EXITO: Login correcto")) {
                    loggedIn = true;
                    break;
                }
                if (delServidor.startsWith("EXITO: Usuario registrado")) {
                    System.out.println("Registro exitoso. Vuelva a conectarse para iniciar sesión.");
                    break;
                }
                if (delServidor.contains("BIENVENIDO") || delServidor.contains("Ingrese")) {
                    delUsuario = teclado.readLine();
                    if (delUsuario == null) {
                        break;
                    }
                    escritor.println(delUsuario);
                }
            }

            if (loggedIn) {
                System.out.println("\n--- CHAT INICIADO ---");
                String opcionMenu;
                while (true) {
                    System.out.println("\nMENU DE OPCIONES:");
                    System.out.println("1. Enviar (Enviar un mensaje a otro usuario)");
                    System.out.println("2. Buzon  (Revisar tus mensajes)");
                    System.out.println("3. Fin    (Cerrar la sesión)");
                    System.out.print("Elige una opción: ");
                    opcionMenu = teclado.readLine();

                }
            }

        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor o la conexión se perdió.");
        }
        System.out.println("Programa cliente terminado.");
    }
}
