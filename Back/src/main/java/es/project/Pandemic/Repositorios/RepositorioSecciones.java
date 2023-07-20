package es.project.Pandemic.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.project.Pandemic.EntidadesYClasesSecundarias.Seccion;

@Repository
public interface RepositorioSecciones extends JpaRepository<Seccion, Long>{
	
	@Query(value = "SELECT * FROM seccion WHERE seccionpadre IS NULL ORDER BY id ASC", nativeQuery = true)
	List<Seccion> getSecciones();
	
	@Query(value = "SELECT * FROM seccion WHERE seccionpadre IS NULL AND mostrarenmenu = true ORDER BY id ASC;", nativeQuery = true)
	List<Seccion> getSeccionesMenu();
	
	@Query(value = "SELECT accion FROM permisosrol"
			+ " INNER JOIN seccion ON permisosrol.seccion = seccion.id"
			+ " INNER JOIN rol AS rol ON permisosrol.rol = rol.id"
			+ " WHERE seccion = :idSeccion AND rol.nombre IN :listaRoles ", nativeQuery = true)
	List<String> getAccionesSeccionDeRoles(@Param("idSeccion") long idSeccion, @Param("listaRoles") List<String> listaRoles);
	
	@Query(value = "SELECT DISTINCT seccion FROM permisosrol"
			+" INNER JOIN seccion ON permisosrol.seccion = seccion.id"
			+" INNER JOIN rol AS rol ON permisosrol.rol = rol.id"
			+" WHERE seccion.mostrarenmenu = true AND rol.nombre IN :listaRoles ", nativeQuery = true)
	List<Long> getSeccionesPermitidasParaRolesEnMenu(@Param("listaRoles") List<String> listaRoles);
	
	@Query(value = "SELECT * FROM seccion WHERE nombre = :nombreSeccion", nativeQuery = true)
	Seccion getSeccion(@Param("nombreSeccion") String nombreSeccion);
	
	@Query(value = "SELECT * FROM seccion WHERE id = :idSeccion", nativeQuery = true)
	Seccion getSeccion(@Param("idSeccion") long idSeccion);
	
	
}