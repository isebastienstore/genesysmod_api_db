import { IYear } from 'app/entities/year/year.model';
import { ICountry } from 'app/entities/country/country.model';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { ModalType } from 'app/entities/enumerations/modal-type.model';

export interface IFactTransport {
  id: string;
  value?: number | null;
  typeOfMobility?: keyof typeof ModalType | null;
  year?: IYear | null;
  country?: ICountry | null;
  metadata?: IMetadata | null;
}

export type NewFactTransport = Omit<IFactTransport, 'id'> & { id: null };
