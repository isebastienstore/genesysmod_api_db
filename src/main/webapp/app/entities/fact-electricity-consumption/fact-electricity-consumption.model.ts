import { IYear } from 'app/entities/year/year.model';
import { ICountry } from 'app/entities/country/country.model';
import { IMetadata } from 'app/entities/metadata/metadata.model';

export interface IFactElectricityConsumption {
  id: string;
  value?: number | null;
  year?: IYear | null;
  country?: ICountry | null;
  metadata?: IMetadata | null;
}

export type NewFactElectricityConsumption = Omit<IFactElectricityConsumption, 'id'> & { id: null };
