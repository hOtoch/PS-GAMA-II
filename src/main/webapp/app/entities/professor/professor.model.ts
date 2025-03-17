import { ITurma } from 'app/entities/turma/turma.model';
import { AreaDoEnem } from 'app/entities/enumerations/area-do-enem.model';

export interface IProfessor {
  id: number;
  nome?: string | null;
  area?: keyof typeof AreaDoEnem | null;
  email?: string | null;
  turmas?: Pick<ITurma, 'id'>[] | null;
}

export type NewProfessor = Omit<IProfessor, 'id'> & { id: null };
