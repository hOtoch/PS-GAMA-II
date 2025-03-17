export interface ILocalizacao {
  id: number;
  endereco?: string | null;
  cidade?: string | null;
  estado?: string | null;
  cep?: string | null;
}

export type NewLocalizacao = Omit<ILocalizacao, 'id'> & { id: null };
