import { IYear, NewYear } from './year.model';

export const sampleWithRequiredData: IYear = {
  id: '765a00d7-5aa7-4d2d-a944-0272e95cb1da',
  year: 13916,
};

export const sampleWithPartialData: IYear = {
  id: '52ccdb1d-6c59-4873-800b-980da43a61b1',
  year: 30084,
};

export const sampleWithFullData: IYear = {
  id: '8952af5e-fa6f-4e0d-8e28-8069bee1d348',
  year: 5038,
};

export const sampleWithNewData: NewYear = {
  year: 3468,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
