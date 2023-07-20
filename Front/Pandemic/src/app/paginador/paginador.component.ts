import { Injectable } from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';
import Globales from 'src/constantes/globales';

@Injectable()
export class Paginador extends MatPaginatorIntl {

  override nextPageLabel = Globales.TEXTO_PAGINADOR.SIGUIENTE_PAGINA;
  override previousPageLabel = Globales.TEXTO_PAGINADOR.PAGINA_ANTERIOR;
  override firstPageLabel = Globales.TEXTO_PAGINADOR.PRIMERA_PAGINA;
  override lastPageLabel = Globales.TEXTO_PAGINADOR.ULTIMA_PAGINA;
  override itemsPerPageLabel = Globales.TEXTO_PAGINADOR.RESULTADOS_POR_PAGINA;

}
