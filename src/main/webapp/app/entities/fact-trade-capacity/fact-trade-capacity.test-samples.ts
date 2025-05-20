import { IFactTradeCapacity, NewFactTradeCapacity } from './fact-trade-capacity.model';

export const sampleWithRequiredData: IFactTradeCapacity = {
  id: '95903e9c-e5a1-4fa2-bf68-36d28c420d09',
  value: 7652.18,
};

export const sampleWithPartialData: IFactTradeCapacity = {
  id: 'be8e5238-ede6-4ec1-aff4-8e0618f1f0b1',
  value: 14109.09,
};

export const sampleWithFullData: IFactTradeCapacity = {
  id: 'a500e5c0-768c-4098-88d8-6ec8187ab68b',
  value: 28834.26,
};

export const sampleWithNewData: NewFactTradeCapacity = {
  value: 13633.81,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
