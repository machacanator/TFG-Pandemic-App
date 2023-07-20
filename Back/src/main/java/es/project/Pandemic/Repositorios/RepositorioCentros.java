package es.project.Pandemic.Repositorios;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.project.Pandemic.EntidadesYClasesSecundarias.Centro;
@Repository
public interface RepositorioCentros extends JpaRepository<Centro, Long> {
	
	@Query(value = "SELECT id FROM centromedico ORDER BY id DESC LIMIT 1", nativeQuery = true)
	long getIdMasAltoCentro();
	
	@Query(value = "SELECT id FROM horariospruebascentros ORDER BY id DESC LIMIT 1", nativeQuery = true)
	long getIdMasAltoHorarios();
	
	@Query(value = "SELECT id FROM imagenescentros ORDER BY id DESC LIMIT 1", nativeQuery = true)
	Optional<Long> getIdMasAltoImagenCentro();
	
	@Query(value = "SELECT nombre FROM centromedico WHERE id = :idCentro ", nativeQuery = true)
	String getNombreDeCentro(@Param("idCentro")long idCentro);
	
	@Query(value = "SELECT * FROM centromedico WHERE id = :idCentro ", nativeQuery = true)
	Centro getCentro(@Param("idCentro")long idCentro);
	
	@Query(value = "SELECT * FROM centromedico ORDER BY id", nativeQuery = true)
	List<Centro> getTodosLosCentros();
	
	@Query(value = "SELECT * FROM imagenescentros  WHERE centro = :idCentro ", nativeQuery = true)
	List<Object[]> getImagenesCentro(@Param("idCentro")long idCentro);
	
	@Query(value = "SELECT DISTINCT prueba FROM horariospruebascentros  WHERE centro = :idCentro ", nativeQuery = true)
	List<Long> getListadoPruebasDeCentro(@Param("idCentro")long idCentro);
	
	@Query(value = "SELECT * FROM horariospruebascentros WHERE centro = :idCentro AND prueba = :idPrueba ORDER BY horainicio", nativeQuery = true)
	List<Object[]> getHorarios(@Param("idCentro")long idCentro, @Param("idPrueba")long idPrueba);
	
	@Query(value = "SELECT DISTINCT centro FROM horariospruebascentros WHERE prueba = :idPrueba", nativeQuery = true)
	List<Long> getCentroConPrueba(@Param("idPrueba")long idPrueba);
	
	@Query(value = "SELECT * FROM imagenescentros WHERE centro = :idCentro", nativeQuery = true)
	List<Object[]> getImagenesDeCentro(@Param("idCentro")long idCentro);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM horariospruebascentros WHERE id = :idHorario", nativeQuery = true)
	void eliminarHorario(@Param("idHorario")long idHorario);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO horariospruebascentros (id, centro, prueba, horainicio, horafin) VALUES (:idHorario, :idCentro, :idPrueba, :horaInicio, :horaFin)", nativeQuery = true)
	void annadirNuevoHorarioCentroPrueba(@Param("idHorario")long idHorario, @Param("idCentro")long idCentro, @Param("idPrueba")long idPrueba, @Param("horaInicio")Time horaInicio, @Param("horaFin")Time horaFin );
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE horariospruebascentros SET centro = :idCentro, prueba = :idPrueba, horainicio = :horaInicio, horafin = :horaFin WHERE id = :idHorario", nativeQuery = true)
	void actualizarHorarioCentroPrueba(@Param("idHorario")long idHorario, @Param("idCentro")long idCentro, @Param("idPrueba")long idPrueba, @Param("horaInicio")long horaInicio, @Param("horaFin")long horaFin );
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO imagenescentros (id, centro, tipo, nombre, datosimagen) VALUES (:idImagen, :idCentro, :tipoImagen, :nombreImagen, :datosImagen)", nativeQuery = true)
	void annadirImagenACentro(@Param("idImagen")long idImagen, @Param("idCentro")long idCentro, @Param("tipoImagen")String tipoImagen, @Param("nombreImagen")String nombreImagen, @Param("datosImagen")byte[] datosImagen);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM imagenescentros WHERE id = :idImagen ", nativeQuery = true)
	void eliminarImagenDeCentro(@Param("idImagen")long idImagen);

}