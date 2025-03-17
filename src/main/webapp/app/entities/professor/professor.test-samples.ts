import { IProfessor, NewProfessor } from './professor.model';

export const sampleWithRequiredData: IProfessor = {
  id: 2741,
  nome: 'emboss opposite velvety',
  area: 'HUMANAS',
};

export const sampleWithPartialData: IProfessor = {
  id: 1632,
  nome: 'blah',
  area: 'LINGUAGENS',
};

export const sampleWithFullData: IProfessor = {
  id: 313,
  nome: 'excepting',
  area: 'LINGUAGENS',
  email: 'Marli_Reis29@bol.com.br',
};

export const sampleWithNewData: NewProfessor = {
  nome: 'unfortunately',
  area: 'NATUREZA',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
