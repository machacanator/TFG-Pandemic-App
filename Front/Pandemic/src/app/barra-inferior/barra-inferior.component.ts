import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { faFacebook, faGithub, faInstagram, faTwitter, faYoutube } from '@fortawesome/free-brands-svg-icons';
import Errores from 'src/constantes/errores';
import Globales from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioDocumentos } from './servicio-documentos.service';

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'barra-inferior',
  templateUrl: './barra-inferior.component.html',
  styleUrls: ['./barra-inferior.component.scss']
})
export class BarraInferior implements OnInit {
  @Input() mostrarPieDePagina: boolean;

  youtubeLogo = faYoutube;
  twitterLogo = faTwitter;
  instagramLogo = faInstagram;
  facebookLogo = faFacebook;
  githubLogo = faGithub;
  
  constructor(
    private servicioPopUp: ServicioPopUp,  
    private servicioDocumentos: ServicioDocumentos,
  ) { }

  ngOnInit(): void {}

  descargarDocumentoAvisoLegal() {
    this.servicioDocumentos.getAvisoLegal()
      .then((datos: any) => {
        const archivo = new Blob([datos.body], { type: 'application/pdf' });
        let url = window.URL.createObjectURL(archivo);
        const enlace = document.createElement('a');
        enlace.href = url;
        enlace.setAttribute('download', `${Globales.NOMBRES_FICHEROS.AVISO_LEGAL}.pdf`);
        enlace.click();
      })
      .catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_DOCUMENTO));
  }

  descargarDocumentoTerminosYCondiciones() {    
    this.servicioDocumentos.getTerminosYCondiciones()
      .then((datos: any) => {
        const archivo = new Blob([datos.body], { type: 'application/pdf' });
        let url = window.URL.createObjectURL(archivo);
        const enlace = document.createElement('a');
        enlace.href = url;
        enlace.setAttribute('download', `${Globales.NOMBRES_FICHEROS.TERMINOS_Y_CONDICIONES}.pdf`);
        enlace.click();
      })
      .catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_DOCUMENTO));
  }

  redirigirRedSocial(nombreRedSocial: string) {
    switch(nombreRedSocial) {
      case "Youtube": window.location.href = "https://www.youtube.es"; break;
      case "Twitter": window.location.href = "https://www.twitter.com"; break;
      case "Instagram": window.location.href = "https://www.instagram.com"; break;
      case "Facebook": window.location.href = "https://www.facebook.com"; break;
      case "GitHub": window.location.href = "https://www.github.com"; break;
      default: break;
    }
  }

  redirigirAInfoEmpresa() {

  }
}
