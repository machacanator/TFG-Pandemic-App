delete from seccion;

INSERT INTO public.seccion (nombre,seccionpadre,accesolibre,mostrarenmenu) VALUES
	 ('centro',NULL,false,true),
	 ('administrar plantilla de centro',13,false,true),
	 ('informacion prueba',5,true,true),
	 ('administrars pruebas complementarias de centro',13,false,true),
	 ('annadir nuevo empleado',NULL,false,false),
	 ('actualizar pruebas empleado',NULL,false,false),
	 ('annadir nuevo prueba a centro',NULL,false,false),
	 ('actualizar horarios prueba en centro',NULL,false,false),
	 ('comunidad autonoma',NULL,false,true),
	 ('citas',NULL,true,true);
INSERT INTO public.seccion (nombre,seccionpadre,accesolibre,mostrarenmenu) VALUES
	 ('nueva cita',1,true,true),
	 ('citas pendientes',1,true,true),
	 ('pruebas complementarias',NULL,true,true),
	 ('listado carpetas de pruebas comunidad',20,false,true),
	 ('listado pruebas comunidad',NULL,false,false),
	 ('listado centros comunidad',20,false,true),
	 ('annadir nueva prueba comunidad',NULL,false,false),
	 ('actualizar prueba comunidad',NULL,false,false),
	 ('mi hospital',NULL,true,true),
	 ('annadir nuevo centro comunidad',NULL,false,false);
INSERT INTO public.seccion (nombre,seccionpadre,accesolibre,mostrarenmenu) VALUES
	 ('informacion mi hospital',7,true,true),
	 ('agenda de citas',NULL,false,true),
	 ('actualizar centro comunidad',NULL,false,false),
	 ('estadisticas',NULL,false,true),
	 ('diario de citas',9,false,true),
	 ('gestionar cita',NULL,false,false),
	 ('historico de citas enfermero',9,false,true),
	 ('informacion estadisticas',28,false,true),
	 ('estadisticas empleados',28,false,true),
	 ('estadisticas centro',28,false,true);
INSERT INTO public.seccion (nombre,seccionpadre,accesolibre,mostrarenmenu) VALUES
	 ('estadisticas centro comunidad',28,false,true),
	 ('historico citas usuario',1,true,true),
	 ('estadisticas comunidad',28,false,true);
	 
	