import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import Errores from 'src/constantes/errores';

@Injectable({
  providedIn: 'root'
})
export class ServicioCargando {
  mapaURLCargando: Map<string, boolean> = new Map<string, boolean>();
  observableCargandoURL: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  setCargando(loading: boolean, url: string): void {
    if (url) {
      if (loading === true) {
        this.mapaURLCargando.set(url, loading);
        this.observableCargandoURL.next(true);
      } else if (loading === false && this.mapaURLCargando.has(url)) {
        this.mapaURLCargando.delete(url);
      }
      if (this.mapaURLCargando.size === 0) {
        this.observableCargandoURL.next(false);
      }
    } else {
      throw new Error(Errores.MENSAJES.FALTA_URL);
    }
  }
}
