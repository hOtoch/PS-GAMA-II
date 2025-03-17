import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'aluno',
    data: { pageTitle: 'Alunos' },
    loadChildren: () => import('./aluno/aluno.routes'),
  },
  {
    path: 'professor',
    data: { pageTitle: 'Professors' },
    loadChildren: () => import('./professor/professor.routes'),
  },
  {
    path: 'turma',
    data: { pageTitle: 'Turmas' },
    loadChildren: () => import('./turma/turma.routes'),
  },
  {
    path: 'meta',
    data: { pageTitle: 'Metas' },
    loadChildren: () => import('./meta/meta.routes'),
  },
  {
    path: 'localizacao',
    data: { pageTitle: 'Localizacaos' },
    loadChildren: () => import('./localizacao/localizacao.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
