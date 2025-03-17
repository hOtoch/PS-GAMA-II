import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LocalizacaoResolve from './route/localizacao-routing-resolve.service';

const localizacaoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/localizacao.component').then(m => m.LocalizacaoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/localizacao-detail.component').then(m => m.LocalizacaoDetailComponent),
    resolve: {
      localizacao: LocalizacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/localizacao-update.component').then(m => m.LocalizacaoUpdateComponent),
    resolve: {
      localizacao: LocalizacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/localizacao-update.component').then(m => m.LocalizacaoUpdateComponent),
    resolve: {
      localizacao: LocalizacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default localizacaoRoute;
