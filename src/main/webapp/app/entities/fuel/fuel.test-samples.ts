import { IFuel, NewFuel } from './fuel.model';

export const sampleWithRequiredData: IFuel = {
  id: '5ba513ae-267a-470d-8a52-ab72794a679f',
  name: 'downright helpfully daughter',
};

export const sampleWithPartialData: IFuel = {
  id: '3b201a2a-d730-46bb-a9bd-1ff6d20447ac',
  name: 'horst cutover bleak',
};

export const sampleWithFullData: IFuel = {
  id: 'd0cfd73b-0623-4cf2-bcf6-b072b09a0f39',
  name: 'hover drat',
};

export const sampleWithNewData: NewFuel = {
  name: 'concrete',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
