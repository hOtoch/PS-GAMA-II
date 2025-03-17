import { ITurma, NewTurma } from './turma.model';

export const sampleWithRequiredData: ITurma = {
  id: 29472,
  nomeTurma: 'around institute',
};

export const sampleWithPartialData: ITurma = {
  id: 30868,
  nomeTurma: 'pulverize assured woot',
  descricao: 'emboss finally than',
};

export const sampleWithFullData: ITurma = {
  id: 22300,
  nomeTurma: 'obvious sit',
  descricao: 'given ick',
};

export const sampleWithNewData: NewTurma = {
  nomeTurma: 'yum steep lovable',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
