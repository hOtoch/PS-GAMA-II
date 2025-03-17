import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocalizacao } from '../localizacao.model';
import { LocalizacaoService } from '../service/localizacao.service';

const localizacaoResolve = (route: ActivatedRouteSnapshot): Observable<null | ILocalizacao> => {
  const id = route.params.id;
  if (id) {
    return inject(LocalizacaoService)
      .find(id)
      .pipe(
        mergeMap((localizacao: HttpResponse<ILocalizacao>) => {
          if (localizacao.body) {
            return of(localizacao.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default localizacaoResolve;
