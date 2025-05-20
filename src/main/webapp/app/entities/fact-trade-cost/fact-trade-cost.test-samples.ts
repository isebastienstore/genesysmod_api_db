import { IFactTradeCost, NewFactTradeCost } from './fact-trade-cost.model';

export const sampleWithRequiredData: IFactTradeCost = {
  id: '40780822-eb32-4da6-9ca7-f7d05f1d85df',
  fixedCost: 14291.45,
  variableCost: 17589.89,
};

export const sampleWithPartialData: IFactTradeCost = {
  id: 'e41c957c-0d28-4aea-848e-82e01d4a6a0d',
  fixedCost: 16435.55,
  variableCost: 8420.29,
};

export const sampleWithFullData: IFactTradeCost = {
  id: '5486ec2e-d311-45f6-96d8-11fdb0fbbb52',
  fixedCost: 23218.03,
  variableCost: 6034.82,
};

export const sampleWithNewData: NewFactTradeCost = {
  fixedCost: 25855.35,
  variableCost: 30127.89,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
