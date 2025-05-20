import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 'fdc879a8-22a2-4576-a8ea-f6ecd3b8bffb',
  code: 'sq',
  name: 'whether',
};

export const sampleWithPartialData: ICountry = {
  id: '69cc4537-3023-444d-bb39-c9d55d016272',
  code: 'yu',
  name: 'dreamily',
};

export const sampleWithFullData: ICountry = {
  id: 'db413a1d-367f-406b-a380-c1fa201a845d',
  code: 'we',
  name: 'often suspension',
};

export const sampleWithNewData: NewCountry = {
  code: 'ab',
  name: 'exempt concerning what',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
