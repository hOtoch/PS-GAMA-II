import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocalizacao, NewLocalizacao } from '../localizacao.model';

export type PartialUpdateLocalizacao = Partial<ILocalizacao> & Pick<ILocalizacao, 'id'>;

export type EntityResponseType = HttpResponse<ILocalizacao>;
export type EntityArrayResponseType = HttpResponse<ILocalizacao[]>;

@Injectable({ providedIn: 'root' })
export class LocalizacaoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/localizacaos');

  create(localizacao: NewLocalizacao): Observable<EntityResponseType> {
    return this.http.post<ILocalizacao>(this.resourceUrl, localizacao, { observe: 'response' });
  }

  update(localizacao: ILocalizacao): Observable<EntityResponseType> {
    return this.http.put<ILocalizacao>(`${this.resourceUrl}/${this.getLocalizacaoIdentifier(localizacao)}`, localizacao, {
      observe: 'response',
    });
  }

  partialUpdate(localizacao: PartialUpdateLocalizacao): Observable<EntityResponseType> {
    return this.http.patch<ILocalizacao>(`${this.resourceUrl}/${this.getLocalizacaoIdentifier(localizacao)}`, localizacao, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocalizacao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalizacao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLocalizacaoIdentifier(localizacao: Pick<ILocalizacao, 'id'>): number {
    return localizacao.id;
  }

  compareLocalizacao(o1: Pick<ILocalizacao, 'id'> | null, o2: Pick<ILocalizacao, 'id'> | null): boolean {
    return o1 && o2 ? this.getLocalizacaoIdentifier(o1) === this.getLocalizacaoIdentifier(o2) : o1 === o2;
  }

  addLocalizacaoToCollectionIfMissing<Type extends Pick<ILocalizacao, 'id'>>(
    localizacaoCollection: Type[],
    ...localizacaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const localizacaos: Type[] = localizacaosToCheck.filter(isPresent);
    if (localizacaos.length > 0) {
      const localizacaoCollectionIdentifiers = localizacaoCollection.map(localizacaoItem => this.getLocalizacaoIdentifier(localizacaoItem));
      const localizacaosToAdd = localizacaos.filter(localizacaoItem => {
        const localizacaoIdentifier = this.getLocalizacaoIdentifier(localizacaoItem);
        if (localizacaoCollectionIdentifiers.includes(localizacaoIdentifier)) {
          return false;
        }
        localizacaoCollectionIdentifiers.push(localizacaoIdentifier);
        return true;
      });
      return [...localizacaosToAdd, ...localizacaoCollection];
    }
    return localizacaoCollection;
  }
}
