import { ITechnology, NewTechnology } from './technology.model';

export const sampleWithRequiredData: ITechnology = {
  id: '6acc855f-0f16-411c-b1f1-9e13df30a42e',
  name: 'pish',
  category: 'RENEWABLE',
};

export const sampleWithPartialData: ITechnology = {
  id: 'bca33d41-7da3-4918-a8ae-a807da1863e9',
  name: 'whup',
  category: 'THERMAL',
};

export const sampleWithFullData: ITechnology = {
  id: 'a363a2f9-7397-4835-99eb-0ef31b7b5ac8',
  name: 'decision',
  category: 'THERMAL',
};

export const sampleWithNewData: NewTechnology = {
  name: 'fruitful from refine',
  category: 'THERMAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
