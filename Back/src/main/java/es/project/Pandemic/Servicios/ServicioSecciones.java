package es.project.Pandemic.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.project.Pandemic.EntidadesYClasesSecundarias.Rol;
import es.project.Pandemic.EntidadesYClasesSecundarias.Seccion;
import es.project.Pandemic.Repositorios.RepositorioRoles;
import es.project.Pandemic.Repositorios.RepositorioSecciones;

@Service
public class ServicioSecciones{
	
	@Autowired
    private RepositorioSecciones repositorioSecciones;
	
	@Autowired
    private RepositorioRoles repositorioRoles;
	
	public List<Seccion> getTodasLasSecciones(){
		return repositorioSecciones.getSecciones();
	}
	
	public List<Seccion> getSeccionesMenuParaUsuario(String documentoIdentidad){
		List<String> rolesUsuario = repositorioRoles.getRolesUsuarioSinCentro(documentoIdentidad).stream().map(rol -> rol.getNombre()).collect(Collectors.toList());
		List<Seccion> todasLasSeccionesDelMenu = repositorioSecciones.getSeccionesMenu();
		List<Long> seccionesPermitidasParaRolesUsuario = repositorioSecciones.getSeccionesPermitidasParaRolesEnMenu(rolesUsuario);
		List<Seccion> listadoDeSeccionesPermitidasParaUsuario = new ArrayList();
		for (Seccion seccion : todasLasSeccionesDelMenu) {
			if(seccion.getAccesoLibre()) {
				listadoDeSeccionesPermitidasParaUsuario.add(seccion);
			} else if(seccionesPermitidasParaRolesUsuario.stream().anyMatch(idSeccionPermitida -> idSeccionPermitida == seccion.getId())) {
				List<Seccion> listadoDeSubSeccionesPermitidasParaUsuario = new ArrayList();
				for(Seccion subSeccion : seccion.getSubSecciones()) {
					if(seccionesPermitidasParaRolesUsuario.stream().anyMatch(idSeccionPermitida -> idSeccionPermitida == subSeccion.getId())) listadoDeSubSeccionesPermitidasParaUsuario.add(subSeccion);
				}
				seccion.setSubSecciones(listadoDeSubSeccionesPermitidasParaUsuario);
				listadoDeSeccionesPermitidasParaUsuario.add(seccion);
			}
		}
		return listadoDeSeccionesPermitidasParaUsuario;
	}
	
	public Seccion getSeccion(int idSeccion) {
		return repositorioSecciones.getSeccion(idSeccion);
	}
	
	public Seccion getSeccion(String nombreSeccion) {
		return repositorioSecciones.getSeccion(nombreSeccion);
	}
	
	public List<String> getAccionesSeccion(long idSeccion, String documentoIdentidad) {
		List<String> listaRoles = repositorioRoles.getRolesUsuarioSinCentro(documentoIdentidad).stream().map(Rol::toString).collect(Collectors.toList());
		return repositorioSecciones.getAccionesSeccionDeRoles(idSeccion, listaRoles);
	}

}