import dayjs from 'dayjs/esm';

export interface IMetadata {
  id: string;
  createdBy?: string | null;
  updatedBy?: string | null;
  action?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  source?: string | null;
}

export type NewMetadata = Omit<IMetadata, 'id'> & { id: null };
