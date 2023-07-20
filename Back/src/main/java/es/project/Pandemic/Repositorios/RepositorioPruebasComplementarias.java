package es.project.Pandemic.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.project.Pandemic.EntidadesYClasesSecundarias.CarpetaDePruebas;
import es.project.Pandemic.EntidadesYClasesSecundarias.PruebaComplementaria;
import es.project.Pandemic.EntidadesYClasesSecundarias.RequisitosPrueba;

public interface RepositorioPruebasComplementarias extends JpaRepository<PruebaComplementaria, Long> {
	@Query(value = "SELECT id FROM carpetadepruebas ORDER BY id DESC LIMIT 1", nativeQuery = true)
	long getIdMasAltoCarpetasDePruebas();
	
	@Query(value = "SELECT id FROM pruebacomplementaria ORDER BY id DESC LIMIT 1", nativeQuery = true)
	Long getIdMasAltoPruebas();
	
	@Query(value = "SELECT id FROM enfermeropruebas ORDER BY id DESC LIMIT 1", nativeQuery = true)
	Long getIdMasAltoEnfermeroPruebas();
	
	@Query(value = "SELECT id FROM requisitospruebas ORDER BY id DESC LIMIT 1", nativeQuery = true)
	Long getIdMasAltoRequisitosPruebas();
	
	@Query(value = "SELECT id FROM pruebascarpetadepruebas ORDER BY id DESC LIMIT 1", nativeQuery = true)
	Long getIdMasAltoRelacionPruebasCarpetaDePruebas();
	
	@Query(value = "SELECT * FROM pruebacomplementaria ORDER BY id ", nativeQuery = true)
	List<PruebaComplementaria> getTodasPruebasComplementarias();
	
	@Query(value = "SELECT * FROM carpetadepruebas ORDER BY id ", nativeQuery = true)
	List<Object[]> getTodasCarpetasDePruebas();	
	
	@Query(value = "SELECT COUNT(1) FROM pruebacomplementaria WHERE nombre = :nombrePrueba ", nativeQuery = true)
	Long getNumeroDePruebasConNombre(@Param("nombrePrueba")String nombrePrueba);
	
	@Query(value = "SELECT * FROM pruebacomplementaria WHERE nombre = :nombrePrueba", nativeQuery = true)
	PruebaComplementaria getPruebaPorNombre(@Param("nombrePrueba")String nombrePrueba);
	
	@Query(value = "SELECT pruebacomplementaria.id, pruebacomplementaria.nombre, duracion, descripcion, avisos, recomendaciones, separacion FROM pruebascarpetadepruebas INNER JOIN pruebacomplementaria ON pruebascarpetadepruebas.prueba = pruebacomplementaria.id  WHERE carpetapruebas = :idCarpeta ORDER BY pruebacomplementaria.id", nativeQuery = true)
	List<PruebaComplementaria> getTodasLasPruebasDentroDeCarpetaDePruebas(@Param("idCarpeta")long idCarpeta);
	
	@Query(value = "SELECT * FROM pruebacomplementaria WHERE id = :idPrueba ", nativeQuery = true)
	PruebaComplementaria getPruebaComplementariaPorId(@Param("idPrueba")long idPrueba);
	
	@Query(value = "SELECT nombre FROM pruebacomplementaria WHERE id = :idPrueba ", nativeQuery = true)
	String getNombrePruebaComplementaria(@Param("idPrueba")long idPrueba);
		
	@Query(value = "SELECT prueba FROM enfermeropruebas WHERE enfermero = :idUsuarioEnfermero ORDER BY prueba ASC ", nativeQuery = true)
	List<Long> getPruebasDisponiblesParaUsuario(@Param("idUsuarioEnfermero")long idUsuarioEnfermero);
	
	@Query(value = "SELECT DISTINCT prueba FROM horariospruebascentros WHERE centro = :idCentro ORDER BY prueba ASC ", nativeQuery = true)
	List<Long> getPruebasTratablesEnCentro(@Param("idCentro")long idCentro);
	
	@Query(value = "SELECT COUNT(1) FROM enfermeropruebas WHERE enfermero = :idUsuarioEnfermero AND prueba = :idPrueba", nativeQuery = true)
	Integer puedeTratarEnfermeroPrueba(@Param("idPrueba")long idPrueba, @Param("idUsuarioEnfermero")long idUsuarioEnfermero);
	
	@Query(value = "SELECT COUNT(1) FROM carpetadepruebas WHERE id = :idCarpeta ", nativeQuery = true)
	Integer existeCarpetaDePruebas(@Param("idCarpeta")long idCarpeta);
	
	@Query(value = "SELECT * FROM requisitospruebas WHERE prueba = :idPrueba ORDER BY id ASC ", nativeQuery = true)
	List<Object[]> getRequisitosPrueba(@Param("idPrueba")long idPrueba);
	
	@Query(value = "SELECT carpeta.id, carpeta.nombre FROM pruebascarpetadepruebas INNER JOIN carpetadepruebas AS carpeta ON carpetapruebas  = carpeta.id WHERE prueba = :idPrueba ", nativeQuery = true)
	List<Object[]> getCarpetaDeLaPrueba(@Param("idPrueba")long idPrueba);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO carpetadepruebas (id, nombre) VALUES (:idCarpeta, :nombreCarpeta) ", nativeQuery = true)
	void crearNuevaCarpetaDePruebas(@Param("idCarpeta")long idCarpeta, @Param("nombreCarpeta")String nombreCarpeta);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO pruebacomplementaria (id, nombre, duracion, descripcion, avisos, recomendaciones, separacion) VALUES (:idPrueba, :nombrePrueba, :duracionPrueba, :descripcion, :avisos, :recomendaciones, :separacionSiguientePrueba) ", nativeQuery = true)
	void crearNuevaPrueba(@Param("idPrueba")long idPrueba, @Param("nombrePrueba")String nombrePrueba, @Param("duracionPrueba")long duracionPrueba, @Param("separacionSiguientePrueba")long separacionSiguientePrueba,  @Param("descripcion")String descripcion,  @Param("avisos")String avisos,  @Param("recomendaciones")String recomendaciones);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE carpetadepruebas SET nombre = :nombreCarpeta WHERE id = :idCarpeta ", nativeQuery = true)
	void actualizarCarpetaDePruebas(@Param("idCarpeta")long idCarpeta, @Param("nombreCarpeta")String nombreCarpeta);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE pruebacomplementaria SET nombre = :nombrePrueba, duracion = :duracionPrueba, descripcion = :descripcion, avisos = :avisos, recomendaciones = :recomendaciones, separacion = :separacionSiguientePrueba WHERE id = :idPrueba ", nativeQuery = true)
	void actualizarPrueba(@Param("idPrueba")long idPrueba, @Param("nombrePrueba")String nombrePrueba, @Param("duracionPrueba")long duracionPrueba, @Param("separacionSiguientePrueba")long separacionSiguientePrueba,  @Param("descripcion")String descripcion,  @Param("avisos")String avisos,  @Param("recomendaciones")String recomendaciones);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM carpetadepruebas WHERE id = :idCarpeta ", nativeQuery = true)
	void eliminarCarpetaDePruebas(@Param("idCarpeta")long idCarpeta);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM pruebacomplementaria WHERE id = :idPrueba ", nativeQuery = true)
	void eliminarPrueba(@Param("idPrueba")long idPrueba);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO pruebascarpetadepruebas (id, carpetapruebas, prueba) VALUES (:idRelacion, :idCarpeta, :idPrueba) ", nativeQuery = true)
	void annadirPruebaACarpetaDePruebas(@Param("idRelacion")long idRelacion, @Param("idCarpeta")long idCarpeta, @Param("idPrueba")long idPrueba);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO requisitospruebas (id, prueba, muyrecomendable, edadInicio, edadFin) VALUES (:idRequisito, :idPrueba, :esMuyRecomendable, :edadInicio, :edadFin) ", nativeQuery = true)
	void annadirRequisitoAPrueba(@Param("idRequisito")long id, @Param("idPrueba")long idPrueba, @Param("esMuyRecomendable")boolean esMuyRecomendable, @Param("edadInicio")Long edadInicio, @Param("edadFin")Long edadFin);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO enfermeropruebas (id, enfermero, prueba) VALUES (:id, :usuarioEnfermero, :idPrueba) ", nativeQuery = true)
	void asignarNuevaPruebaAEnfermero(@Param("id")long id, @Param("usuarioEnfermero")long usuarioEnfermero, @Param("idPrueba")long idPrueba);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM requisitospruebas WHERE id = :idRequisito", nativeQuery = true)
	void eliminarRequisito(@Param("idRequisito")long idRequisito);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM enfermeropruebas WHERE prueba = :idPrueba AND enfermero = :usuarioEnfermero", nativeQuery = true)
	void desasignarPruebaAEnfermero(@Param("idPrueba")long idPrueba, @Param("usuarioEnfermero")long usuarioEnfermero);

}
