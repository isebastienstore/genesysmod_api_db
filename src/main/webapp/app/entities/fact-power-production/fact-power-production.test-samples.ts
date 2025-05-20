import { IFactPowerProduction, NewFactPowerProduction } from './fact-power-production.model';

export const sampleWithRequiredData: IFactPowerProduction = {
  id: '755d9cb6-5f7f-4f2d-9eea-84ccc2b5d95d',
  value: 14945.08,
};

export const sampleWithPartialData: IFactPowerProduction = {
  id: '464f54ae-ec3f-4388-a29f-b2d47327060e',
  value: 16389.61,
};

export const sampleWithFullData: IFactPowerProduction = {
  id: '63b69818-4754-45e4-8dcd-e8352baaf810',
  value: 28130.18,
};

export const sampleWithNewData: NewFactPowerProduction = {
  value: 6616.03,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
