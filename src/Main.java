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
                System.out.println("\n---CHAT INICIADO---");
                String opcionMenu;
                while (true) {
                    System.out.println("\nMENU DE OPCIONES:");
                    System.out.println("1. Enviar (Enviar un mensaje a otro usuario)");
                    System.out.println("2. Buzon  (Revisar tus mensajes)");
                    System.out.println("3. Eliminar cuenta (Borrar tu usuario y mensajes)");
                    System.out.println("4. Fin    (Cerrar la sesión)");
                    System.out.print("Elige una opción: ");
                    opcionMenu = teclado.readLine();

                    if (opcionMenu == null) {
                        escritor.println("FIN");
                        break;
                    }

                    if ("Enviar".equalsIgnoreCase(opcionMenu) || "1".equals(opcionMenu)) {
                        escritor.println("LISTA_USUARIOS");
                        String lista = lectorServidor.readLine();
                        System.out.println("Usuarios disponibles: " + lista);
                        System.out.print("¿Para quién es el mensaje?: ");
                        String destinatario = teclado.readLine();
                        System.out.print("Escribe tu mensaje: ");
                        String mensaje = teclado.readLine();

                        escritor.println("ENVIAR_MENSAJE:" + destinatario + ":" + mensaje);

                        System.out.println("SERVIDOR: " + lectorServidor.readLine());

                    } else if ("Buzon".equalsIgnoreCase(opcionMenu) || "2".equals(opcionMenu)) {
                        escritor.println("VER_BUZON");
                        String lineaBuzon;
                        while ((lineaBuzon = lectorServidor.readLine()) != null) {
                            if ("FIN_MENSAJES".equals(lineaBuzon)) {
                                break;
                            }
                            System.out.println(lineaBuzon);
                        }
                        System.out.println("--- Fin del buzón ---");

                    }else if ("Eliminar cuenta".equalsIgnoreCase(opcionMenu) || "3".equals(opcionMenu)) {
                        System.out.print("ADVERTENCIA: Esta acción es permanente y no se puede deshacer.\n¿Estás seguro de que quieres eliminar tu cuenta? (si/no): ");
                        String confirmacion = teclado.readLine();
                        if ("si".equalsIgnoreCase(confirmacion)) {
                            escritor.println("ELIMINAR_CUENTA");
                            String respuestaServidor = lectorServidor.readLine();
                            System.out.println("SERVIDOR: " + respuestaServidor);
                            if (respuestaServidor != null && respuestaServidor.startsWith("EXITO")) {
                                break;
                            }
                        } else {
                            System.out.println("Operación cancelada.");
                        }

                    }else if ("Fin".equalsIgnoreCase(opcionMenu) || "4".equals(opcionMenu)) {
                        escritor.println("FIN");
                        break;
                    } else {
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor o la conexión se perdió.");
        }
        System.out.println("Programa cliente terminado.");
    }
}
