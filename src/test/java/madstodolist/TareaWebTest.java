package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datos-test.sql")
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaWebTest {

    @Autowired
    private MockMvc mockMvc;

    // Declaramos los servicios como Autowired y usamos los datos
    // de prueba de la base de datos
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TareaService tareaService;

    // Al moquear el manegerUserSession, no lanza la excepción cuando
    // en los controllers se llama a comprobarUsuarioLogeado y se intenta comprobar
    // si un usuario está logeado
    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void listaTareas() throws Exception {
        // Cargados datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect((content().string(allOf(
                        containsString("Lavar coche"),
                        containsString("Renovar DNI")
                ))));
    }

    @Test
    public void getNuevaTareaDevuelveForm() throws Exception {
        // Cargados datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(get("/usuarios/1/tareas/nueva"))
                .andExpect(content().string(containsString("action=\"/usuarios/1/tareas/nueva\"")));
    }

    @Test
    public void postNuevaTareaDevuelveRedirectYAñadeTarea() throws Exception {
        // Cargados datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(post("/usuarios/1/tareas/nueva")
                        .param("titulo", "Estudiar examen MADS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/1/tareas"));

        this.mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect((content().string(containsString("Estudiar examen MADS"))));
    }

    @Test
    public void deleteTareaDevuelveOKyBorraTarea() throws Exception {
        // Cargados datos de prueba del fichero datos-test.sql

        // La petición nos devuelve OK
        this.mockMvc.perform(delete("/tareas/1"))
                .andExpect(status().isOk());

        // Y se pide un listado y se comprueba que la tarea 1 ya no aparece
        this.mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect(content().string(
                        allOf(not(containsString("Lavar coche")),
                                containsString("Renovar DNI"))));
    }

    @Test
    public void getNuevaTareaDevuelveNotFoundCuandoNoExisteUsuario() throws Exception {
        // Cargados datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(get("/usuarios/2/tareas/nueva"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editarTareaDevuelveForm() throws Exception {
        // Cargados datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(get("/tareas/1/editar"))
                .andExpect(content().string(allOf(
                    // Contiene la acción para enviar el post a la URL correcta
                    containsString("action=\"/tareas/1/editar\""),
                    // Contiene el texto de la tarea a editar
                    containsString("Lavar coche"),
                    // Contiene enlace a listar tareas del usuario si se cancela la edición
                    containsString("href=\"/usuarios/1/tareas\""))));
    }
}
