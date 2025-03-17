import { ILocalizacao, NewLocalizacao } from './localizacao.model';

export const sampleWithRequiredData: ILocalizacao = {
  id: 3662,
  endereco: 'whether ha',
  cidade: 'overplay formamide',
  estado: 'testing',
  cep: 'spanish gosh regal',
};

export const sampleWithPartialData: ILocalizacao = {
  id: 1444,
  endereco: 'convince patiently',
  cidade: 'near now provided',
  estado: 'tenant putrefy yippee',
  cep: 'sock behind',
};

export const sampleWithFullData: ILocalizacao = {
  id: 8832,
  endereco: 'present',
  cidade: 'in oh finally',
  estado: 'despite besides geez',
  cep: 'pace boo bell',
};

export const sampleWithNewData: NewLocalizacao = {
  endereco: 'snappy',
  cidade: 'yesterday lace provision',
  estado: 'skeleton hence yowza',
  cep: 'accessorise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
