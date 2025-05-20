import { IFactPowerPlants, NewFactPowerPlants } from './fact-power-plants.model';

export const sampleWithRequiredData: IFactPowerPlants = {
  id: '0096df3d-0cff-4949-b63a-80fab4aca142',
  name: 'supposing unusual clone',
  intalledCapacity: 17177.59,
  availabilityCapacity: 21305.68,
};

export const sampleWithPartialData: IFactPowerPlants = {
  id: 'a28b0eb9-f348-4571-ad9f-ba46b610b859',
  name: 'hexagon but',
  intalledCapacity: 26143.03,
  availabilityCapacity: 7103.15,
  status: 'PLANNED',
};

export const sampleWithFullData: IFactPowerPlants = {
  id: '4f78af7a-272b-41d6-8e1c-f831cb4b05ec',
  name: 'commonly strict squeaky',
  intalledCapacity: 15336.69,
  availabilityCapacity: 25330.39,
  status: 'PLANNED',
};

export const sampleWithNewData: NewFactPowerPlants = {
  name: 'barring down yellowish',
  intalledCapacity: 30597.13,
  availabilityCapacity: 833.81,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
