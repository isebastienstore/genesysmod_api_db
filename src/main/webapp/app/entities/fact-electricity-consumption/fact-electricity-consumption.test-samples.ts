import { IFactElectricityConsumption, NewFactElectricityConsumption } from './fact-electricity-consumption.model';

export const sampleWithRequiredData: IFactElectricityConsumption = {
  id: '8af4f2b1-e678-4658-8c0f-da342a173f09',
  value: 18343.48,
};

export const sampleWithPartialData: IFactElectricityConsumption = {
  id: 'f785af6f-9ea0-4b75-acec-951ca64916ce',
  value: 2318.75,
};

export const sampleWithFullData: IFactElectricityConsumption = {
  id: '17bdabc4-1951-46c8-990e-889800874649',
  value: 10451.91,
};

export const sampleWithNewData: NewFactElectricityConsumption = {
  value: 27050.3,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
