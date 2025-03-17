import { ITurma } from 'app/entities/turma/turma.model';

export interface IAluno {
  id: number;
  nome?: string | null;
  idade?: number | null;
  email?: string | null;
  celular?: string | null;
  turma?: Pick<ITurma, 'id'> | null;
}

export type NewAluno = Omit<IAluno, 'id'> & { id: null };
