import dayjs from 'dayjs/esm';

import { IMetadata, NewMetadata } from './metadata.model';

export const sampleWithRequiredData: IMetadata = {
  id: '93c1af8d-6aaa-453f-9efb-1e4c493b530b',
};

export const sampleWithPartialData: IMetadata = {
  id: '496cd669-d48a-4905-b87a-4430095e89af',
};

export const sampleWithFullData: IMetadata = {
  id: 'f9e10795-065c-4d7e-b497-ed809f02c876',
  createdBy: 'longingly',
  updatedBy: 'yum difficult',
  action: 'expense',
  createdAt: dayjs('2025-05-19T22:23'),
  updatedAt: dayjs('2025-05-19T22:17'),
  source: 'excluding softly cripple',
};

export const sampleWithNewData: NewMetadata = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
