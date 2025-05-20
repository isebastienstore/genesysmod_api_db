import { IFactRenewablePotential, NewFactRenewablePotential } from './fact-renewable-potential.model';

export const sampleWithRequiredData: IFactRenewablePotential = {
  id: 'f3a9d934-707b-4c72-879e-df7e3dc89f83',
  maxCapacity: 7063.39,
  availableCapacity: 21916.58,
  minCapacity: 28823.44,
};

export const sampleWithPartialData: IFactRenewablePotential = {
  id: 'f4bcd6ba-cbd2-4039-a1bc-f0e55ef21c27',
  maxCapacity: 21413.52,
  availableCapacity: 27973.98,
  minCapacity: 12621.8,
};

export const sampleWithFullData: IFactRenewablePotential = {
  id: '3c7781db-9b1d-4451-b3f5-5ff7c32b656f',
  maxCapacity: 17997.97,
  availableCapacity: 12483.85,
  minCapacity: 1872.19,
};

export const sampleWithNewData: NewFactRenewablePotential = {
  maxCapacity: 2299.27,
  availableCapacity: 32429.61,
  minCapacity: 3527.52,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
