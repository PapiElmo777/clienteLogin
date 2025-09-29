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
                    System.out.println("--- Opciones de mensajes ---");
                    System.out.println("1. Enviar (Enviar un mensaje a otro usuario)");
                    System.out.println("2. Eliminar (Eliminar mensajes enviados)");
                    System.out.println("3. Buzon  (Revisar tus mensajes)");
                    System.out.println("--- Archivos ---");
                    System.out.println("4. Crear Archivo");
                    System.out.println("5. Mis Archivos");
                    System.out.println("6. Obtener Archivo de otro usuario");
                    System.out.println("--- Opciones de Usuarios ---");
                    System.out.println("7. Bloquear (Bloquea a un usuario de tu vida)");
                    System.out.println("8. Desbloquear usuario(Ya se reconciliaron)");
                    System.out.println("--- Opciones de cuenta ---");
                    System.out.println("9. Eliminar cuenta (Borrar tu usuario y mensajes)");
                    System.out.println("10. Fin    (Cerrar la sesión)");
                    System.out.print("Elige una opción: ");
                    opcionMenu = teclado.readLine();


                    if (opcionMenu == null) {
                        escritor.println("FIN");
                        break;
                    }
                switch (opcionMenu.toLowerCase()){
                    case "enviar":
                    case "1":
                        escritor.println("LISTA_BLOQUEADOS");
                        String bloqueados;
                        while ((bloqueados = lectorServidor.readLine()) != null) {
                            if ("FIN_LISTA_BLOQUEADOS".equals(bloqueados)) {
                                break;
                            }
                            System.out.println("Te caen gordos: " + bloqueados);
                        }
                        escritor.println("LISTA_USUARIOS");
                        String listaUsuarios = lectorServidor.readLine();
                        System.out.println("Usuarios disponibles: " + listaUsuarios);
                        System.out.print("\n¿Para quién es el mensaje?: ");
                        String destinatario = teclado.readLine();
                        System.out.print("Escribe tu mensaje: ");
                        String mensaje = teclado.readLine();
                        escritor.println("ENVIAR_MENSAJE:" + destinatario + ":" + mensaje);
                        System.out.println("SERVIDOR: " + lectorServidor.readLine());
                        break;
                    case "eliminar":
                    case "2":
                        escritor.println("MIS_MENSAJES");
                        System.out.println("\nTus mensajes enviados:");
                        String lineaMensaje;
                        boolean hayMensajes = false;
                        while ((lineaMensaje = lectorServidor.readLine()) != null) {
                            if ("FIN_LISTA_MENSAJES".equals(lineaMensaje)) {
                                break;
                            }
                            System.out.println(lineaMensaje);
                            hayMensajes = true;
                        }

                        if (!hayMensajes) {
                            System.out.println("No tienes mensajes enviados para eliminar.");
                        } else {
                            System.out.print("Ingresa el número del mensaje que quieres eliminar (o 'cancelar'): ");
                            String numParaEliminar = teclado.readLine();
                            if (numParaEliminar != null && !numParaEliminar.equalsIgnoreCase("cancelar")) {
                                escritor.println("ELIMINAR_MENSAJE:" + numParaEliminar);
                                System.out.println("SERVIDOR: " + lectorServidor.readLine());
                            } else {
                                System.out.println("Operación cancelada.");
                            }
                        }
                        break;
                    case "buzon":
                    case "3":
                        escritor.println("VER_BUZON");
                        String lineaBuzon;
                        while ((lineaBuzon = lectorServidor.readLine()) != null) {
                            if ("FIN_MENSAJES".equals(lineaBuzon)) {
                                break;
                            }
                            System.out.println(lineaBuzon);
                        }
                        System.out.println("--- Fin del buzón ---");
                        break;

                    case "bloquear":
                    case "7":
                        escritor.println("LISTA_USUARIOS");
                        String listaBloquear = lectorServidor.readLine();
                        System.out.println("Usuarios que puedes bloquear: " + listaBloquear);
                        System.out.print("¿Quien te cae gordo?: ");
                        String usuarioABloquear = teclado.readLine();
                        if (usuarioABloquear != null && !usuarioABloquear.trim().isEmpty()) {
                            escritor.println("BLOQUEAR_USUARIO:" + usuarioABloquear);
                            System.out.println("SERVIDOR: " + lectorServidor.readLine());
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                        break;
                    case "desbloquear":
                    case "8":
                        escritor.println("LISTA_BLOQUEADOS");
                        System.out.println("Tus usuarios bloqueados");
                        String lineaRespuesta;
                        String listaCompletaDeBloqueados = "";
                        boolean hayUsuariosBloqueados = false;
                        while ((lineaRespuesta = lectorServidor.readLine()) != null) {
                            if ("FIN_LISTA_BLOQUEADOS".equals(lineaRespuesta)) {
                                break;
                            }
                            if (!lineaRespuesta.equalsIgnoreCase("No tienes a nadie bloqueado.")) {
                                listaCompletaDeBloqueados = lineaRespuesta;
                                hayUsuariosBloqueados = true;
                            }
                            System.out.println(lineaRespuesta);
                        }
                        if (hayUsuariosBloqueados) {
                            System.out.print("Escribe el nombre del usuario a desbloquear (o 'cancelar'): ");
                            String usuarioADesbloquear = teclado.readLine();
                            if (usuarioADesbloquear != null && !usuarioADesbloquear.equalsIgnoreCase("cancelar")) {
                                escritor.println("DESBLOQUEAR_USUARIO:" + usuarioADesbloquear);
                                System.out.println("SERVIDOR: " + lectorServidor.readLine());
                            } else {
                                System.out.println("Operación cancelada.");
                            }
                        }
                        break;

                    case "eliminar cuenta":
                    case "9":
                        System.out.print("ADVERTENCIA: Esta acción es permanente y no se puede deshacer.\n¿Estás seguro de que quieres eliminar tu cuenta? (si/no): ");
                        String confirmacion = teclado.readLine();
                        if ("si".equalsIgnoreCase(confirmacion)) {
                            escritor.println("ELIMINAR_CUENTA");
                            String respuestaServidor = lectorServidor.readLine();
                            System.out.println("SERVIDOR: " + respuestaServidor);
                            if (respuestaServidor != null && respuestaServidor.startsWith("EXITO")) {
                                return;
                            }
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                        break;
                    case "salir":
                    case "10":
                        escritor.println("FIN");
                        System.out.println("Cerrando sesión...");
                        return;

                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                        break;
                }
                }
            }

        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor o la conexión se perdió.");
        }
        System.out.println("Programa cliente terminado.");
    }
}
