package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Entity
@Table(name = "imagenescentros")
public class ImagenCentro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "centro", nullable = false)
	private long idCentro;
	
	@Column(nullable = false)
	private String tipo;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(name = "datosimagen", nullable = false)
	private byte[] datosImagen;
	
	public ImagenCentro() {}
	
	public ImagenCentro(Object[] objeto) {
		this.setId(((BigInteger) objeto[0]).intValue());
		this.setIdCentro(((BigInteger) objeto[1]).intValue());
		this.setTipo((String) objeto[2]);
		this.setNombre((String) objeto[3]);
		this.setDatosImagen((byte[]) objeto[4]);
	}

	public ImagenCentro(long id, long idCentro, String tipo, String nombre, byte[] datosImagen) {
		this.id = id;
		this.idCentro = idCentro;
		this.tipo = tipo;
		this.nombre = nombre;
		this.datosImagen = datosImagen;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(long idCentro) {
		this.idCentro = idCentro;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public byte[] getDatosImagen() {
		return datosImagen;
	}

	public void setDatosImagen(byte[] datosImagen) {
		this.datosImagen = datosImagen;
	}
	
	public void comprimirDatos() {
		this.setDatosImagen(comprimirImagen(datosImagen));
	}
	
	public void descomprimirDatos() {
		this.setDatosImagen(descomprimirImagen(datosImagen));
	}
	
	public static byte[] comprimirImagen(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }



    public static byte[] descomprimirImagen(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

}
