import { IProfessor } from 'app/entities/professor/professor.model';

export interface ITurma {
  id: number;
  nomeTurma?: string | null;
  descricao?: string | null;
  professors?: Pick<IProfessor, 'id'>[] | null;
}

export type NewTurma = Omit<ITurma, 'id'> & { id: null };
