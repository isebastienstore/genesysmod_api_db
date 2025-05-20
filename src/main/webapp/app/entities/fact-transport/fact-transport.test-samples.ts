import { IFactTransport, NewFactTransport } from './fact-transport.model';

export const sampleWithRequiredData: IFactTransport = {
  id: '9094c935-2108-466b-a5db-4f960ba9520b',
  value: 21433.53,
  typeOfMobility: 'MOBILITY_PASSENGER_ROAD_RE',
};

export const sampleWithPartialData: IFactTransport = {
  id: '0444799d-87bc-4ea3-a47d-2296a421288a',
  value: 28979.89,
  typeOfMobility: 'MOBILITY_PASSENGER_AIR_CONV',
};

export const sampleWithFullData: IFactTransport = {
  id: '396b6fba-2ac8-4800-9fb4-cd5c5f4a80b4',
  value: 6206.87,
  typeOfMobility: 'MOBILITY_PASSENGER_AIR_CONV',
};

export const sampleWithNewData: NewFactTransport = {
  value: 4541.93,
  typeOfMobility: 'MOBILITY_FREIGHT_ROAD_CONV',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
