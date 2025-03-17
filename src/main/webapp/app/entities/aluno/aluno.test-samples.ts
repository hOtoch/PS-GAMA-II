import { IAluno, NewAluno } from './aluno.model';

export const sampleWithRequiredData: IAluno = {
  id: 8806,
  nome: 'boo tomorrow',
};

export const sampleWithPartialData: IAluno = {
  id: 12879,
  nome: 'unfit wonderfully',
  idade: 19209,
  celular: 'voluminous throughout',
};

export const sampleWithFullData: IAluno = {
  id: 25732,
  nome: 'oof near likewise',
  idade: 7292,
  email: 'Lavinia_Souza12@hotmail.com',
  celular: 'misfire',
};

export const sampleWithNewData: NewAluno = {
  nome: 'energetically yet webbed',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
